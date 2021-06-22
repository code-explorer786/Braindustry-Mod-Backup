package mindustry.entities.comp;

import arc.Core;
import arc.Events;
import arc.func.Boolf;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.struct.Queue;
import arc.util.Nullable;
import arc.util.Structs;
import arc.util.Time;
import java.util.Arrays;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.game.EventType.BuildSelectEvent;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Posc;
import mindustry.gen.Rotc;
import mindustry.gen.Statusc;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.ConstructBlock.ConstructBuild;
import mindustry.world.blocks.storage.CoreBlock.CoreBuild;

abstract class BuilderComp implements Posc, Statusc, Teamc, Rotc {
   static final Vec2[] vecs = new Vec2[]{new Vec2(), new Vec2(), new Vec2(), new Vec2()};
   float x;
   float y;
   float rotation;
   float buildSpeedMultiplier;
   UnitType type;
   Team team;
   Queue plans = new Queue(1);
   boolean updateBuilding = true;
   private transient BuildPlan lastActive;
   private transient int lastSize;
   private transient float buildAlpha = 0.0F;

   public boolean canBuild() {
      return this.type.buildSpeed > 0.0F && this.buildSpeedMultiplier > 0.0F;
   }

   public void update() {
      if (!Vars.headless) {
         if (this.lastActive != null && this.buildAlpha <= 0.01F) {
            this.lastActive = null;
         }

         this.buildAlpha = Mathf.lerpDelta(this.buildAlpha, this.activelyBuilding() ? 1.0F : 0.0F, 0.15F);
      }

      if (this.updateBuilding && this.canBuild()) {
         float finalPlaceDst = Vars.state.rules.infiniteResources ? Float.MAX_VALUE : 220.0F;
         boolean infinite = Vars.state.rules.infiniteResources || this.team().rules().infiniteResources;
         Iterator it = this.plans.iterator();

         while(true) {
            BuildPlan req;
            Tile tile;
            do {
               if (!it.hasNext()) {
                  Building core = this.core();
                  if (this.buildPlan() == null) {
                     return;
                  }

                  BuildPlan req;
                  if (this.plans.size > 1) {
                     for(int total = 0; (!this.within((req = this.buildPlan()).tile(), finalPlaceDst) || this.shouldSkip(req, core)) && total < this.plans.size; ++total) {
                        this.plans.removeFirst();
                        this.plans.addLast(req);
                     }
                  }

                  BuildPlan current = this.buildPlan();
                  Tile tile = current.tile();
                  this.lastActive = current;
                  this.buildAlpha = 1.0F;
                  if (current.breaking) {
                     this.lastSize = tile.block().size;
                  }

                  if (!this.within(tile, finalPlaceDst)) {
                     return;
                  }

                  Building var8 = tile.build;
                  ConstructBuild entity;
                  if (var8 instanceof ConstructBuild && (entity = (ConstructBuild)var8) == (ConstructBuild)var8) {
                     if (tile.team() != this.team && tile.team() != Team.derelict || !current.breaking && (entity.cblock != current.block || entity.tile != current.tile())) {
                        this.plans.removeFirst();
                        return;
                     }
                  } else if (!current.initialized && !current.breaking && Build.validPlace(current.block, this.team, current.x, current.y, current.rotation)) {
                     boolean hasAll = infinite || current.isRotation(this.team) || !Structs.contains(current.block.requirements, (i) -> {
                        return core != null && !core.items.has(i.item);
                     });
                     if (hasAll) {
                        Call.beginPlace((Unit)this.self(), current.block, this.team, current.x, current.y, current.rotation);
                     } else {
                        current.stuck = true;
                     }
                  } else {
                     if (current.initialized || !current.breaking || !Build.validBreak(this.team, current.x, current.y)) {
                        this.plans.removeFirst();
                        return;
                     }

                     Call.beginBreak((Unit)this.self(), this.team, current.x, current.y);
                  }

                  if (tile.build instanceof ConstructBuild && !current.initialized) {
                     Core.app.post(() -> {
                        Events.fire(new BuildSelectEvent(tile, this.team, (Unit)this.self(), current.breaking));
                     });
                     current.initialized = true;
                  }

                  if (core != null || infinite) {
                     var8 = tile.build;
                     if (var8 instanceof ConstructBuild && (entity = (ConstructBuild)var8) == (ConstructBuild)var8) {
                        if (current.breaking) {
                           entity.deconstruct((Unit)this.self(), core, 1.0F / entity.buildCost * Time.delta * this.type.buildSpeed * this.buildSpeedMultiplier * Vars.state.rules.buildSpeedMultiplier);
                        } else {
                           entity.construct((Unit)this.self(), core, 1.0F / entity.buildCost * Time.delta * this.type.buildSpeed * this.buildSpeedMultiplier * Vars.state.rules.buildSpeedMultiplier, current.config);
                        }

                        current.stuck = Mathf.equal(current.progress, entity.progress);
                        current.progress = entity.progress;
                        return;
                     }
                  }

                  return;
               }

               req = (BuildPlan)it.next();
               tile = Vars.world.tile(req.x, req.y);
            } while(tile != null && (!req.breaking || tile.block() != Blocks.air) && (req.breaking || (tile.build == null || tile.build.rotation != req.rotation) && req.block.rotate || tile.block() != req.block));

            it.remove();
         }
      }
   }

