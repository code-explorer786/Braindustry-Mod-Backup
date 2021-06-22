package mindustry.entities.comp;

import arc.math.Mathf;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.gen.Flyingc;
import mindustry.gen.Healthc;
import mindustry.gen.Posc;
import mindustry.gen.Velc;

abstract class BoundedComp implements Velc, Posc, Healthc, Flyingc {
   static final float warpDst = 180.0F;
   float x;
   float y;
   Vec2 vel;

   public void update() {
      if (!Vars.net.client() || this.isLocal()) {
         Vec2 var10000;
         if (this.x < 0.0F) {
            var10000 = this.vel;
            var10000.x += -this.x / 180.0F;
         }

         if (this.y < 0.0F) {
            var10000 = this.vel;
            var10000.y += -this.y / 180.0F;
         }

         if (this.x > (float)Vars.world.unitWidth()) {
            var10000 = this.vel;
            var10000.x -= (this.x - (float)Vars.world.unitWidth()) / 180.0F;
         }

         if (this.y > (float)Vars.world.unitHeight()) {
            var10000 = this.vel;
            var10000.y -= (this.y - (float)Vars.world.unitHeight()) / 180.0F;
         }
      }

      if (this.isGrounded()) {
         this.x = Mathf.clamp(this.x, 0.0F, (float)(Vars.world.width() * 8 - 8));
         this.y = Mathf.clamp(this.y, 0.0F, (float)(Vars.world.height() * 8 - 8));
      }

      if (this.x < -500.0F || this.y < -500.0F || this.x >= (float)(Vars.world.width() * 8) + 500.0F || this.y >= (float)(Vars.world.height() * 8) + 500.0F) {
         this.kill();
      }

   }
}
