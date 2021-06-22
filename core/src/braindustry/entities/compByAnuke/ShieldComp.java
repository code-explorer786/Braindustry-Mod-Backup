package braindustry.entities.compByAnuke;
import arc.util.Time;
import mindustry.content.Fx;
import mindustry.gen.Healthc;
import mindustry.gen.Posc;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ShieldComp implements Healthc, Posc{
    @braindustry.annotations.ModAnnotations.Import float health, hitTime, x, y, healthMultiplier;
    @braindustry.annotations.ModAnnotations.Import boolean dead;

    /** Absorbs health damage. */
    float shield;
    /** Substracts an amount from damage. */
    float armor;
    /** Shield opacity. */
    transient float shieldAlpha = 0f;
@braindustry.annotations.ModAnnotations.Replace
   public void damage(float amount) {
      amount = Math.max(amount - this.armor, 0.1F * amount);
      amount /= this.healthMultiplier;
      this.rawDamage(amount);
   }

@braindustry.annotations.ModAnnotations.Replace
   public void damagePierce(float amount, boolean withEffect) {
      float pre = this.hitTime;
      this.rawDamage(amount);
      if (!withEffect) {
         this.hitTime = pre;
      }

   }

   private void rawDamage(float amount) {
      boolean hadShields = this.shield > 1.0E-4F;
      if (hadShields) {
         this.shieldAlpha = 1.0F;
      }

      float shieldDamage = Math.min(Math.max(this.shield, 0.0F), amount);
      this.shield -= shieldDamage;
      this.hitTime = 1.0F;
      amount -= shieldDamage;
      if (amount > 0.0F) {
         this.health -= amount;
         if (this.health <= 0.0F && !this.dead) {
            this.kill();
         }

         if (hadShields && this.shield <= 1.0E-4F) {
            Fx.unitShieldBreak.at(this.x, this.y, 0.0F, this);
         }
      }

   }

   public void update() {
      this.shieldAlpha -= Time.delta / 15.0F;
      if (this.shieldAlpha < 0.0F) {
         this.shieldAlpha = 0.0F;
      }

   }
}