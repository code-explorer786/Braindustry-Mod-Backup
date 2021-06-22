package mindustry.entities.comp;

import arc.math.Angles;
import mindustry.gen.Unitc;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;

abstract class TrailComp implements Unitc {
   UnitType type;
   float x;
   float y;
   float rotation;
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
