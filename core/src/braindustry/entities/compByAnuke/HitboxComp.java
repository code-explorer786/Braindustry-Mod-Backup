package braindustry.entities.compByAnuke;
import arc.func.Cons;
import arc.math.Mathf;
import arc.math.geom.Rect;
import arc.math.geom.QuadTree.QuadTreeObject;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class HitboxComp implements Posc, QuadTreeObject{
    @braindustry.annotations.ModAnnotations.Import float x, y;

    transient float lastX, lastY, deltaX, deltaY, hitSize;
   public void update() {
   }

   public void add() {
      this.updateLastPosition();
   }

   public void afterRead() {
      this.updateLastPosition();
   }

   public float hitSize() {
      return this.hitSize;
   }

   void getCollisions(Cons consumer) {
   }

   void updateLastPosition() {
      this.deltaX = this.x - this.lastX;
      this.deltaY = this.y - this.lastY;
      this.lastX = this.x;
      this.lastY = this.y;
   }

   void collision(Hitboxc other, float x, float y) {
   }

   float deltaLen() {
      return Mathf.len(this.deltaX, this.deltaY);
   }

   float deltaAngle() {
      return Mathf.angle(this.deltaX, this.deltaY);
   }

   boolean collides(Hitboxc other) {
      return true;
   }

   public void hitbox(Rect rect) {
      rect.setCentered(this.x, this.y, this.hitSize, this.hitSize);
   }

   public void hitboxTile(Rect rect) {
      float size = Math.min(this.hitSize * 0.66F, 7.9F);
      rect.setCentered(this.x, this.y, size, size);
   }
}