package mindustry.entities.comp;

import arc.Events;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Tmp;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.core.World;
import mindustry.entities.EntityGroup;
import mindustry.game.EventType.PickupEvent;
import mindustry.gen.Building;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;
import mindustry.gen.Rotc;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import mindustry.ui.Cicon;
import mindustry.world.Build;
import mindustry.world.Tile;
import mindustry.world.blocks.payloads.BuildPayload;
import mindustry.world.blocks.payloads.Payload;
import mindustry.world.blocks.payloads.UnitPayload;

abstract class PayloadComp implements Posc, Rotc, Hitboxc, Unitc {
   float x;
   float y;
   float rotation;
   UnitType type;
   Seq payloads = new Seq();

   float payloadUsed() {
      return this.payloads.sumf((p) -> {
         return p.size() * p.size();
      });
   }

   boolean canPickup(Unit unit) {
      return this.payloadUsed() + unit.hitSize * unit.hitSize <= this.type.payloadCapacity + 0.001F && unit.team == this.team() && unit.isAI();
   }

   boolean canPickup(Building build) {
      return this.payloadUsed() + (float)(build.block.size * build.block.size * 8 * 8) <= this.type.payloadCapacity + 0.001F && build.canPickup();
   }

   boolean canPickupPayload(Payload pay) {
      return this.payloadUsed() + pay.size() * pay.size() <= this.type.payloadCapacity + 0.001F;
   }

   boolean hasPayload() {
      return this.payloads.size > 0;
   }

   void addPayload(Payload load) {
      this.payloads.add(load);
   }

   void pickup(Unit unit) {
      unit.remove();
      this.payloads.add(new UnitPayload(unit));
      Fx.unitPickup.at(unit);
      if (Vars.net.client()) {
         Vars.netClient.clearRemovedEntity(unit.id);
      }

      Events.fire(new PickupEvent((Unit)this.self(), unit));
   }

   void pickup(Building tile) {
      tile.pickedUp();
      tile.tile.remove();
      this.payloads.add(new BuildPayload(tile));
      Fx.unitPickup.at(tile);
      Events.fire(new PickupEvent((Unit)this.self(), tile));
   }

   boolean dropLastPayload() {
      if (this.payloads.isEmpty()) {
         return false;
      } else {
         Payload load = (Payload)this.payloads.peek();
         if (this.tryDropPayload(load)) {
            this.payloads.pop();
            return true;
         } else {
            return false;
         }
      }
   }

   boolean tryDropPayload(Payload payload) {
      Tile on = this.tileOn();
      if (Vars.net.client() && payload instanceof UnitPayload) {
         Vars.netClient.clearRemovedEntity(((UnitPayload)payload).unit.id);
      }

      if (on != null && on.build != null && on.build.acceptPayload(on.build, payload)) {
         Fx.unitDrop.at(on.build);
         on.build.handlePayload(on.build, payload);
         return true;
      } else {
         BuildPayload b;
         if (payload instanceof BuildPayload && (b = (BuildPayload)payload) == (BuildPayload)payload) {
            return this.dropBlock(b);
         } else {
            UnitPayload p;
            return payload instanceof UnitPayload && (p = (UnitPayload)payload) == (UnitPayload)payload ? this.dropUnit(p) : false;
         }
      }
   }

   boolean dropUnit(UnitPayload payload) {
      Unit u = payload.unit;
      if (!u.canPass(this.tileX(), this.tileY())) {
         return false;
      } else {
         Fx.unitDrop.at(this);
         if (Vars.net.client()) {
            return true;
         } else {
            u.set(this);
            u.trns(Tmp.v1.rnd(Mathf.random(2.0F)));
            u.rotation(this.rotation);
            u.id = EntityGroup.nextId();
            if (!u.isAdded()) {
               u.team.data().updateCount(u.type, -1);
            }

            u.add();
            return true;
         }
      }
   }

   boolean dropBlock(BuildPayload payload) {
      Building tile = payload.build;
      int tx = World.toTile(this.x - tile.block.offset);
      int ty = World.toTile(this.y - tile.block.offset);
      Tile on = Vars.world.tile(tx, ty);
      if (on != null && Build.validPlace(tile.block, tile.team, tx, ty, tile.rotation, false)) {
         int rot = (int)((this.rotation + 45.0F) / 90.0F) % 4;
         payload.place(on, rot);
         if (this.getControllerName() != null) {
            payload.build.lastAccessed = this.getControllerName();
         }

         Fx.unitDrop.at(tile);
         Fx.placeBlock.at(on.drawx(), on.drawy(), (float)on.block().size);
         return true;
      } else {
         return false;
      }
   }

   void contentInfo(Table table, float itemSize, float width) {
      table.clear();
      table.top().left();
      float pad = 0.0F;
      float items = (float)this.payloads.size;
      if (itemSize * items + pad * items > width) {
         pad = (width - itemSize * items) / items;
      }

      Iterator var6 = this.payloads.iterator();

      while(var6.hasNext()) {
         Payload p = (Payload)var6.next();
         table.image(p.icon(Cicon.small)).size(itemSize).padRight(pad);
      }

   }
}
