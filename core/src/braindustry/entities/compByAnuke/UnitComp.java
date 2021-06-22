package braindustry.entities.compByAnuke;
import arc.Events;
import arc.func.Cons;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Time;
import arc.util.Tmp;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.ai.types.FormationAI;
import mindustry.ai.types.LogicAI;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.ctype.Content;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.Units;
import mindustry.entities.abilities.Ability;
import mindustry.entities.units.AIController;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.game.EventType.Trigger;
import mindustry.game.EventType.UnitDestroyEvent;
import mindustry.gen.Boundedc;
import mindustry.gen.Builderc;
import mindustry.gen.Call;
import mindustry.gen.Commanderc;
import mindustry.gen.Drawc;
import mindustry.gen.Entityc;
import mindustry.gen.Healthc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Itemsc;
import mindustry.gen.Minerc;
import mindustry.gen.Payloadc;
import mindustry.gen.Physicsc;
import mindustry.gen.Player;
import mindustry.gen.Rotc;
import mindustry.gen.Shieldc;
import mindustry.gen.Statusc;
import mindustry.gen.Syncc;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.gen.Weaponsc;
import mindustry.logic.LAccess;
import mindustry.logic.Ranged;
import mindustry.logic.Senseable;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.ui.Cicon;
import mindustry.ui.Displayable;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.UnitPayload;

import arc.*;
import arc.func.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.ai.types.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.ctype.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.units.*;
import mindustry.game.EventType.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.logic.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.payloads.*;

import static mindustry.Vars.*;
import static mindustry.logic.GlobalConstants.*;

@braindustry.annotations.ModAnnotations.Component(base = true)
abstract class UnitComp implements Healthc, Physicsc, Hitboxc, Statusc, Teamc, Itemsc, Rotc, Unitc, Weaponsc, Drawc, Boundedc, Syncc, Shieldc, Commanderc, Displayable, Senseable, Ranged, Minerc, Builderc{

    @braindustry.annotations.ModAnnotations.Import boolean hovering, dead, disarmed;
    @braindustry.annotations.ModAnnotations.Import float x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, ammo, minFormationSpeed, dragMultiplier;
    @braindustry.annotations.ModAnnotations.Import Team team;
    @braindustry.annotations.ModAnnotations.Import int id;
    @braindustry.annotations.ModAnnotations.Import @Nullable Tile mineTile;
    @braindustry.annotations.ModAnnotations.Import Vec2 vel;

    private UnitController controller;
    UnitType type;
    boolean spawnedByCore;
    double flag;

    transient Seq<Ability> abilities = new Seq<>(0);
    private transient float resupplyTime = Mathf.random(10f);
    private transient boolean wasPlayer;
   public void moveAt(Vec2 vector) {
      this.moveAt(vector, this.type.accel);
   }

   public void approach(Vec2 vector) {
      this.vel.approachDelta(vector, this.type.accel * this.realSpeed());
   }

   public void aimLook(Position pos) {
      this.aim(pos);
      this.lookAt(pos);
   }

   public void aimLook(float x, float y) {
      this.aim(x, y);
      this.lookAt(x, y);
   }

   public boolean inRange(Position other) {
      return this.within(other, this.type.range);
   }

   public boolean hasWeapons() {
      return this.type.hasWeapons();
   }

   public float speed() {
      float strafePenalty = !this.isGrounded() && this.isPlayer() ? Mathf.lerp(1.0F, this.type.strafePenalty, Angles.angleDist(this.vel().angle(), this.rotation) / 180.0F) : 1.0F;
      return (this.isCommanding() ? this.minFormationSpeed * 0.98F : this.type.speed) * strafePenalty;
   }

   public float realSpeed() {
      return Mathf.lerp(1.0F, this.type.canBoost ? this.type.boostMultiplier : 1.0F, this.elevation) * this.speed() * this.floorSpeedMultiplier();
   }

   public void eachGroup(Cons cons) {
      cons.get((Unit)this.self());
      this.controlling().each(cons);
   }

   public float prefRotation() {
      if (this.activelyBuilding()) {
         return this.angleTo(this.buildPlan());
      } else if (this.mineTile != null) {
         return this.angleTo(this.mineTile);
      } else {
         return this.moving() ? this.vel().angle() : this.rotation;
      }
   }

   public float range() {
      return this.type.maxRange;
   }

@braindustry.annotations.ModAnnotations.Replace
   public float clipSize() {
      if (this.isBuilding()) {
         return Vars.state.rules.infiniteResources ? Float.MAX_VALUE : Math.max(this.type.clipSize, (float)this.type.region.width) + 220.0F + 32.0F;
      } else {
         return Math.max((float)this.type.region.width * 2.0F, this.type.clipSize);
      }
   }

