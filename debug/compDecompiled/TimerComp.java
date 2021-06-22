package mindustry.entities.comp;

import arc.util.Interval;

abstract class TimerComp {
   transient Interval timer = new Interval(6);

   public boolean timer(int index, float time) {
      return Float.isInfinite(time) ? false : this.timer.get(index, time);
   }
}
