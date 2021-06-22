package braindustry.entities.compByAnuke;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.audio.SoundLoop;
import mindustry.entities.Effect;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.Bullet;
import mindustry.gen.Posc;
import mindustry.gen.Rotc;
import mindustry.gen.Sounds;
import mindustry.gen.Statusc;
import mindustry.gen.Teamc;
import mindustry.gen.Velc;
import mindustry.type.UnitType;
import mindustry.type.Weapon;

import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.audio.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.type.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class WeaponsComp implements Teamc, Posc, Rotc, Velc, Statusc{
    @braindustry.annotations.ModAnnotations.Import float x, y, rotation, reloadMultiplier;
    @braindustry.annotations.ModAnnotations.Import boolean disarmed;
    @braindustry.annotations.ModAnnotations.Import Vec2 vel;
    @braindustry.annotations.ModAnnotations.Import UnitType type;

    /** temporary weapon sequence number */
    static int sequenceNum = 0;

    /** weapon mount array, never null */
    @braindustry.annotations.ModAnnotations.SyncLocal WeaponMount[] mounts = {};
    @braindustry.annotations.ModAnnotations.ReadOnly transient boolean isRotate;
    transient float aimX, aimY;
    boolean isShooting;
    float ammo;
   float ammof() {
      return this.ammo / (float)this.type.ammoCapacity;
   }

   void setWeaponRotation(float rotation) {
      WeaponMount[] var2 = this.mounts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WeaponMount mount = var2[var4];
         mount.rotation = rotation;
      }

   }

   void setupWeapons(UnitType def) {
      this.mounts = new WeaponMount[def.weapons.size];

      for(int i = 0; i < this.mounts.length; ++i) {
         this.mounts[i] = new WeaponMount((Weapon)def.weapons.get(i));
      }

   }

   void controlWeapons(boolean rotateShoot) {
      this.controlWeapons(rotateShoot, rotateShoot);
   }

   void controlWeapons(boolean rotate, boolean shoot) {
      WeaponMount[] var3 = this.mounts;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WeaponMount mount = var3[var5];
         mount.rotate = rotate;
         mount.shoot = shoot;
      }

      this.isRotate = rotate;
      this.isShooting = shoot;
   }

   void aim(Position pos) {
      this.aim(pos.getX(), pos.getY());
   }

   void aim(float x, float y) {
      Tmp.v1.set(x, y).sub(this.x, this.y);
      if (Tmp.v1.len() < this.type.aimDst) {
         Tmp.v1.setLength(this.type.aimDst);
      }

      x = Tmp.v1.x + this.x;
      y = Tmp.v1.y + this.y;
      WeaponMount[] var3 = this.mounts;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WeaponMount mount = var3[var5];
         mount.aimX = x;
         mount.aimY = y;
      }

      this.aimX = x;
      this.aimY = y;
   }

   boolean canShoot() {
      return !this.disarmed;
   }

   public void remove() {
      WeaponMount[] var1 = this.mounts;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         WeaponMount mount = var1[var3];
         if (mount.bullet != null) {
            mount.bullet.time = mount.bullet.lifetime - 10.0F;
            mount.bullet = null;
         }

         if (mount.sound != null) {
            mount.sound.stop();
         }
      }

   }

   public void update() {
      boolean can = this.canShoot();
      WeaponMount[] var2 = this.mounts;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WeaponMount mount = var2[var4];
         Weapon weapon = mount.weapon;
         mount.reload = Math.max(mount.reload - Time.delta * this.reloadMultiplier, 0.0F);
         float weaponRotation = this.rotation - 90.0F + (weapon.rotate ? mount.rotation : 0.0F);
         float mountX = this.x + Angles.trnsx(this.rotation - 90.0F, weapon.x, weapon.y);
         float mountY = this.y + Angles.trnsy(this.rotation - 90.0F, weapon.x, weapon.y);
         float shootX = mountX + Angles.trnsx(weaponRotation, weapon.shootX, weapon.shootY);
         float shootY = mountY + Angles.trnsy(weaponRotation, weapon.shootX, weapon.shootY);
         float shootAngle = weapon.rotate ? weaponRotation + 90.0F : Angles.angle(shootX, shootY, mount.aimX, mount.aimY) + (this.rotation - this.angleTo(mount.aimX, mount.aimY));
         if (weapon.continuous && mount.bullet != null) {
            if (mount.bullet.isAdded() && !(mount.bullet.time >= mount.bullet.lifetime) && mount.bullet.type == weapon.bullet) {
               mount.bullet.rotation(weaponRotation + 90.0F);
               mount.bullet.set(shootX, shootY);
               mount.reload = weapon.reload;
               this.vel.add(Tmp.v1.trns(this.rotation + 180.0F, mount.bullet.type.recoil));
               if (weapon.shootSound != Sounds.none && !Vars.headless) {
                  if (mount.sound == null) {
                     mount.sound = new SoundLoop(weapon.shootSound, 1.0F);
                  }

                  mount.sound.update(this.x, this.y, true);
               }
            } else {
               mount.bullet = null;
            }
         } else {
            mount.heat = Math.max(mount.heat - Time.delta * this.reloadMultiplier / mount.weapon.cooldownTime, 0.0F);
            if (mount.sound != null) {
               mount.sound.update(this.x, this.y, false);
            }
         }

         if (weapon.otherSide != -1 && weapon.alternate && mount.side == weapon.flipSprite && mount.reload + Time.delta * this.reloadMultiplier > weapon.reload / 2.0F && mount.reload <= weapon.reload / 2.0F) {
            this.mounts[weapon.otherSide].side = !this.mounts[weapon.otherSide].side;
            mount.side = !mount.side;
         }

         if (weapon.rotate && (mount.rotate || mount.shoot) && can) {
            float axisX = this.x + Angles.trnsx(this.rotation - 90.0F, weapon.x, weapon.y);
            float axisY = this.y + Angles.trnsy(this.rotation - 90.0F, weapon.x, weapon.y);
            mount.targetRotation = Angles.angle(axisX, axisY, mount.aimX, mount.aimY) - this.rotation;
            mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, weapon.rotateSpeed * Time.delta);
         } else if (!weapon.rotate) {
            mount.rotation = 0.0F;
            mount.targetRotation = this.angleTo(mount.aimX, mount.aimY);
         }

         if (mount.shoot && can && (this.ammo > 0.0F || !Vars.state.rules.unitAmmo || this.team().rules().infiniteAmmo) && (!weapon.alternate || mount.side == weapon.flipSprite) && (this.vel.len() >= mount.weapon.minShootVelocity || Vars.net.active() && !this.isLocal()) && mount.reload <= 1.0E-4F && Angles.within(weapon.rotate ? mount.rotation : this.rotation, mount.targetRotation, mount.weapon.shootCone)) {
            this.shoot(mount, shootX, shootY, mount.aimX, mount.aimY, mountX, mountY, shootAngle, Mathf.sign(weapon.x));
            mount.reload = weapon.reload;
            --this.ammo;
            if (this.ammo < 0.0F) {
               this.ammo = 0.0F;
            }
         }
      }

   }

   private void shoot(WeaponMount mount, float x, float y, float aimX, float aimY, float mountX, float mountY, float rotation, int side) {
      Weapon weapon = mount.weapon;
      float baseX = this.x;
      float baseY = this.y;
      boolean delay = weapon.firstShotDelay + weapon.shotDelay > 0.0F;
      (delay ? weapon.chargeSound : (weapon.continuous ? Sounds.none : weapon.shootSound)).at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
      BulletType ammo = weapon.bullet;
      float lifeScl = ammo.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y, aimX, aimY) / ammo.range()) : 1.0F;
      sequenceNum = 0;
      if (delay) {
         Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> {
            Time.run((float)sequenceNum * weapon.shotDelay + weapon.firstShotDelay, () -> {
               if (this.isAdded()) {
                  mount.bullet = this.bullet(weapon, x + this.x - baseX, y + this.y - baseY, f + Mathf.range(weapon.inaccuracy), lifeScl);
               }
            });
            ++sequenceNum;
         });
      } else {
         Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> {
            mount.bullet = this.bullet(weapon, x, y, f + Mathf.range(weapon.inaccuracy), lifeScl);
         });
      }

      boolean parentize = ammo.keepVelocity;
      if (delay) {
         Time.run(weapon.firstShotDelay, () -> {
            if (this.isAdded()) {
               this.vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
               Effect.shake(weapon.shake, weapon.shake, x, y);
               mount.heat = 1.0F;
               if (!weapon.continuous) {
                  weapon.shootSound.at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
               }

            }
         });
      } else {
         this.vel.add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
         Effect.shake(weapon.shake, weapon.shake, x, y);
         mount.heat = 1.0F;
      }

      weapon.ejectEffect.at(mountX, mountY, rotation * (float)side);
      ammo.shootEffect.at(x, y, rotation, parentize ? this : null);
      ammo.smokeEffect.at(x, y, rotation, parentize ? this : null);
      this.apply(weapon.shootStatus, weapon.shootStatusDuration);
   }

   private Bullet bullet(Weapon weapon, float x, float y, float angle, float lifescl) {
      float xr = Mathf.range(weapon.xRand);
      return weapon.bullet.create(this, this.team(), x + Angles.trnsx(angle, 0.0F, xr), y + Angles.trnsy(angle, 0.0F, xr), angle, 1.0F - weapon.velocityRnd + Mathf.random(weapon.velocityRnd), lifescl);
   }
}