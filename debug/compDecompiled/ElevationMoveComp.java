package mindustry.entities.comp;

import mindustry.entities.EntityCollisions;
import mindustry.entities.EntityCollisions.SolidPred;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;
import mindustry.gen.Velc;

abstract class ElevationMoveComp implements Velc, Posc, Flyingc, Hitboxc {
   float x;
   float y;

   public SolidPred solidity() {
      return this.isFlying() ? null : EntityCollisions::solid;
   }
}
