package braindustry.entities.comp;

import arc.graphics.Blending;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.annotations.ModAnnotations;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import braindustry.gen.OrbitalPlatformOwnerc;
import braindustry.gen.OrbitalPlatformc;
import mindustry.audio.SoundLoop;
import mindustry.content.Bullets;
import mindustry.entities.Effect;
import mindustry.entities.EntityCollisions;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.units.WeaponMount;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Weapon;

import static mindustry.Vars.*;

@ModAnnotations.EntityDef(value = {OrbitalPlatformc.class}, serialize = false)
@ModAnnotations.Component(base = true)
abstract class OrbitalPlatformComp implements Entityc, Drawc, Posc, Hitboxc, Rotc {
    public static final float shadowTX = -5, shadowTY = -6, outlineSpace = 0.01f;
    public static int sequenceNum = 0;
    protected final float z = 85f;
    public float orbitAngle = 0f;
    public int seqId;
    public WeaponMount mount = null;
    public transient OrbitalPlatformOwnerc owner;
    @ModAnnotations.Import
    float x, y, rotation, maxHealth, hitSize, health;

    public void setOwner(Unit unitOwner, Weapon weapon) {
        if (!(unitOwner instanceof OrbitalPlatformOwnerc)) throw new IllegalArgumentException("unitOwner cannot be non-instance of OrbitalPlatformOwnerc");
        OrbitalPlatformOwnerc owner=unitOwner.as();
        this.owner = owner;
        set(owner.platformPosition(orbitAngle));
        setupWeapon(weapon);
    }public void setupWeapon(Weapon weapon) {
        if (weapon == null) return;
        mount = new WeaponMount(weapon);
    }

    public void move(float cx, float cy) {
        if (owner!=null) {
            owner.platformPosition(orbitAngle+cx);
            Tmp.v1.trns(orbitAngle+cx,0,owner.hitSize()).sub(Tmp.v2.trns(orbitAngle,owner.hitSize(),owner.hitSize()));
            cx=Tmp.v1.x;
            cy=Tmp.v1.y;
        }
        x += cx;
        y += cy;
    }

    @Override
    public void update() {
        if (owner == null || !owner.contains(self())) {
            remove();
            return;
        }
        updateWeapon();
    }

    private void updateWeapon() {
        if (mount == null) return;
//        mount.rotate;
        mount.rotate = false;
        for (WeaponMount m : owner.mounts()) {
            mount.rotate |= m.rotate;
        }
        mount.aimX = owner.aimX();
        mount.aimY = owner.aimY();
        mount.rotate = owner.isRotate();
        mount.shoot = owner.isShooting();
        boolean can = owner.canShoot();
        Weapon weapon = mount.weapon;
        mount.reload = Math.max(mount.reload - Time.delta * owner.reloadMultiplier(), 0);
        float weaponRotation = rotation - 90 + (weapon.rotate ? mount.rotation : 0);
//            float mountX = unit.x + Angles.trnsx(unit.rotation - 90, weapon.x, weapon.y);
//            float mountY = unit.y + Angles.trnsy(unit.rotation - 90, weapon.x, weapon.y);
        float mountX = x, mountY = y;
        Tmp.v1.trns(weaponRotation, weapon.shootX, weapon.shootY);
        float shootX = mountX + Tmp.v1.x;
        float shootY = mountY + Tmp.v1.y;
        float shootAngle = weapon.rotate ? weaponRotation + 90 : Angles.angle(shootX, shootY, mount.aimX, mount.aimY) + (rotation - angleTo(mount.aimX, mount.aimY));
        if (weapon.continuous && mount.bullet != null) {
            if (!mount.bullet.isAdded() || mount.bullet.time >= mount.bullet.lifetime || mount.bullet.type != weapon.bullet) {
                mount.bullet = null;
            } else {
                mount.bullet.rotation(weaponRotation + 90);
                mount.bullet.set(shootX, shootY);
                mount.reload = weapon.reload;
                owner.vel().add(Tmp.v1.trns(owner.rotation() + 180.0F, mount.bullet.type.recoil));
                if (weapon.shootSound != Sounds.none && !headless) {
                    if (mount.sound == null) mount.sound = new SoundLoop(weapon.shootSound, 1.0F);
                    mount.sound.update(owner.x(), owner.y(), true);
                }
            }
        } else {
            mount.heat = Math.max(mount.heat - Time.delta * owner.reloadMultiplier() / mount.weapon.cooldownTime, 0);
            if (mount.sound != null) {
                mount.sound.update(owner.x(), owner.y(), false);
            }
        }
        if (weapon.rotate && (mount.rotate || mount.shoot) && can) {
            mount.targetRotation = angleTo(mount.aimX, mount.aimY);// - unit.rotation;
            mount.rotation = Angles.moveToward(mount.rotation, mount.targetRotation, weapon.rotateSpeed * Time.delta);
        } else if (!weapon.rotate) {
            mount.rotation = 0;
            mount.targetRotation = angleTo(mount.aimX, mount.aimY);
        }
        if (mount.shoot && can && (owner.ammo() > 0 || !state.rules.unitAmmo || owner.team().rules().infiniteAmmo) && (owner.vel().len() >= mount.weapon.minShootVelocity || (net.active() && !owner.isLocal())) && mount.reload <= 1.0E-4F && Angles.within(weapon.rotate ? mount.rotation : owner.rotation(), mount.targetRotation, mount.weapon.shootCone)) {
            shoot(mount, shootX, shootY, mount.aimX, mount.aimY, mountX, mountY, shootAngle, Mathf.sign(weapon.x));
            mount.reload = weapon.reload;
            owner.ammo(Math.max(0, owner.ammo() - 1));
        }

    }

