package braindustry.entities.compByAnuke;
import arc.math.Angles;
import mindustry.gen.Unitc;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;

import arc.math.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

@braindustry.annotations.ModAnnotations.Component
abstract class TrailComp implements Unitc{
    @braindustry.annotations.ModAnnotations.Import UnitType type;
    @braindustry.annotations.ModAnnotations.Import float x, y, rotation;

    transient Trail trail = new Trail(6);
   public void update() {
      this.trail.length = this.type.trailLength;
      float scale = this.elevation();
      float offset = this.type.engineOffset / 2.0F + this.type.engineOffset / 2.0F * scale;
      float cx = this.x + Angles.trnsx(this.rotation + 180.0F, offset);
      float cy = this.y + Angles.trnsy(this.rotation + 180.0F, offset);
      this.trail.update(cx, cy);
   }
}