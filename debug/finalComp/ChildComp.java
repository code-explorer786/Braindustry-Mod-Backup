package braindustry.entities.compByAnuke;
import arc.util.Nullable;
import mindustry.gen.Posc;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ChildComp implements Posc{
    @braindustry.annotations.ModAnnotations.Import float x, y;

    @Nullable Posc parent;
    float offsetX, offsetY;
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