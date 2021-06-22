package mindustry.entities.comp;

import arc.math.geom.Vec2;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.entities.EntityCollisions.SolidPred;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;

abstract class VelComp implements Posc {
   float x;
   float y;
   final transient Vec2 vel = new Vec2();
   transient float drag = 0.0F;

   public void update() {
      this.move(this.vel.x * Time.delta, this.vel.y * Time.delta);
      this.vel.scl(Math.max(1.0F - this.drag * Time.delta, 0.0F));
   }

   @Nullable
   SolidPred solidity() {
      return null;
   }

   boolean canPass(int tileX, int tileY) {
      SolidPred s = this.solidity();
      return s == null || !s.solid(tileX, tileY);
   }

   boolean canPassOn() {
      return this.canPass(this.tileX(), this.tileY());
   }

   boolean moving() {
      return !this.vel.isZero(0.01F);
   }

   void move(float cx, float cy) {
      SolidPred check = this.solidity();
      if (check != null) {
         Vars.collisions.move((Hitboxc)this.self(), cx, cy, check);
      } else {
         this.x += cx;
         this.y += cy;
      }

   }
}