   void drawBuildPlans() {
      Boolf skip = (planx) -> {
         return planx.progress > 0.01F || this.buildPlan() == planx && planx.initialized && (this.within((float)(planx.x * 8), (float)(planx.y * 8), 220.0F) || Vars.state.isEditor());
      };

      for(int i = 0; i < 2; ++i) {
         Iterator var3 = this.plans.iterator();

         while(var3.hasNext()) {
            BuildPlan plan = (BuildPlan)var3.next();
            if (!skip.get(plan)) {
               if (i == 0) {
                  this.drawPlan(plan, 1.0F);
               } else {
                  this.drawPlanTop(plan, 1.0F);
               }
            }
         }
      }

      Draw.reset();
   }

   void drawPlan(BuildPlan request, float alpha) {
      request.animScale = 1.0F;
      if (request.breaking) {
         Vars.control.input.drawBreaking(request);
      } else {
         request.block.drawPlan(request, Vars.control.input.allRequests(), Build.validPlace(request.block, this.team, request.x, request.y, request.rotation) || Vars.control.input.requestMatches(request), alpha);
      }

   }

   void drawPlanTop(BuildPlan request, float alpha) {
      if (!request.breaking) {
         Draw.reset();
         Draw.mixcol(Color.white, 0.24F + Mathf.absin(Time.globalTime, 6.0F, 0.28F));
         Draw.alpha(alpha);
         request.block.drawRequestConfigTop(request, this.plans);
      }

   }

   boolean shouldSkip(BuildPlan request, @Nullable Building core) {
      if (!Vars.state.rules.infiniteResources && !this.team.rules().infiniteResources && !request.breaking && core != null && !request.isRotation(this.team)) {
         return request.stuck && !core.items.has(request.block.requirements) || Structs.contains(request.block.requirements, (i) -> {
            return !core.items.has(i.item) && Mathf.round((float)i.amount * Vars.state.rules.buildCostMultiplier) > 0;
         }) && !request.initialized;
      } else {
         return false;
      }
   }

   void removeBuild(int x, int y, boolean breaking) {
      int idx = this.plans.indexOf((req) -> {
         return req.breaking == breaking && req.x == x && req.y == y;
      });
      if (idx != -1) {
         this.plans.removeIndex(idx);
      }

   }

   boolean isBuilding() {
      return this.plans.size != 0;
   }

   void clearBuilding() {
      this.plans.clear();
   }

   void addBuild(BuildPlan place) {
      this.addBuild(place, true);
   }

