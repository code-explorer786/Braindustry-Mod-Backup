package mindustry.entities.comp;

import arc.graphics.Color;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import arc.util.pooling.Pools;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.content.StatusEffects;
import mindustry.ctype.ContentType;
import mindustry.entities.units.StatusEntry;
import mindustry.gen.Flyingc;
import mindustry.gen.Posc;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;
import mindustry.type.UnitType;
import mindustry.world.blocks.environment.Floor;

abstract class StatusComp implements Posc, Flyingc {
   private Seq statuses = new Seq();
   private transient Bits applied;
   transient float speedMultiplier;
   transient float damageMultiplier;
   transient float healthMultiplier;
   transient float reloadMultiplier;
   transient float buildSpeedMultiplier;
   transient float dragMultiplier;
   transient boolean disarmed;
   UnitType type;

   StatusComp() {
      this.applied = new Bits(Vars.content.getBy(ContentType.status).size);
      this.speedMultiplier = 1.0F;
      this.damageMultiplier = 1.0F;
      this.healthMultiplier = 1.0F;
      this.reloadMultiplier = 1.0F;
      this.buildSpeedMultiplier = 1.0F;
      this.dragMultiplier = 1.0F;
      this.disarmed = false;
   }

   void apply(StatusEffect effect) {
      this.apply(effect, 1.0F);
   }

   void apply(StatusEffect effect, float duration) {
      if (effect != StatusEffects.none && effect != null && !this.isImmune(effect)) {
         if (Vars.state.isCampaign()) {
            effect.unlock();
         }

         if (this.statuses.size > 0) {
            for(int i = 0; i < this.statuses.size; ++i) {
               StatusEntry entry = (StatusEntry)this.statuses.get(i);
               if (entry.effect == effect) {
                  entry.time = Math.max(entry.time, duration);
                  return;
               }

               if (entry.effect.reactsWith(effect)) {
                  StatusEntry.tmp.effect = entry.effect;
                  entry.effect.getTransition((Unit)this.self(), effect, entry.time, duration, StatusEntry.tmp);
                  entry.time = StatusEntry.tmp.time;
                  if (StatusEntry.tmp.effect != entry.effect) {
                     entry.effect = StatusEntry.tmp.effect;
                  }

                  return;
               }
            }
         }

         if (!effect.reactive) {
            StatusEntry entry = (StatusEntry)Pools.obtain(StatusEntry.class, StatusEntry::new);
            entry.set(effect, duration);
            this.statuses.add(entry);
         }

      }
   }

   void clearStatuses() {
      this.statuses.clear();
   }

   void unapply(StatusEffect effect) {
      this.statuses.remove((e) -> {
         if (e.effect == effect) {
            Pools.free(e);
            return true;
         } else {
            return false;
         }
      });
   }

   boolean isBoss() {
      return this.hasEffect(StatusEffects.boss);
   }

   abstract boolean isImmune(StatusEffect var1);

   Color statusColor() {
      if (this.statuses.size == 0) {
         return Tmp.c1.set(Color.white);
      } else {
         float r = 1.0F;
         float g = 1.0F;
         float b = 1.0F;
         float total = 0.0F;

         float intensity;
         for(Iterator var5 = this.statuses.iterator(); var5.hasNext(); total += intensity) {
            StatusEntry entry = (StatusEntry)var5.next();
            intensity = entry.time < 10.0F ? entry.time / 10.0F : 1.0F;
            r += entry.effect.color.r * intensity;
            g += entry.effect.color.g * intensity;
            b += entry.effect.color.b * intensity;
         }

         float count = (float)this.statuses.size + total;
         return Tmp.c1.set(r / count, g / count, b / count, 1.0F);
      }
   }

   public void update() {
      Floor floor = this.floorOn();
      if (this.isGrounded() && !this.type.hovering) {
         this.apply(floor.status, floor.statusDuration);
      }

      this.applied.clear();
      this.speedMultiplier = this.damageMultiplier = this.healthMultiplier = this.reloadMultiplier = this.buildSpeedMultiplier = this.dragMultiplier = 1.0F;
      this.disarmed = false;
      if (!this.statuses.isEmpty()) {
         int index = 0;

         while(true) {
            while(index < this.statuses.size) {
               StatusEntry entry = (StatusEntry)this.statuses.get(index++);
               entry.time = Math.max(entry.time - Time.delta, 0.0F);
               if (entry.effect != null && (!(entry.time <= 0.0F) || entry.effect.permanent)) {
                  this.applied.set(entry.effect.id);
                  this.speedMultiplier *= entry.effect.speedMultiplier;
                  this.healthMultiplier *= entry.effect.healthMultiplier;
                  this.damageMultiplier *= entry.effect.damageMultiplier;
                  this.reloadMultiplier *= entry.effect.reloadMultiplier;
                  this.buildSpeedMultiplier *= entry.effect.buildSpeedMultiplier;
                  this.dragMultiplier *= entry.effect.dragMultiplier;
                  this.disarmed |= entry.effect.disarm;
                  entry.effect.update((Unit)this.self(), entry.time);
               } else {
                  Pools.free(entry);
                  --index;
                  this.statuses.remove(index);
               }
            }

            return;
         }
      }
   }

   public void draw() {
      Iterator var1 = this.statuses.iterator();

      while(var1.hasNext()) {
         StatusEntry e = (StatusEntry)var1.next();
         e.effect.draw((Unit)this.self());
      }

   }

   boolean hasEffect(StatusEffect effect) {
      return this.applied.get(effect.id);
   }
}
