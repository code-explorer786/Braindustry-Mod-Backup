package mindustry.entities.comp;

import arc.math.Mathf;
import arc.util.Time;
import mindustry.gen.Entityc;
import mindustry.gen.Posc;

abstract class HealthComp implements Entityc, Posc {
   static final float hitDuration = 9.0F;
   float health;
   transient float hitTime;
   transient float maxHealth = 1.0F;
   transient boolean dead;

   boolean isValid() {
      return !this.dead && this.isAdded();
   }

   float healthf() {
      return this.health / this.maxHealth;
   }

   public void update() {
      this.hitTime -= Time.delta / 9.0F;
   }

   void killed() {
   }

   void kill() {
      if (!this.dead) {
         this.health = 0.0F;
         this.dead = true;
         this.killed();
         this.remove();
      }
   }

   void heal() {
      this.dead = false;
      this.health = this.maxHealth;
   }

   boolean damaged() {
      return this.health < this.maxHealth - 0.001F;
   }

   void damagePierce(float amount, boolean withEffect) {
      this.damage(amount, withEffect);
   }

   void damagePierce(float amount) {
      this.damagePierce(amount, true);
   }

   void damage(float amount) {
      this.health -= amount;
      this.hitTime = 1.0F;
      if (this.health <= 0.0F && !this.dead) {
         this.kill();
      }

   }

   void damage(float amount, boolean withEffect) {
      float pre = this.hitTime;
      this.damage(amount);
      if (!withEffect) {
         this.hitTime = pre;
      }

   }

   void damageContinuous(float amount) {
      this.damage(amount * Time.delta, this.hitTime <= -1.0F);
   }

   void damageContinuousPierce(float amount) {
      this.damagePierce(amount * Time.delta, this.hitTime <= -11.0F);
   }

   void clampHealth() {
      this.health = Mathf.clamp(this.health, 0.0F, this.maxHealth);
   }

   void heal(float amount) {
      this.health += amount;
      this.clampHealth();
   }

   void healFract(float amount) {
      this.heal(amount * this.maxHealth);
   }
}