   void addBuild(BuildPlan place, boolean tail) {
      if (this.canBuild()) {
         BuildPlan replace = null;
         Iterator var4 = this.plans.iterator();

         while(var4.hasNext()) {
            BuildPlan request = (BuildPlan)var4.next();
            if (request.x == place.x && request.y == place.y) {
               replace = request;
               break;
            }
         }

         if (replace != null) {
            this.plans.remove(replace);
         }

         Tile tile = Vars.world.tile(place.x, place.y);
         if (tile != null) {
            Building var6 = tile.build;
            ConstructBuild cons;
            if (var6 instanceof ConstructBuild && (cons = (ConstructBuild)var6) == (ConstructBuild)var6) {
               place.progress = cons.progress;
            }
         }

         if (tail) {
            this.plans.addLast(place);
         } else {
            this.plans.addFirst(place);
         }

      }
   }

   boolean activelyBuilding() {
      if (this.isBuilding() && !Vars.state.isEditor() && !this.within(this.buildPlan(), Vars.state.rules.infiniteResources ? Float.MAX_VALUE : 220.0F)) {
         return false;
      } else {
         return this.isBuilding() && this.updateBuilding;
      }
   }

   @Nullable
   BuildPlan buildPlan() {
      return this.plans.size == 0 ? null : (BuildPlan)this.plans.first();
   }

   public void draw() {
      boolean active = this.activelyBuilding();
      if (active || this.lastActive != null) {
         Draw.z(115.0F);
         BuildPlan plan = active ? this.buildPlan() : this.lastActive;
         Tile tile = Vars.world.tile(plan.x, plan.y);
         CoreBuild core = this.team.core();
         if (tile != null && this.within(plan, Vars.state.rules.infiniteResources ? Float.MAX_VALUE : 220.0F)) {
            if (core != null && active && !this.isLocal() && !(tile.block() instanceof ConstructBlock)) {
               Draw.z(84.0F);
               this.drawPlan(plan, 0.5F);
               this.drawPlanTop(plan, 0.5F);
               Draw.z(115.0F);
            }

            int size = plan.breaking ? (active ? tile.block().size : this.lastSize) : plan.block.size;
            float tx = plan.drawx();
            float ty = plan.drawy();
            Lines.stroke(1.0F, plan.breaking ? Pal.remove : Pal.accent);
            float focusLen = this.type.buildBeamOffset + Mathf.absin(Time.time, 3.0F, 0.6F);
            float px = this.x + Angles.trnsx(this.rotation, focusLen);
            float py = this.y + Angles.trnsy(this.rotation, focusLen);
            float sz = (float)(8 * size) / 2.0F;
            float ang = this.angleTo(tx, ty);
            vecs[0].set(tx - sz, ty - sz);
            vecs[1].set(tx + sz, ty - sz);
            vecs[2].set(tx - sz, ty + sz);
            vecs[3].set(tx + sz, ty + sz);
            Arrays.sort(vecs, Structs.comparingFloat((vec) -> {
               return -Angles.angleDist(this.angleTo(vec), ang);
            }));
            Vec2 close = (Vec2)Geometry.findClosest(this.x, this.y, vecs);
            float x1 = vecs[0].x;
            float y1 = vecs[0].y;
            float x2 = close.x;
            float y2 = close.y;
            float x3 = vecs[1].x;
            float y3 = vecs[1].y;
            Draw.z(122.0F);
            Draw.alpha(this.buildAlpha);
            if (!active && !(tile.build instanceof ConstructBuild)) {
               Fill.square(plan.drawx(), plan.drawy(), (float)(size * 8) / 2.0F);
            }

            if (Vars.renderer.animateShields) {
               if (close != vecs[0] && close != vecs[1]) {
                  Fill.tri(px, py, x1, y1, x2, y2);
                  Fill.tri(px, py, x3, y3, x2, y2);
               } else {
                  Fill.tri(px, py, x1, y1, x3, y3);
               }
            } else {
               Lines.line(px, py, x1, y1);
               Lines.line(px, py, x3, y3);
            }

            Fill.square(px, py, 1.8F + Mathf.absin(Time.time, 2.2F, 1.1F), this.rotation + 45.0F);
            Draw.reset();
            Draw.z(115.0F);
         }
      }
   }
}