   public double sense(LAccess sensor) {
      double var10000;
      switch(sensor) {
      case totalItems:
         var10000 = (double)this.stack().amount;
         break;
      case itemCapacity:
         var10000 = (double)this.type.itemCapacity;
         break;
      case rotation:
         var10000 = (double)this.rotation;
         break;
      case health:
         var10000 = (double)this.health;
         break;
      case maxHealth:
         var10000 = (double)this.maxHealth;
         break;
      case ammo:
         var10000 = !Vars.state.rules.unitAmmo ? (double)this.type.ammoCapacity : (double)this.ammo;
         break;
      case ammoCapacity:
         var10000 = (double)this.type.ammoCapacity;
         break;
      case x:
         var10000 = (double)World.conv(this.x);
         break;
      case y:
         var10000 = (double)World.conv(this.y);
         break;
      case dead:
         var10000 = !this.dead && this.isAdded() ? 0.0D : 1.0D;
         break;
      case team:
         var10000 = (double)this.team.id;
         break;
      case shooting:
         var10000 = this.isShooting() ? 1.0D : 0.0D;
         break;
      case boosting:
         var10000 = this.type.canBoost && this.isFlying() ? 1.0D : 0.0D;
         break;
      case range:
         var10000 = (double)(this.range() / 8.0F);
         break;
      case shootX:
         var10000 = (double)World.conv(this.aimX());
         break;
      case shootY:
         var10000 = (double)World.conv(this.aimY());
         break;
      case mining:
         var10000 = this.mining() ? 1.0D : 0.0D;
         break;
      case mineX:
         var10000 = this.mining() ? (double)this.mineTile.x : -1.0D;
         break;
      case mineY:
         var10000 = this.mining() ? (double)this.mineTile.y : -1.0D;
         break;
      case flag:
         var10000 = this.flag;
         break;
      case controlled:
         var10000 = !this.isValid() ? 0.0D : (this.controller instanceof LogicAI ? 1.0D : (this.controller instanceof Player ? 2.0D : (this.controller instanceof FormationAI ? 3.0D : 0.0D)));
         break;
      case commanded:
         var10000 = this.controller instanceof FormationAI && this.isValid() ? 1.0D : 0.0D;
         break;
      case payloadCount:
         Entityc var3 = this.self();
         Payloadc pay;
         var10000 = (double)(var3 instanceof Payloadc && (pay = (Payloadc)var3) == (Payloadc)var3 ? pay.payloads().size : 0);
         break;
      case size:
         var10000 = (double)(this.hitSize / 8.0F);
         break;
      default:
         var10000 = Double.NaN;
      }

      return var10000;
   }

   public Object senseObject(LAccess sensor) {
      Object var10000;
      switch(sensor) {
      case type:
         var10000 = this.type;
         break;
      case name:
         UnitController var9 = this.controller;
         Player p;
         var10000 = var9 instanceof Player && (p = (Player)var9) == (Player)var9 ? p.name : null;
         break;
      case firstItem:
         var10000 = this.stack().amount == 0 ? null : this.item();
         break;
      case controller:
         if (!this.isValid()) {
            var10000 = null;
         } else {
            UnitController var10 = this.controller;
            LogicAI log;
            if (var10 instanceof LogicAI && (log = (LogicAI)var10) == (LogicAI)var10) {
               var10000 = log.controller;
            } else {
               var10 = this.controller;
               FormationAI form;
               var10000 = var10 instanceof FormationAI && (form = (FormationAI)var10) == (FormationAI)var10 ? form.leader : this;
            }
         }
         break;
      case payloadType:
         Entityc var5 = this.self();
         Payloadc pay;
         if (var5 instanceof Payloadc && (pay = (Payloadc)var5) == (Payloadc)var5) {
            if (pay.payloads().isEmpty()) {
               var10000 = null;
            } else {
               Object var11 = pay.payloads().peek();
               UnitPayload p1;
               if (var11 instanceof UnitPayload && (p1 = (UnitPayload)var11) == (UnitPayload)var11) {
                  var10000 = p1.unit.type;
               } else {
                  var11 = pay.payloads().peek();
                  BuildPayload p2;
                  var10000 = var11 instanceof BuildPayload && (p2 = (BuildPayload)var11) == (BuildPayload)var11 ? p2.block() : null;
               }
            }
         } else {
            var10000 = null;
         }
         break;
      default:
         var10000 = noSensed;
      }

      return var10000;
   }

