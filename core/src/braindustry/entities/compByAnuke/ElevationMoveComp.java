package braindustry.entities.compByAnuke;
import mindustry.entities.EntityCollisions;
import mindustry.entities.EntityCollisions.SolidPred;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;
import mindustry.gen.Velc;

import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ElevationMoveComp implements Velc, Posc, Flyingc, Hitboxc{
    @braindustry.annotations.ModAnnotations.Import float x, y;
@braindustry.annotations.ModAnnotations.Replace
   public SolidPred solidity() {
      return this.isFlying() ? null : EntityCollisions::solid;
   }
}