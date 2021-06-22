package braindustry.entities.compByAnuke;
import arc.math.geom.Vec2;
import mindustry.async.PhysicsProcess.PhysicRef;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Velc;

import arc.math.*;
import arc.math.geom.*;
import mindustry.annotations.Annotations.*;
import mindustry.async.PhysicsProcess.*;
import mindustry.gen.*;

/** Affected by physics.
 * Will bounce off of other objects that are at similar elevations.
 * Has mass.*/
@braindustry.annotations.ModAnnotations.Component
abstract class PhysicsComp implements Velc, Hitboxc, Flyingc{
    @braindustry.annotations.ModAnnotations.Import float hitSize;
    @braindustry.annotations.ModAnnotations.Import Vec2 vel;

    transient PhysicRef physref;
   float mass() {
      return this.hitSize * this.hitSize * 3.1415927F;
   }

   void impulse(float x, float y) {
      float mass = this.mass();
      this.vel.add(x / mass, y / mass);
   }

   void impulse(Vec2 v) {
      this.impulse(v.x, v.y);
   }

   void impulseNet(Vec2 v) {
      this.impulse(v.x, v.y);
      if (this.isRemote()) {
         float mass = this.mass();
         this.move(v.x / mass, v.y / mass);
      }

   }
}