   public double sense(Content content) {
      return content == this.stack().item ? (double)this.stack().amount : Double.NaN;
   }

@braindustry.annotations.ModAnnotations.Replace
   public boolean canDrown() {
      return this.isGrounded() && !this.hovering && this.type.canDrown;
   }

@braindustry.annotations.ModAnnotations.Replace
   public boolean canShoot() {
      return !this.disarmed && (!this.type.canBoost || !this.isFlying());
   }

   public boolean isCounted() {
      return this.type.isCounted;
   }

   public int itemCapacity() {
      return this.type.itemCapacity;
   }

   public float bounds() {
      return this.hitSize * 2.0F;
   }

   public void controller(UnitController next) {
      this.controller = next;
      if (this.controller.unit() != this.self()) {
         this.controller.unit((Unit)this.self());
      }

   }

   public UnitController controller() {
      return this.controller;
   }

   public void resetController() {
      this.controller(this.type.createController());
   }

   public void set(UnitType def, UnitController controller) {
      if (this.type != def) {
         this.setType(def);
      }

      this.controller(controller);
   }

   public int pathType() {
      return 0;
   }

   public void lookAt(float angle) {
      this.rotation = Angles.moveToward(this.rotation, angle, this.type.rotateSpeed * Time.delta * this.speedMultiplier());
   }

   public void lookAt(Position pos) {
      this.lookAt(this.angleTo(pos));
   }

   public void lookAt(float x, float y) {
      this.lookAt(this.angleTo(x, y));
   }

   public boolean isAI() {
      return this.controller instanceof AIController;
   }

   public int count() {
      return this.team.data().countType(this.type);
   }

   public int cap() {
      return Units.getCap(this.team);
   }

   public void setType(UnitType type) {
      this.type = type;
      this.maxHealth = type.health;
      this.drag = type.drag;
      this.armor = type.armor;
      this.hitSize = type.hitSize;
      this.hovering = type.hovering;
      if (this.controller == null) {
         this.controller(type.createController());
      }

      if (this.mounts().length != type.weapons.size) {
         this.setupWeapons(type);
      }

      if (this.abilities.size != type.abilities.size) {
         this.abilities = type.abilities.map(Ability::copy);
      }

   }

   public void afterSync() {
      this.setType(this.type);
      this.controller.unit((Unit)this.self());
   }

   public void afterRead() {
      this.afterSync();
      this.controller(this.type.createController());
   }

   public void add() {
      this.team.data().updateCount(this.type, 1);
      if (this.count() > this.cap() && !this.spawnedByCore && !this.dead && !Vars.state.rules.editor) {
         Call.unitCapDeath((Unit)this.self());
         this.team.data().updateCount(this.type, -1);
      }

   }

   public void remove() {
      this.team.data().updateCount(this.type, -1);
      this.controller.removed((Unit)this.self());
   }

   public void landed() {
      if (this.type.landShake > 0.0F) {
         Effect.shake(this.type.landShake, this.type.landShake, this);
      }

      this.type.landed((Unit)this.self());
   }

   public void update() {
      this.type.update((Unit)this.self());
      if (Vars.state.rules.unitAmmo && this.ammo < (float)this.type.ammoCapacity - 1.0E-4F) {
         this.resupplyTime += Time.delta;
         if (this.resupplyTime > 10.0F) {
            this.type.ammoType.resupply((Unit)this.self());
            this.resupplyTime = 0.0F;
         }
      }

      if (this.abilities.size > 0) {
         Iterator var1 = this.abilities.iterator();

         while(var1.hasNext()) {
            Ability a = (Ability)var1.next();
            a.update((Unit)this.self());
         }
      }

      this.drag = this.type.drag * (this.isGrounded() ? this.floorOn().dragMultiplier : 1.0F) * this.dragMultiplier;
      float offset;
      if (this.team != Vars.state.rules.waveTeam && Vars.state.hasSpawns() && (!Vars.net.client() || this.isLocal())) {
         offset = Vars.state.rules.dropZoneRadius + this.hitSize / 2.0F + 1.0F;
         Iterator var6 = Vars.spawner.getSpawns().iterator();

         while(var6.hasNext()) {
            Tile spawn = (Tile)var6.next();
            if (this.within(spawn.worldx(), spawn.worldy(), offset)) {
               this.vel().add(Tmp.v1.set(this).sub(spawn.worldx(), spawn.worldy()).setLength(1.1F - this.dst(spawn) / offset).scl(0.45F * Time.delta));
            }
         }
      }

      if (this.dead || this.health <= 0.0F) {
         this.drag = 0.01F;
         if (Mathf.chanceDelta(0.1D)) {
            Tmp.v1.setToRandomDirection().scl(this.hitSize);
            this.type.fallEffect.at(this.x + Tmp.v1.x, this.y + Tmp.v1.y);
         }

         if (Mathf.chanceDelta(0.2D)) {
            offset = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * this.elevation;
            float range = Mathf.range(this.type.engineSize);
            this.type.fallThrusterEffect.at(this.x + Angles.trnsx(this.rotation + 180.0F, offset) + Mathf.range(range), this.y + Angles.trnsy(this.rotation + 180.0F, offset) + Mathf.range(range), Mathf.random());
         }

         this.elevation -= this.type.fallSpeed * Time.delta;
         if (this.isGrounded()) {
            this.destroy();
         }
      }

      Tile tile = this.tileOn();
      Floor floor = this.floorOn();
      if (tile != null && this.isGrounded() && !this.type.hovering) {
         if (tile.build != null) {
            tile.build.unitOn((Unit)this.self());
         }

         if (floor.damageTaken > 0.0F) {
            this.damageContinuous(floor.damageTaken);
         }
      }

      if (tile != null && !this.canPassOn()) {
         if (this.type.canBoost) {
            this.elevation = 1.0F;
         } else if (!Vars.net.client()) {
            this.kill();
         }
      }

      if (!Vars.net.client() && !this.dead) {
         this.controller.updateUnit();
      }

      if (!this.controller.isValidController()) {
         this.resetController();
      }

      if (this.spawnedByCore && !this.isPlayer() && !this.dead) {
         Call.unitDespawn((Unit)this.self());
      }

   }

