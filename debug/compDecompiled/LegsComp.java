package mindustry.entities.comp;

import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.entities.Damage;
import mindustry.entities.Effect;
import mindustry.entities.EntityCollisions;
import mindustry.entities.Leg;
import mindustry.entities.EntityCollisions.SolidPred;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;
import mindustry.gen.Rotc;
import mindustry.gen.Unitc;
import mindustry.graphics.InverseKinematics;
import mindustry.type.UnitType;
import mindustry.world.blocks.environment.Floor;

abstract class LegsComp implements Posc, Rotc, Hitboxc, Flyingc, Unitc {
   float x;
   float y;
   UnitType type;
   transient Leg[] legs = new Leg[0];
   transient float totalLength;
   transient float moveSpace;
   transient float baseRotation;

   public SolidPred solidity() {
      return !this.type.allowLegStep ? EntityCollisions::solid : EntityCollisions::legsSolid;
   }

   public int pathType() {
      return 1;
   }

   public void add() {
      this.resetLegs();
   }

   public void resetLegs() {
      float rot = this.baseRotation;
      int count = this.type.legCount;
      float legLength = this.type.legLength;
      this.legs = new Leg[count];
      float spacing = 360.0F / (float)count;

      for(int i = 0; i < this.legs.length; ++i) {
         Leg l = new Leg();
         l.joint.trns((float)i * spacing + rot, legLength / 2.0F + this.type.legBaseOffset).add(this.x, this.y);
         l.base.trns((float)i * spacing + rot, legLength + this.type.legBaseOffset).add(this.x, this.y);
         this.legs[i] = l;
      }

   }

   public void update() {
      if (Mathf.dst(this.deltaX(), this.deltaY()) > 0.001F) {
         this.baseRotation = Angles.moveToward(this.baseRotation, Mathf.angle(this.deltaX(), this.deltaY()), this.type.rotateSpeed);
      }

      float rot = this.baseRotation;
      float legLength = this.type.legLength;
      if (this.legs.length != this.type.legCount) {
         this.resetLegs();
      }

      float moveSpeed = this.type.legSpeed;
      int div = Math.max(this.legs.length / this.type.legGroupSize, 2);
      this.moveSpace = legLength / 1.6F / ((float)div / 2.0F) * this.type.legMoveSpace;
      this.totalLength += Mathf.dst(this.deltaX(), this.deltaY());
      float trns = this.moveSpace * 0.85F * this.type.legTrns;
      Vec2 moveOffset = Tmp.v4.trns(rot, trns);
      boolean moving = this.moving();

      for(int i = 0; i < this.legs.length; ++i) {
         float dstRot = this.legAngle(rot, i);
         Vec2 baseOffset = Tmp.v5.trns(dstRot, this.type.legBaseOffset).add(this.x, this.y);
         Leg l = this.legs[i];
         l.joint.sub(baseOffset).limit(this.type.maxStretch * legLength / 2.0F).add(baseOffset);
         l.base.sub(baseOffset).limit(this.type.maxStretch * legLength).add(baseOffset);
         float stageF = (this.totalLength + (float)i * this.type.legPairOffset) / this.moveSpace;
         int stage = (int)stageF;
         int group = stage % div;
         boolean move = i % div == group;
         boolean side = i < this.legs.length / 2;
         boolean backLeg = Math.abs((float)i + 0.5F - (float)this.legs.length / 2.0F) <= 0.501F;
         if (backLeg && this.type.flipBackLegs) {
            side = !side;
         }

         l.moving = move;
         l.stage = moving ? stageF % 1.0F : Mathf.lerpDelta(l.stage, 0.0F, 0.1F);
         if (l.group != group) {
            if (!move && i % div == l.group) {
               Floor floor = Vars.world.floorWorld(l.base.x, l.base.y);
               if (floor.isLiquid) {
                  floor.walkEffect.at(l.base.x, l.base.y, this.type.rippleScale, floor.mapColor);
                  floor.walkSound.at(this.x, this.y, 1.0F, floor.walkSoundVolume);
               } else {
                  Fx.unitLandSmall.at(l.base.x, l.base.y, this.type.rippleScale, floor.mapColor);
               }

               if (this.type.landShake > 0.0F) {
                  Effect.shake(this.type.landShake, this.type.landShake, l.base);
               }

               if (this.type.legSplashDamage > 0.0F) {
                  Damage.damage(this.team(), l.base.x, l.base.y, this.type.legSplashRange, this.type.legSplashDamage, false, true);
               }
            }

            l.group = group;
         }

         Vec2 legDest = Tmp.v1.trns(dstRot, legLength * this.type.legLengthScl).add(baseOffset).add(moveOffset);
         Vec2 jointDest = Tmp.v2;
         InverseKinematics.solve(legLength / 2.0F, legLength / 2.0F, Tmp.v6.set(l.base).sub(baseOffset), side, jointDest);
         jointDest.add(baseOffset);
         jointDest.lerp(Tmp.v6.set(baseOffset).lerp(l.base, 0.5F), 1.0F - this.type.kinematicScl);
         if (move) {
            float moveFract = stageF % 1.0F;
            l.base.lerpDelta(legDest, moveFract);
            l.joint.lerpDelta(jointDest, moveFract / 2.0F);
         }

         l.joint.lerpDelta(jointDest, moveSpeed / 4.0F);
      }

   }

   float legAngle(float rotation, int index) {
      return rotation + 360.0F / (float)this.legs.length * (float)index + 360.0F / (float)this.legs.length / 2.0F;
   }
}
