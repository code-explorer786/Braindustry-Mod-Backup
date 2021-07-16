package braindustry.entities.compByAnuke;

import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ElevationMoveComp implements Velc, Posc, Flyingc, Hitboxc{
    @braindustry.annotations.ModAnnotations.Import float x, y;

    @braindustry.annotations.ModAnnotations.Replace
    @Override
    public SolidPred solidity(){
        return isFlying() ? null : EntityCollisions::solid;
    }

}
