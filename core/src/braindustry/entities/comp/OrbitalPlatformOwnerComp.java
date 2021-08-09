package braindustry.entities.comp;

import arc.func.Prov;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Time;
import braindustry.annotations.ModAnnotations;
import braindustry.customArc.math.ModAngles;
import braindustry.entities.abilities.ModAbility;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import braindustry.gen.ModEntityc;
import braindustry.gen.OrbitalPlatform;
import braindustry.type.ModUnitType;
import mindustry.entities.units.WeaponMount;
import mindustry.game.Team;
import mindustry.gen.Entityc;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;

import java.util.Arrays;

import static arc.util.Tmp.v1;

@ModAnnotations.Component
abstract class OrbitalPlatformOwnerComp implements ModEntityc, Unitc, Entityc {
    public final Seq<OrbitalPlatform> orbitalPlatforms = new Seq<>();
    @ModAnnotations.Import
    float x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, ammo, minFormationSpeed, dragMultiplier;
    @ModAnnotations.Import
    Team team;
    @ModAnnotations.Import
    UnitType type;
    @ModAnnotations.Import
    WeaponMount[] mounts;
    private transient Object[] attachAngles;
    private transient OrbitalPlatformAbility platformAbility;

    public static boolean notNullPlatform(OrbitalPlatform platform) {
        return platform != null && platform.mount != null;
    }

    public boolean contains(OrbitalPlatform platform) {
        return orbitalPlatforms.contains(platform);
    }

    @Override
    public void afterRead() {
        if (platformAbility() == null) return;
        orbitalPlatforms.each(OrbitalPlatform::add);
    }

    public OrbitalPlatformAbility platformAbility() {
        if (platformAbility != null) return platformAbility;
        Prov<OrbitalPlatformAbility> prov = () -> {
            if (modUnitType() == null) {
                return null;
            }
            for (ModAbility modAbility : modUnitType().getModAbilities()) {
                if (modAbility instanceof OrbitalPlatformAbility) return (OrbitalPlatformAbility) modAbility;
            }
            return null;
        };
        return platformAbility = prov.get();
    }

    @Override
    public void setType(UnitType type) {
        if (type instanceof ModUnitType) {
            ModUnitType modUnitType = (ModUnitType) type;
            ModAbility modAbility = ((ModUnitType) type).getModAbilities().find(a -> a instanceof OrbitalPlatformAbility);
            if (modAbility != null) {
                platformAbility = (OrbitalPlatformAbility) modAbility;
                orbitalPlatforms.each(OrbitalPlatform::remove);
                orbitalPlatforms.clear();
                for (int i = 0; i < platformAbility.platformsCount(); i++) {
                    OrbitalPlatform orbitalPlatform = OrbitalPlatform.create();
                    orbitalPlatform.seqId(i);
//                    orbitalPlatform.set(x, y);
                    orbitalPlatform.orbitAngle(i / (float) platformAbility.platformsCount() * 360);
                    orbitalPlatform.setupWeapon( platformAbility.weapons().get(i));
                    orbitalPlatform.owner=self();
                    orbitalPlatform.owner=self();
                    orbitalPlatform.add();
                    orbitalPlatforms.add(orbitalPlatform);
                }
            }
        }
    }

    public void rotateTo(OrbitalPlatform platform, float angel) {
        float v = ((platformAbility.rotateSpeed() % 360f) / 180f);
        if (false) {
            platform.orbitAngle = angel;
            return;
        }
        float speed = platformAbility.rotateSpeed();

        float realSpeed = speed * Time.delta;
        float newAngle = ModAngles.moveLerpToward(platform.orbitAngle, angel, realSpeed);
        Vec2 vec2 = platformPosition(newAngle);
        platform.set(vec2);
        platform.orbitAngle=newAngle;
//        platform.orbitAngle = Mathf.mod(newAngle, 360f);
    }

    public void update() {
        if (platformAbility() == null) return;
        if (attachAngles == null || attachAngles.length != orbitalPlatforms.size) {
            attachAngles = new Object[orbitalPlatforms.size];

        }
//        Log.info(toString());
        for (int i = 0; i < orbitalPlatforms.size; i++) {
            OrbitalPlatform prev = orbitalPlatforms.get(i);
            if (!prev.isAdded() || prev.owner==null){
                prev.remove();
                OrbitalPlatform platform = OrbitalPlatform.create();
                orbitalPlatforms.set(i,platform);
                platform.setOwner(self(),platformAbility().weapons.get(i));
                platform.add();
            }
//            Log.info("\t@| @",i, prev);
        }
        final float platformCount = orbitalPlatforms.size;
        float oneAngle = 360f / platformCount;
        Vec2 target = new Vec2(aimX(), aimY());
        float targetAngle = angleTo(target);
        float onePlatformAngle = (float) (Math.acos((hitSize * hitSize * 2 - platformAbility.platformHitsize() * platformAbility.platformHitsize() * 4) / (2 * hitSize * hitSize)) * Mathf.radiansToDegrees);
        attachAngles:
        {
            Arrays.fill(attachAngles, null);
            float offset = onePlatformAngle / 2f;
            if (platformCount % 2 == 0) {
                offset += onePlatformAngle / 2f;
            }
            for (int i = 0; i < platformCount; i++) {
                int i1 = i + 1;
                int mul = i1 % 2 == 0 ? -2 : 2;
                float angle = (i1 / mul) * onePlatformAngle - ((int) platformCount / 2) * onePlatformAngle + offset;
                float checkAngle = angle + targetAngle;
                Vec2 pos = platformPosition(checkAngle);

                OrbitalPlatform min = null;
                float value = Float.MAX_VALUE;
                for (OrbitalPlatform platform : orbitalPlatforms) {
                    float dst = platform.dst(pos);
                    if (dst < value && attachAngles[platform.seqId] == null) {
                        value = dst;
                        min = platform;
                    }
                }
                if (min==null){
                    Log.err("min is null at @ in @",i,toString());
                    continue;
                }
                attachAngles[min.seqId] = Mathf.mod(angle, 360);
            }
        }
        for (OrbitalPlatform platform : orbitalPlatforms) {
            if (!isShooting()) {
                rotateTo(platform, rotation + oneAngle * (platform.seqId));
            } else {
                rotateTo(platform, (float) attachAngles[platform.seqId] + targetAngle);

            }
        }
    }

    public Vec2 platformPosition(float angle) {
        return v1.trns(angle, hitSize, hitSize).add(this).cpy();
    }

    @Override
    public void draw() {
    }

    @Override
    public void remove() {
        orbitalPlatforms.select(p -> notNullPlatform(p)).each(platform -> {
            WeaponMount mount = platform.mount;
            if (mount.bullet != null) {
                mount.bullet.time = mount.bullet.lifetime - 10f;
                mount.bullet = null;
            }

            if (mount.sound != null) {
                mount.sound.stop();
            }
            platform.remove();
        });
        orbitalPlatforms.clear();
    }
}
