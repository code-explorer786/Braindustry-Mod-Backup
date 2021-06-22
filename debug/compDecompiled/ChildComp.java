package mindustry.entities.comp;

import arc.util.Nullable;
import mindustry.gen.Posc;

abstract class ChildComp implements Posc {
   float x;
   float y;
   @Nullable
   Posc parent;
   float offsetX;
   float offsetY;

   public void add() {
      if (this.parent != null) {
         this.offsetX = this.x - this.parent.getX();
         this.offsetY = this.y - this.parent.getY();
      }

   }

   public void update() {
      if (this.parent != null) {
         this.x = this.parent.getX() + this.offsetX;
         this.y = this.parent.getY() + this.offsetY;
      }

   }
}
