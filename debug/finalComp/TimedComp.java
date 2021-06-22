package braindustry.entities.compByAnuke;
import arc.math.Scaled;
import arc.util.Time;
import mindustry.gen.Entityc;

import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class TimedComp implements Entityc, Scaled{
    float time, lifetime;
@braindustry.annotations.ModAnnotations.MethodPriority(100)
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