   public TextureRegion icon() {
      return this.type.icon(Cicon.full);
   }

   public void destroy() {
      if (this.isAdded()) {
         float explosiveness = 2.0F + this.item().explosiveness * (float)this.stack().amount * 1.53F;
         float flammability = this.item().flammability * (float)this.stack().amount / 1.9F;
         float power = this.item().charge * (float)this.stack().amount * 150.0F;
         if (!this.spawnedByCore) {
            Damage.dynamicExplosion(this.x, this.y, flammability, explosiveness, power, this.bounds() / 2.0F, Vars.state.rules.damageExplosions, this.item().flammability > 1.0F, this.team);
         }

         float shake = this.hitSize / 3.0F;
         Effect.scorch(this.x, this.y, (int)(this.hitSize / 5.0F));
         Fx.explosion.at(this);
         Effect.shake(shake, shake, this);
         this.type.deathSound.at(this);
         Events.fire(new UnitDestroyEvent((Unit)this.self()));
         if (explosiveness > 7.0F && (this.isLocal() || this.wasPlayer)) {
            Events.fire(Trigger.suicideBomb);
         }

         if (this.type.flying && !this.spawnedByCore) {
            Damage.damage(this.team, this.x, this.y, Mathf.pow(this.hitSize, 0.94F) * 1.25F, Mathf.pow(this.hitSize, 0.75F) * this.type.crashDamageMultiplier * 5.0F, true, false, true);
         }

         if (!Vars.headless) {
            for(int i = 0; i < this.type.wreckRegions.length; ++i) {
               if (this.type.wreckRegions[i].found()) {
                  float range = this.type.hitSize / 4.0F;
                  Tmp.v1.rnd(range);
                  Effect.decal(this.type.wreckRegions[i], this.x + Tmp.v1.x, this.y + Tmp.v1.y, this.rotation - 90.0F);
               }
            }
         }

         this.remove();
      }
   }

   @Nullable
   public String getControllerName() {
      if (this.isPlayer()) {
         return this.getPlayer().name;
      } else {
         UnitController var2 = this.controller;
         LogicAI ai;
         if (var2 instanceof LogicAI && (ai = (LogicAI)var2) == (LogicAI)var2 && ai.controller != null) {
            return ai.controller.lastAccessed;
         } else {
            var2 = this.controller;
            FormationAI ai;
            return var2 instanceof FormationAI && (ai = (FormationAI)var2) == (FormationAI)var2 && ai.leader != null && ai.leader.isPlayer() ? ai.leader.getPlayer().name : null;
         }
      }
   }

   public void display(Table table) {
      this.type.display((Unit)this.self(), table);
   }

   public boolean isImmune(StatusEffect effect) {
      return this.type.immunities.contains(effect);
   }

   public void draw() {
      this.type.draw((Unit)this.self());
   }

   public boolean isPlayer() {
      return this.controller instanceof Player;
   }

   @Nullable
   public Player getPlayer() {
      return this.isPlayer() ? (Player)this.controller : null;
   }

   public void killed() {
      this.wasPlayer = this.isLocal();
      this.health = 0.0F;
      this.dead = true;
      if (!this.type.flying) {
         this.destroy();
      }

   }

@braindustry.annotations.ModAnnotations.Replace
   public void kill() {
      if (!this.dead && !Vars.net.client()) {
         Call.unitDeath(this.id);
      }
   }
}