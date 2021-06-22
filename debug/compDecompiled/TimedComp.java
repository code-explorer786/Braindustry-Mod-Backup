package mindustry.entities.comp;

import arc.math.Scaled;
import arc.util.Time;
import mindustry.gen.Entityc;

abstract class TimedComp implements Entityc, Scaled {
   float time;
   float lifetime;

   public void update() {
      this.time = Math.min(this.time + Time.delta, this.lifetime);
      if (this.time >= this.lifetime) {
         this.remove();
      }

   }

   public float fin() {
      return this.time / this.lifetime;
   }
}