    @Override
    public void draw() {
        if (owner == null) return;
        OrbitalPlatformAbility ability = owner.platformAbility();
        if (ability == null) return;
        float unitZ = owner.elevation() > 0.5f ? (owner.type().lowAltitude ? Layer.flyingUnitLow : Layer.flyingUnit) : owner.type().groundLayer + Mathf.clamp(owner.type().hitSize / 4000f, 0, 0.01f);

        Draw.z(z);
        drawEngines();
        Draw.color();
        Draw.rect(ability.outlineRegion(), x, y, rotation - 90);
        Draw.rect(ability.region(), x, y, rotation - 90);
        drawWeapon();
        Draw.z(Math.min(Layer.darkness, z - 1f));

        drawShadow();
        Draw.reset();
    }

    public void drawWeapon() {
        if (mount == null) return;
        Weapon weapon = mount.weapon;
        float rotation = this.rotation - 90f;
        float weaponRotation = rotation + (weapon.rotate ? mount.rotation : 0);
        float recoil = -((mount.reload) / weapon.reload * weapon.recoil);
        float wx = x + Angles.trnsx(rotation, weapon.x, weapon.y) + Angles.trnsx(weaponRotation, 0, recoil),
                wy = y + Angles.trnsy(rotation, weapon.x, weapon.y) + Angles.trnsy(weaponRotation, 0, recoil);

        if (weapon.shadow > 0) {
            Drawf.shadow(wx, wy, weapon.shadow);
        }

        if (weapon.outlineRegion.found()) {
            float z = Draw.z();
            if (!weapon.top) Draw.z(z - outlineSpace);

            Draw.rect(weapon.outlineRegion,
                    wx, wy,
                    weapon.outlineRegion.width * Draw.scl * -Mathf.sign(weapon.flipSprite),
                    weapon.region.height * Draw.scl,
                    weaponRotation);

            Draw.z(z);
        }

        Draw.rect(weapon.region,
                wx, wy,
                weapon.region.width * Draw.scl * -Mathf.sign(weapon.flipSprite),
                weapon.region.height * Draw.scl,
                weaponRotation);

        if (weapon.heatRegion.found() && mount.heat > 0) {
            Draw.color(weapon.heatColor, mount.heat);
            Draw.blend(Blending.additive);
            Draw.rect(weapon.heatRegion,
                    wx, wy,
                    weapon.heatRegion.width * Draw.scl * -Mathf.sign(weapon.flipSprite),
                    weapon.heatRegion.height * Draw.scl,
                    weaponRotation);
            Draw.blend();
            Draw.color();
        }


        Draw.reset();
    }

    public void drawShadow() {
        Draw.color(Pal.shadow);
        float e = owner.platformAbility().visualElevation;
        Draw.rect(owner.platformAbility().region(), x + shadowTX * e, y + shadowTY * e, rotation - 90);
        Draw.color();
    }

