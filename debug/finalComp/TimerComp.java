package braindustry.entities.compByAnuke;
import arc.util.Interval;

import arc.util.*;
import mindustry.annotations.Annotations.*;

@braindustry.annotations.ModAnnotations.Component
abstract class TimerComp{
    transient Interval timer = new Interval(6);
   public boolean timer(int index, float time) {
      return Float.isInfinite(time) ? false : this.timer.get(index, time);
   }
}