package braindustry.entities.compByAnuke;
import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Effect;
import mindustry.gen.ElevationMovec;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Mechc;
import mindustry.gen.Posc;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import mindustry.world.Tile;

import arc.graphics.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.gen.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class MechComp implements Posc, Flyingc, Hitboxc, Unitc, Mechc, ElevationMovec{
    @braindustry.annotations.ModAnnotations.Import float x, y, hitSize;
    @braindustry.annotations.ModAnnotations.Import UnitType type;

    @braindustry.annotations.ModAnnotations.SyncField(false) @braindustry.annotations.ModAnnotations.SyncLocal float baseRotation;
    transient float walkTime, walkExtension;
    transient private boolean walked;
   public void update() {
      float extend;
      if (this.walked || Vars.net.client()) {
         extend = this.deltaLen();
         this.baseRotation = Angles.moveToward(this.baseRotation, this.deltaAngle(), this.type().baseRotateSpeed * Mathf.clamp(extend / this.type().speed / Time.delta) * Time.delta);
         this.walkTime += extend;
         this.walked = false;
      }

      extend = this.walkExtend(false);
      float base = this.walkExtend(true);
      float extendScl = base % 1.0F;
      float lastExtend = this.walkExtension;
      if (extendScl < lastExtend && base % 2.0F > 1.0F && !this.isFlying()) {
         int side = -Mathf.sign(extend);
         float width = this.hitSize / 2.0F * (float)side;
         float length = this.type.mechStride * 1.35F;
         float cx = this.x + Angles.trnsx(this.baseRotation, length, width);
         float cy = this.y + Angles.trnsy(this.baseRotation, length, width);
         if (this.type.mechStepShake > 0.0F) {
            Effect.shake(this.type.mechStepShake, this.type.mechStepShake, cx, cy);
         }

         if (this.type.mechStepParticles) {
            Tile tile = Vars.world.tileWorld(cx, cy);
            if (tile != null) {
               Color color = tile.floor().mapColor;
               Fx.unitLand.at(cx, cy, this.hitSize / 8.0F, color);
            }
         }
      }

      this.walkExtension = extendScl;
   }

   public float walkExtend(boolean scaled) {
      float raw = this.walkTime % (this.type.mechStride * 4.0F);
      if (scaled) {
         return raw / this.type.mechStride;
      } else {
         if (raw > this.type.mechStride * 3.0F) {
            raw -= this.type.mechStride * 4.0F;
         } else if (raw > this.type.mechStride * 2.0F) {
            raw = this.type.mechStride * 2.0F - raw;
         } else if (raw > this.type.mechStride) {
            raw = this.type.mechStride * 2.0F - raw;
         }

         return raw;
      }
   }

   public void moveAt(Vec2 vector, float acceleration) {
      if (!vector.isZero()) {
         this.walked = true;
      }

   }

   public void approach(Vec2 vector) {
      if (!vector.isZero(0.001F)) {
         this.walked = true;
      }

   }
}