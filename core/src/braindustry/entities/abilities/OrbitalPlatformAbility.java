package braindustry.entities.abilities;

import arc.Core;
import arc.func.Prov;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.type.Weapon;

import static braindustry.BDVars.fullName;

public class OrbitalPlatformAbility extends ModAbility {
    public final Weapon[] weapons;
    private final int platformsCount;
    private final float rotateSpeed;
    public TextureRegion region;
    public TextureRegion outlineRegion;
    public float visualElevation = 1;
    public Seq<Vec2> enginePosses = new Seq<>();
    public float engineSize;
    public String spriteName = "orbital-platform";
    private float platformHitsize = 16f;

    public OrbitalPlatformAbility(int platformsCount, float rotateSpeed, Weapon... weapons) {
        this.platformsCount = platformsCount;
        this.rotateSpeed = rotateSpeed;
        this.weapons = new Weapon[platformsCount];
        for (int i = 0; i < this.weapons.length; i++) {
            this.weapons[i]=i<weapons.length?weapons[i]:null;
        }
    }

    public static TextureRegion defRegion() {
        return Core.atlas.find("orbital-platform");
    }

    public OrbitalPlatformAbility platformHitsize(float platformHitsize) {
        this.platformHitsize = platformHitsize;
        return this;
    }

    public OrbitalPlatformAbility spriteName(String spriteName) {
        this.spriteName = spriteName;
        return this;
    }

    public OrbitalPlatformAbility engineSize(float engineSize) {
        this.engineSize = engineSize;
        return this;
    }

    public OrbitalPlatformAbility enginePosses(Seq<Vec2> enginePosses) {
        this.enginePosses = enginePosses;
        return this;
    }

    public OrbitalPlatformAbility enginePosses(Prov<Seq<Vec2>> enginePosses) {
        return enginePosses(enginePosses.get());
    }

    @Override
    public void load() {
//        Log.info("loadCC: @==@==", getClass().getName());
//        if (region == null) region = Core.atlas.find(fullName("orbital-platform"));
//        if (outlineRegion==null)outlineRegion=Core.atlas.find(fullName("orbital-platform-outline"));
        for (Weapon weapon : weapons) {
            if (weapon != null) {
                weapon.load();
            }
        }
    }

    @Override
    public Seq<? extends TextureRegion> outlineRegions() {
        return Seq.with(region());
    }

    public TextureRegion region() {
        if (region == null) region = Core.atlas.find(fullName(spriteName));
        if (!region.found())region=defRegion();
        return region;
    }

    public int platformsCount() {
        return platformsCount;
    }

    public float rotateSpeed() {
        return rotateSpeed;
    }

    @Override
    public Weapon[] weapons() {
        return weapons;
    }

    public TextureRegion outlineRegion() {
        if (outlineRegion == null) outlineRegion = Core.atlas.find(fullName("orbital-platform-outline"));
        return outlineRegion;
    }

    public float platformHitsize() {
        return platformHitsize;
    }
}
