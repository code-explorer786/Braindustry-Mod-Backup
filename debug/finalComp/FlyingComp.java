package braindustry.entities.compByAnuke;
import arc.Events;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.game.EventType.UnitDrownEvent;
import mindustry.gen.Healthc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;
import mindustry.gen.Unit;
import mindustry.gen.Velc;
import mindustry.gen.WaterMovec;
import mindustry.world.blocks.environment.Floor;

import arc.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.game.EventType.*;
import mindustry.gen.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class FlyingComp implements Posc, Velc, Healthc, Hitboxc{
    private static final Vec2 tmp1 = new Vec2(), tmp2 = new Vec2();

    @braindustry.annotations.ModAnnotations.Import float x, y, speedMultiplier;
    @braindustry.annotations.ModAnnotations.Import Vec2 vel;

    @braindustry.annotations.ModAnnotations.SyncLocal float elevation;
    private transient boolean wasFlying;
    transient boolean hovering;
    transient float drownTime;
    transient float splashTimer;
   boolean checkTarget(boolean targetAir, boolean targetGround) {
      return this.isGrounded() && targetGround || this.isFlying() && targetAir;
   }

   boolean isGrounded() {
      return this.elevation < 0.001F;
   }

   boolean isFlying() {
      return this.elevation >= 0.09F;
   }

   boolean canDrown() {
      return this.isGrounded() && !this.hovering;
   }

   void landed() {
   }

   void wobble() {
      this.x += Mathf.sin(Time.time + (float)(this.id() % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
      this.y += Mathf.cos(Time.time + (float)(this.id() % 10 * 12), 25.0F, 0.05F) * Time.delta * this.elevation;
   }

   void moveAt(Vec2 vector, float acceleration) {
      Vec2 t = tmp1.set(vector);
      tmp2.set(t).sub(this.vel).limit(acceleration * vector.len() * Time.delta * this.floorSpeedMultiplier());
      this.vel.add(tmp2);
   }

   float floorSpeedMultiplier() {
      Floor on = !this.isFlying() && !this.hovering ? this.floorOn() : Blocks.air.asFloor();
      return on.speedMultiplier * this.speedMultiplier;
   }

   public void update() {
      Floor floor = this.floorOn();
      if (this.isFlying() != this.wasFlying) {
         if (this.wasFlying && this.tileOn() != null) {
            Fx.unitLand.at(this.x, this.y, this.floorOn().isLiquid ? 1.0F : 0.5F, this.tileOn().floor().mapColor);
         }

         this.wasFlying = this.isFlying();
      }

      if (!this.hovering && this.isGrounded() && (this.splashTimer += Mathf.dst(this.deltaX(), this.deltaY())) >= 7.0F + this.hitSize() / 8.0F) {
         floor.walkEffect.at(this.x, this.y, this.hitSize() / 8.0F, floor.mapColor);
         this.splashTimer = 0.0F;
         if (!(this instanceof WaterMovec)) {
            floor.walkSound.at(this.x, this.y, Mathf.random(floor.walkSoundPitchMin, floor.walkSoundPitchMax), floor.walkSoundVolume);
         }
      }

      if (this.canDrown() && floor.isLiquid && floor.drownTime > 0.0F) {
         this.drownTime += Time.delta / floor.drownTime;
         this.drownTime = Mathf.clamp(this.drownTime);
         if (Mathf.chanceDelta(0.05000000074505806D)) {
            floor.drownUpdateEffect.at(this.x, this.y, 1.0F, floor.mapColor);
         }

         if (this.drownTime >= 0.999F && !Vars.net.client()) {
            this.kill();
            Events.fire(new UnitDrownEvent((Unit)this.self()));
         }
      } else {
         this.drownTime = Mathf.lerpDelta(this.drownTime, 0.0F, 0.03F);
      }

   }
}