    public void drawEngines() {
        OrbitalPlatformAbility ability = owner.platformAbility();
        float scale = 0.5f;
//        float offset = ability.engineOffset/2f + ability.engineOffset/2f*scale;

        float engineSize = ability.engineSize;
        Seq<Vec2> enginePosses = ability.enginePosses;
        enginePosses.each(enginePos -> {

            int index = enginePosses.indexOf(enginePos);
            float rotation = this.rotation/*+ 360f/ enginePosses.size*index*/;

            Draw.color(owner.team().color);
            Vec2 engineOffset = enginePos.cpy().rotate(rotation);
            Fill.circle(
                    x() + engineOffset.x,
                    y() + engineOffset.y,
                    (engineSize + Mathf.absin(Time.time, 2f, engineSize / 4f)) * scale
            );
            Draw.color(Color.white);
            Vec2 one = new Vec2(Angles.trnsx(enginePos.angle(), 1f), Angles.trnsy(enginePos.angle(), 1f));
            engineOffset.sub(one);
            Fill.circle(
                    x() + engineOffset.x,
                    y() + engineOffset.y,
                    (engineSize + Mathf.absin(Time.time, 2f, engineSize / 4f)) / 2f * scale
            );
            Draw.color();
        });
    }

    public void shoot(WeaponMount mount, float x, float y, float aimX, float aimY, float mountX,
                      float mountY, float rotation, int side) {
        if (owner == null) return;
        Weapon weapon = mount.weapon;
        float baseX = this.getX();
        float baseY = this.getY();
        boolean delay = weapon.firstShotDelay + weapon.shotDelay > 0.0F;
        (delay ? weapon.chargeSound : weapon.continuous ? Sounds.none : weapon.shootSound).at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
        BulletType ammo = weapon.bullet;
        float lifeScl = ammo.scaleVelocity ? Mathf.clamp(Mathf.dst(x, y, aimX, aimY) / ammo.range()) : 1.0F;
        sequenceNum = 0;
        if (delay) {
            Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> {
                Time.run(sequenceNum * weapon.shotDelay + weapon.firstShotDelay, () -> {
                    if (owner == null || !owner.isValid()) return;
                    mount.bullet = bullet(weapon, x + this.getX() - baseX, y + this.getY() - baseY, f + Mathf.range(weapon.inaccuracy), lifeScl);
                });
                sequenceNum++;
            });
        } else {
            Angles.shotgun(weapon.shots, weapon.spacing, rotation, (f) -> mount.bullet = bullet(weapon, x, y, f + Mathf.range(weapon.inaccuracy), lifeScl));
        }
        boolean parentize = ammo.keepVelocity;
        if (delay) {
            Time.run(weapon.firstShotDelay, () -> {
                if (owner == null || !owner.isValid()) return;
                owner.vel().add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
                Effect.shake(weapon.shake, weapon.shake, x, y);
                mount.heat = 1.0F;
                if (!weapon.continuous) {
                    weapon.shootSound.at(x, y, Mathf.random(weapon.soundPitchMin, weapon.soundPitchMax));
                }
            });
        } else {
            owner.vel().add(Tmp.v1.trns(rotation + 180.0F, ammo.recoil));
            Effect.shake(weapon.shake, weapon.shake, x, y);
            mount.heat = 1.0F;
        }
        weapon.ejectEffect.at(mountX, mountY, rotation * side);
        ammo.shootEffect.at(x, y, rotation, parentize ? this : null);
        ammo.smokeEffect.at(x, y, rotation, parentize ? this : null);
        owner.apply(weapon.shootStatus, weapon.shootStatusDuration);
    }

    private Bullet bullet(Weapon weapon, float x, float y, float angle, float lifescl) {
        if (owner == null || !owner.isValid()) return Bullets.standardCopper.create(null, -10, -10, 0);
        float xr = Mathf.range(weapon.xRand);
        return weapon.bullet.create(owner, owner.team(), x + Angles.trnsx(angle, 0, xr), y + Angles.trnsy(angle, 0, xr), angle, (1.0F - weapon.velocityRnd) + Mathf.random(weapon.velocityRnd), lifescl);
    }
}
