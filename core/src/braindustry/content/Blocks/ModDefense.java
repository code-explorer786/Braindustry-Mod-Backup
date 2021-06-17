package braindustry.content.Blocks;

import arc.graphics.Color;
import braindustry.content.*;
import braindustry.entities.bullets.ContinuousRainbowLaserBulletType;
import braindustry.entities.bullets.SpikeCircleOrbonBullet;
import braindustry.gen.ModSounds;
import braindustry.graphics.ModPal;
import braindustry.world.blocks.Wall.ReflectionWall;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.StatusEffects;
import mindustry.ctype.ContentList;
import mindustry.entities.bullet.*;
import mindustry.gen.Bullet;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.*;
import mindustry.world.consumers.ConsumeLiquidFilter;

import static braindustry.content.Blocks.ModBlocks.*;
import static mindustry.content.Bullets.*;

class ModDefense implements ContentList {

    @Override
    public void load() {
        rapier = new ItemTurret("rapier") {{
            localizedName = "Rapier";
            description = "Shots hedgehod-like frag bullets.";
            range = 200;
            reloadTime = 80;
            size = 4;
            shots = 1;
            health = 2800;
            inaccuracy = 0;
            shootSound = Sounds.missile;
            rotateSpeed = 0.9f;
            targetAir = true;
            targetGround = true;
            ammo(
                    ModItems.graphenite, new SpikeCircleOrbonBullet() {
                        {
                            hitEffect = ModFx.circleSpikeHit;
                            ammoMultiplier = 4.0f;
                            despawnEffect = ModFx.circleSpikeHit;
                            smokeEffect = ModFx.spikeSmoke;
                            damage = 800;
                            despawnShake = 2.3f;
                            hitShake = 4.8f;
                            speed = 2.8f;
                            absorbable = false;
                            reflectable = false;
                            hittable = false;
                            hitSize = 6.0f;
                            status = ModStatusEffects.speedMul.get(5);
                            statusDuration = 70.0f * 3.0f;
                        }
                    }
            );
            consumes.liquid(Liquids.cryofluid, 0.2f).optional(false, true);
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 400, Items.plastanium, 350, Items.silicon, 800, Items.titanium, 420, Items.metaglass, 280));
        }};
        axon = new ItemTurret("axon") {{
            health = 2890;
            size = 3;
            rotateSpeed = 0.9f;
            shots = 6;
            reloadTime = 40;
            hasItems = true;
            hasLiquids = true;
            range = 180;
            localizedName = "Axon";
            description = "Powerful Electric shotgun.";
            ammo(
                    ModItems.exoticAlloy, new BasicBulletType() {
                        public void update(Bullet b) {
                            SubBullets.addLightning(b, this);
                            super.update(b);
                        }
                        {
                            backColor = Color.valueOf("c2cc37");
                            width = 14;
                            height = 14;
                            shrinkY = 0.1f;
                            shrinkX = 0.2f;
                            spin = 3.5f;
                            speed = 4.1f;
                            damage = 15;
                            shootEffect = Fx.railShoot;
                            hitColor = frontColor = Color.valueOf("f1fc58");
                            despawnEffect = Fx.railHit;
                            lifetime = 90;
                            knockback = 1;
                            lightning = 3;
                            lightningLength = 5;
                            lightningLengthRand = 15;
                            lightningDamage = 9;
                            lightningAngle = 5;
                            lightningCone = 45;
                            lightningColor = Color.valueOf("f1fc58");
                        }
                    }
            );
            requirements(Category.turret, ItemStack.with(ModItems.odinum, 125, ModItems.graphenite, 200, Items.silicon, 240, Items.metaglass, 210, ModItems.exoticAlloy, 140));
        }};
        blaze = new ItemTurret("blaze") {{
            localizedName = "Blaze";
            description = "Fires four powerful beams at enemies. Every beam has 1/4 of the damage. Requires high-tech ammo to fire.";
            reloadTime = 90;
            shootShake = 5;
            range = 130;
            recoilAmount = 6;
            spread = 20;
            shootCone = 35;
            size = 4;
            health = 2600;
            shots = 4;
            rotate = true;
            shootSound = Sounds.laser;
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 300, Items.silicon, 340, Items.plastanium, 250, Items.graphite, 240));
            ammo(
                    ModItems.graphenite, new ShrapnelBulletType() {
                        {
                            length = 200;
                            damage = 180;
                            width = 20;
                            serrationLenScl = 3;
                            serrationSpaceOffset = 40;
                            serrationFadeOffset = 0;
                            serrations = 12;
                            serrationWidth = 10;
                            ammoMultiplier = 6;
                            lifetime = 40;
                            shootEffect = Fx.thoriumShoot;
                            smokeEffect = Fx.sparkShoot;
                        }
                    },
                    Items.plastanium, new ShrapnelBulletType() {
                        {
                            length = 160;
                            damage = 280;
                            width = 42;
                            serrationLenScl = 6;
                            serrationSpaceOffset = 40;
                            serrationFadeOffset = 0;
                            serrations = 8;
                            serrationWidth = 5;
                            ammoMultiplier = 7;
                            toColor = Color.valueOf("c2f9ff");
                            fromColor = Color.valueOf("9ee1e8");
                            lifetime = 60;
                            shootEffect = Fx.thoriumShoot;
                            smokeEffect = Fx.sparkShoot;
                        }
                    }
            );
            consumes.liquid(ModLiquids.liquidMethane, 0.04f).optional(true, true);
        }};
        brain = new ItemTurret("brain") {{
            localizedName = "Brain";
            description = "Fires a beam of death at enemies. Requires Phase Alloy to concentrate energy. Total destruction.";
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 470, Items.silicon, 500, Items.surgeAlloy, 220, Items.thorium, 420, ModItems.phaseAlloy, 230, ModItems.plastic, 250));
            ammo(
                    ModItems.phaseAlloy, new LaserBulletType() {
                        {
                            length = 240;
                            damage = 300;
                            width = 60;
                            lifetime = 30;
                            lightningSpacing = 34;
                            lightningLength = 6;
                            lightningDelay = 1;
                            lightningLengthRand = 21;
                            lightningDamage = 45;
                            lightningAngleRand = 30;
                            largeHit = true;
                            shootEffect = ModFx.purpleBomb;
                            collidesTeam = true;
                            sideAngle = 15;
                            sideWidth = 0;
                            sideLength = 0;
                            lightningColor = Color.valueOf("d5b2ed");
                            colors = new Color[]{Color.valueOf("d5b2ed"), Color.valueOf("9671b1"), Color.valueOf("a17dcd")};
                        }
                    },
                    ModItems.plastic, new LaserBulletType() {
                        {
                            length = 240;
                            damage = 180;
                            width = 60;
                            lifetime = 30;
                            largeHit = true;
                            shootEffect = ModFx.purpleLaserChargeSmall;
                            collidesTeam = true;
                            sideAngle = 15;
                            sideWidth = 0;
                            sideLength = 0;
                            lightningColor = Color.valueOf("d5b2ed");
                            colors = new Color[]{Color.valueOf("d5b2ed"), Color.valueOf("9671b1"), Color.valueOf("a17dcd")};
                        }
                    }
            );
            reloadTime = 120;
            shots = 3;
            burstSpacing = 3;
            inaccuracy = 7;
            range = 240;
            size = 4;
            health = 2500;
        }};
        mind = new ItemTurret("mind") {{
            localizedName = "Mind";
            description = "An alternative to Brain. Fires a splash-damage beam. Requires lightweight Plastic to shoot.";
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 340, Items.silicon, 500, Items.surgeAlloy, 200, ModItems.odinum, 115, ModItems.phaseAlloy, 250, ModItems.plastic, 250));
            ammo(
                    ModItems.plastic, new ShrapnelBulletType() {
                        {
                            length = 220;
                            damage = 650;
                            width = 35;
                            serrationLenScl = 7;
                            serrationSpaceOffset = 60;
                            serrationFadeOffset = 0;
                            serrations = 11;
                            serrationWidth = 6;
                            fromColor = Color.valueOf("d5b2ed");
                            toColor = Color.valueOf("a17dcd");
                            lifetime = 45;
                            shootEffect = Fx.sparkShoot;
                            smokeEffect = Fx.sparkShoot;
                        }
                    },
                    ModItems.odinum, new LaserBulletType() {
                        {
                            length = 260;
                            damage = 420;
                            width = 40;
                            lifetime = 30;
                            lightningSpacing = 34;
                            lightningLength = 2;
                            lightningDelay = 1;
                            lightningLengthRand = 7;
                            lightningDamage = 45;
                            lightningAngleRand = 30;
                            largeHit = true;
                            shootEffect = Fx.greenLaserChargeSmall;
                            collidesTeam = true;
                            sideAngle = 15;
                            sideWidth = 0;
                            sideLength = 0;
                            colors = new Color[]{Color.valueOf("ffffff"), Color.valueOf("EDEDED"), Color.valueOf("A4A4A4")};
                        }
                    },
                    ModItems.exoticAlloy, new ShrapnelBulletType() {
                        {
                            length = 160;
                            damage = 710;
                            width = 25;
                            serrationLenScl = 8;
                            serrationSpaceOffset = 120;
                            serrationFadeOffset = 0;
                            serrations = 13;
                            serrationWidth = 3;
                            fromColor = Color.valueOf("FFF6A3");
                            toColor = Color.valueOf("FFE70F");
                            lifetime = 35;
                            shootEffect = Fx.sparkShoot;
                            smokeEffect = Fx.sparkShoot;
                        }
                    }
            );
            reloadTime = 70;
            shots = 1;
            burstSpacing = 3;
            inaccuracy = 0.2f;
            range = 230;
            size = 4;
            health = 2350;
        }};
        electron = new LaserTurret("electron") {{
            localizedName = "Dendrite";
            shootSound = ModSounds.electronShoot;
            loopSound = ModSounds.electronCharge;
            health = 8600;
            size = 10;
            recoilAmount = 11;
            shootShake = 4;
            shootCone = 15;
            rotateSpeed = 0.9f;
            shots = 1;
            hasItems = true;
            hasLiquids = true;
            rotate = true;
            shootDuration = 230;
            powerUse = 160;
            range = 400;
            firingMoveFract = 0.4f;
            localizedName = "Dendrite";
            description = "Monstruous turret with Electric Laser.";
            shootType = new ContinuousLaserBulletType() {
                public void update(Bullet b) {
                    SubBullets.addLightning(b, this);
                    super.update(b);
                }
                {
                    hitSize = 14;
                    drawSize = 520;
                    width = 34;
                    length = 390;
                    largeHit = true;
                    hitColor = Color.valueOf("f1fc58");
                    incendAmount = 4;
                    incendSpread = 10;
                    incendChance = 0.7f;
                    lightColor = Color.valueOf("fbffcc");
                    keepVelocity = true;
                    collides = true;
                    pierce = true;
                    hittable = true;
                    absorbable = false;
                    damage = 590;
                    shootEffect = Fx.railShoot;
                    despawnEffect = Fx.railHit;
                    knockback = 1;
                    lightning = 4;
                    lightningLength = 20;
                    lightningLengthRand = 20;
                    lightningDamage = 58;
                    lightningAngle = 15;
                    lightningCone = 50;
                    lightningColor = Color.valueOf("f1fc58");
                }};
            requirements(Category.turret, ItemStack.with(ModItems.phaseAlloy, 550, ModItems.exoticAlloy, 600, Items.surgeAlloy, 450, ModItems.chromium, 420, ModItems.graphenite, 1020, Items.metaglass, 420));
            reloadTime = 4;
        }};
        fragment = new PointDefenseTurret("fragment") {{
            localizedName = "Fragment";
            description = "Upgraded Segment, consumes more power as well Refrigerant for cooling.";
            size = 3;
            health = 420;
            range = 250;
            hasPower = true;
            shootLength = 15;
            bulletDamage = 32;
            reloadTime = 5;
            consumes.power(6f);
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(false, false);
            requirements(Category.turret, ItemStack.with(ModItems.phaseAlloy, 70, Items.silicon, 180, ModItems.odinum, 150));
        }};
        impulse = new ItemTurret("impulse") {{
            health = 840;
            size = 1;
            rotateSpeed = 1.8f;
            shots = 1;
            reloadTime = 30;
            hasItems = true;
            hasLiquids = true;
            range = 130;
            localizedName = "Impulse";
            description = "Arc upgraded version, can make more lightnings and shot a one basic bullet.";
            ammo(
                    Items.silicon, new BasicBulletType() {
                        public void update(Bullet b) {
                            SubBullets.addLightning(b, this);
                            super.update(b);
                        }
                        {
                            backColor = Color.valueOf("c2cc37");
                            width = 4;
                            height = 6;
                            shrinkY = 0.1f;
                            shrinkX = 0.2f;
                            spin = 1.2f;
                            speed = 2.1f;
                            damage = 19;
                            shootEffect = Fx.shockwave;
                            hitColor = frontColor = Color.valueOf("f1fc58");
                            despawnEffect = Fx.hitLancer;
                            lifetime = 60;
                            knockback = 1;
                            lightning = 3;
                            lightningLength = 3;
                            lightningLengthRand = 7;
                            lightningDamage = 9;
                            lightningAngle = 3;
                            lightningCone = 20;
                            lightningColor = Color.valueOf("f1fc58");
                        }
                    }
            );
            requirements(Category.turret, ItemStack.with(Items.silicon, 70, ModItems.graphenite, 50, Items.lead, 90, Items.copper, 110));
        }};
        katana = new ItemTurret("katana") {{
            localizedName = "Katana";
            description = "Strong artillery turret with powerful ammo. Requires a strong alloy to fire. Better in groups.";
            range = 300;
            recoilAmount = 6;
            reloadTime = 17;
            size = 4;
            shots = 4;
            health = 3000;
            inaccuracy = 0.1f;
            rotateSpeed = 1.4f;
            targetAir = false;
            targetGround = true;
            ammo(
//                    ModItems.exoticAlloy, artilleryExplosive,
//                    ModItems.exoticAlloy, ModBullets.heavyMethaneShot,
                    Items.surgeAlloy, artilleryIncendiary
            );
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.14f).optional(true, true);
            requirements(Category.turret, ItemStack.with(ModItems.exoticAlloy, 120, ModItems.graphenite, 330, Items.plastanium, 150, Items.silicon, 200, Items.surgeAlloy, 50));
        }};
        neuron = new ItemTurret("neuron") {{
            localizedName = "Neuron";
            description = "A small turret that fires lasers that do splash damage. Requires power aswell as Exotic Alloy to shoot.";
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 150, Items.silicon, 150, Items.surgeAlloy, 70, Items.plastanium, 120, Items.thorium, 270));
            ammo(
                    ModItems.exoticAlloy, new PointBulletType() {
                        {
                            shootEffect = Fx.instShoot;
                            hitEffect = Fx.instHit;
                            smokeEffect = Fx.smokeCloud;
                            trailEffect = Fx.instTrail;
                            despawnEffect = Fx.instBomb;
                            trailSpacing = 12;
                            damage = 400;
                            buildingDamageMultiplier = 0.3f;
                            speed = 50;
                            hitShake = 1;
                            ammoMultiplier = 2;
                        }
                    },
                    ModItems.plastic, new ShrapnelBulletType() {
                        {
                            length = 170;
                            damage = 250;
                            width = 15;
                            serrationLenScl = 3;
                            serrationSpaceOffset = 20;
                            serrationFadeOffset = 0;
                            serrations = 9;
                            serrationWidth = 4;
                            fromColor = Color.valueOf("AD5CFF");
                            toColor = Color.valueOf("870FFF");
                            lifetime = 45;
                            shootEffect = Fx.sparkShoot;
                            smokeEffect = Fx.sparkShoot;
                        }
                    }
            );
            consumes.power(3f);
            consumes.liquid(Liquids.cryofluid, 0.14f).optional(false, false);
            reloadTime = 35;
            shots = 1;
            burstSpacing = 6;
            inaccuracy = 1;
            range = 190;
            size = 2;
            health = 1100;
        }};
        perlin = new TractorBeamTurret("perlin") {{
            localizedName = "Perlin";
            description = "Upgraded Parallax. Does more damage to targets at cost of power and cooling.";
            size = 3;
            force = 14;
            scaledForce = 9;
            health = 390;
            range = 220;
            damage = 5;
            rotateSpeed = 10;
            hasPower = true;
            consumes.power(6.8f);
            consumes.liquid(Liquids.cryofluid, 0.1f).optional(false, false);
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 130, Items.thorium, 160, Items.silicon, 180));
        }};
        soul = new ItemTurret("soul") {{
            localizedName = "Soul";
            description = "A small, but powerful turret. Requires expensive Surge Alloy to fire. Can literally reap your soul.";
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 340, Items.silicon, 405, Items.surgeAlloy, 100, ModItems.plastic, 270, ModItems.odinum, 420));
            ammo(
                    Items.surgeAlloy, new SapBulletType() {
                        {
                            sapStrength = 1.2f;
                            length = 160;
                            damage = 400;
                            shootEffect = Fx.shootSmall;
                            hitColor = Color.valueOf("e88ec9");
                            color = Color.valueOf("e88ec9");
                            despawnEffect = Fx.none;
                            width = 2;
                            lifetime = 60;
                            knockback = 0.4f;
                        }
                    }
            );
            reloadTime = 60;
            shots = 1;
            burstSpacing = 6;
            inaccuracy = 1.1f;
            range = 140;
            size = 2;
            health = 1300;
        }};
        stinger = new ItemTurret("stinger") {{
            localizedName = "Stinger";
            description = "A huge, powerful rocket launcher. Doesn't require expensive ammo to fire. May be a better Ripple.";
            range = 280;
            reloadTime = 60;
            size = 4;
            shots = 4;
            health = 1400;
            inaccuracy = 0.4f;
            targetAir = true;
            targetGround = true;
            ammo(
                    Items.plastanium, missileSurge,
                    ModItems.graphenite, missileIncendiary,
                    ModItems.odinum, missileExplosive
            );
            consumes.liquid(Liquids.cryofluid, 0.1f).optional(false, true);
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 500, Items.plastanium, 250, Items.silicon, 420, ModItems.odinum, 360, Items.phaseFabric, 260));
        }};
        synaps = new ItemTurret("synaps") {{
            localizedName = "Synaps";
            description = "Electrical Sap turret, can shoot a sap bullet with mane lightnings.";
            health = 1260;
            size = 2;
            shootSound = ModSounds.shooting1;
            hasItems = true;
            hasLiquids = true;
            ammo(
                    Items.plastanium, new SapBulletType() {
                        public void update(Bullet b) {
                            SubBullets.addLightning(b, this);
                            super.update(b);
                        }
                        {
                            sapStrength = 0.48f;
                            length = 75;
                            damage = 47;
                            shootEffect = Fx.railShoot;
                            hitColor = color = Color.valueOf("fbff9e");
                            despawnEffect = Fx.railHit;
                            width = 1.4f;
                            lifetime = 35;
                            knockback = -1;
                            lightning = 6;
                            lightningLength = 2;
                            lightningLengthRand = 10;
                            lightningDamage = 9;
                            lightningAngle = 6;
                            lightningCone = 12;
                            lightColor = lightningColor = Color.valueOf("fbff9e");
                        }
                    }
            );

            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 120, Items.silicon, 140, Items.lead, 190, Items.titanium, 120));
        }};

        gem = new LaserTurret("gem") {{
            localizedName = "Gem";
            description = "Endgame turret with rainbow laser, consumes much liquids and power.";
            requirements(Category.turret, ItemStack.with(ModItems.chloroAlloy, 480, ModItems.phaseAlloy, 960, ModItems.graphenite, 2250, Items.silicon, 2230, Items.phaseFabric, 830));
            size = 14;
            health = 280 * size * size;
            range = 240f;
            reloadTime = 210;
            shootSound = ModSounds.gemShoot;
            loopSound = ModSounds.gemCharge;
            firingMoveFract = 0.4f;
            shootLength = -8;
            shootType = new ContinuousRainbowLaserBulletType() {{
                damage = 860f;
                length = 400f;
                lifetime = 120f;
                width = 30f;
                hittable = false;
                absorbable = false;
                hitEffect = Fx.hitMeltHeal;
                despawnEffect = ModFx.spikeSmoke;
                shootEffect = Fx.hitMeltdown;
                smokeEffect = Fx.rocketSmokeLarge;
            }};
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 0.5f)).update(false);
        }};
        shinigami = new ItemTurret("shinigami") {{
            float brange = range = 880f;
            localizedName = "Shinigami";
            description = "Railgun turret to defense from higher tiers of units, consumes Dense Composite to shoot, don't use this turret to attack buildings. ";
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 2000, Items.metaglass, 800, ModItems.phaseAlloy, 300, Items.plastanium, 700, Items.silicon, 2000));
            ammo(
                    ModItems.plastic, new PointBulletType() {{
                        shootEffect = Fx.lightningShoot;
                        hitEffect = Fx.instHit;
                        smokeEffect = Fx.reactorsmoke;
                        trailEffect = ModFx.shinigamiTrail;
                        despawnEffect = ModFx.instBomb;
                        trailSpacing = 25f;
                        damage = 17480;
                        buildingDamageMultiplier = 0.4f;
                        speed = brange;
                        hitShake = 9f;
                        ammoMultiplier = 2f;
                    }}
            );
            maxAmmo = 40;
            ammoPerShot = 10;
            rotateSpeed = 1.5f;
            reloadTime = 7700f;
            ammoUseEffect = Fx.casing3Double;
            recoilAmount = 10f;
            restitution = 0.009f;
            cooldown = 0.01f;
            shootShake = 6f;
            shots = 1;
            size = 8;
            shootCone = 3f;
            shootSound = Sounds.railgun;
            unitSort = (u, x, y) -> -u.maxHealth;
            coolantMultiplier = 0.8f;
            health = 260 * size * size;
            coolantUsage = 1.1f;
            consumes.add(new ConsumeLiquidFilter(liquid -> liquid.temperature <= 0.5f && liquid.flammability < 0.1f, 7f)).update(false);
            consumes.powerCond(24f, TurretBuild::isActive);
        }};

        voidwave = new ItemTurret("void-wave") {{
            localizedName = "Void Wave";
            description = "Turret with heavy and slow violet bullets, consumes liquid gas and Plastic.";
            range = 660;
            reloadTime = 220;
            size = 8;
            shots = 10;
            health = 12000;
            inaccuracy = 10f;
            shootSound = Sounds.plasmaboom;
            rotateSpeed = 0.4f;
            targetAir = true;
            targetGround = true;
            ammo(
                    ModItems.phaseAlloy, new BasicBulletType() {
                        {
                            damage = 960;
                            width = 15;
                            height = 18;
                            shrinkY = 0.1f;
                            shrinkX = 0.2f;
                            speed = 4;
                            hitSize = 50;
                            lifetime = 180;
                            status = StatusEffects.slow;
                            statusDuration = 120;
                            pierce = true;
                            buildingDamageMultiplier = 0.6f;
                            hittable = true;
                            ammoMultiplier = 1;
                            trailChance = 92f;
                            trailEffect = ModFx.fireworkTrail;
                            backColor = ModPal.blackHoleLaserBackColor;
                            frontColor = ModPal.blackHoleLaserColor;
                            hitColor = trailColor = lightColor = lightningColor = Color.violet;
                        }
                    }
            );
            consumes.liquid(ModLiquids.liquidMethane, 0.1f).optional(false, true);
            requirements(Category.turret, ItemStack.with(ModItems.graphenite, 2000, Items.titanium, 600, ModItems.phaseAlloy, 400, Items.phaseFabric, 300, Items.silicon, 1900));
        }};
        spark = new ItemTurret("spark"){
            {
                localizedName = "Spark";
                description = "Upgraded Salvo with using high-tec Graphenite.";
                requirements(Category.turret, ItemStack.with(Items.copper, 130, Items.graphite, 90, Items.silicon, 70, ModItems.graphenite, 80));
                ammo(
                        ModItems.graphenite, new BasicBulletType() {
                            {
                                damage = 60;
                                width = 10;
                                height = 11;
                                shrinkY = 0.1f;
                                shrinkX = 0.2f;
                                speed = 4.1f;
                                hitSize = 10;
                                lifetime = 190;
                                status = StatusEffects.shocked;
                                statusDuration = 60;
                                pierce = true;
                                buildingDamageMultiplier = 0.8f;
                                hittable = true;
                                ammoMultiplier = 1;
                                trailChance = 1.2f;
                                trailEffect = Fx.missileTrail;
                                backColor = ModPal.amethyst;
                                frontColor = ModPal.amethystLight;
                                hitColor = trailColor = lightColor = lightningColor = Color.violet;
                            }
                        },
                        Items.copper, new BasicBulletType() {
                            {
                                damage = 32;
                                width = 8;
                                height = 9;
                                shrinkY = 0.1f;
                                shrinkX = 0.2f;
                                speed = 3.1f;
                                hitSize = 11;
                                lifetime = 180;
                                status = StatusEffects.shocked;
                                statusDuration = 40;
                                pierce = true;
                                buildingDamageMultiplier = 0.8f;
                                hittable = true;
                                ammoMultiplier = 1;
                                trailChance = 1.2f;
                                trailEffect = Fx.missileTrail;
                                backColor = ModPal.orangeBackColor;
                                frontColor = ModPal.orangeFrontColor;
                                hitColor = trailColor = lightColor = lightningColor = Color.orange;
                            }
                        },
                        Items.titanium, new BasicBulletType() {
                            {
                                damage =48;
                                width = 9;
                                height = 10;
                                shrinkY = 0.1f;
                                shrinkX = 0.2f;
                                speed = 3.6f;
                                hitSize = 10;
                                lifetime = 190;
                                status = StatusEffects.shocked;
                                statusDuration = 50;
                                pierce = true;
                                buildingDamageMultiplier = 0.9f;
                                hittable = true;
                                ammoMultiplier = 1;
                                trailChance = 1.2f;
                                trailEffect = Fx.missileTrail;
                                backColor = ModPal.NorthernLightsNoiseColor;
                                frontColor = ModPal.NorthernLightsColor;
                                hitColor = trailColor = lightColor = lightningColor = Color.cyan;
                            }
                        }
                );

                size = 4;
                range = 180f;
                reloadTime = 42f;
                restitution = 0.04f;
                ammoEjectBack = 3.2f;
                cooldown = 0.08f;
                recoilAmount = 3.2f;
                shootShake = 1f;
                burstSpacing = 2f;
                shots = 5;
                ammoUseEffect = ModFx.napalmShoot;
                health = 360 * size * size;
                shootSound = Sounds.bang;
            }};
        exoticAlloyWallLarge = new Wall("dense-composite-wall-large") {{
            localizedName = "Dense Composite Wall Large";
            description = "A bigger Dense Composite Wall, creates lightings when shot.";
            health = 4060;
            size = 2;
            requirements(Category.defense, ItemStack.with(ModItems.phaseAlloy, 22));
            lightningChance = 0.24f;
        }};
        exoticAlloyWall = new Wall("dense-composite-wall") {{
            localizedName = "Dense Composite Wall";
            description = "An Dense Composite Wall, creates lightnings when shot.";
            health = 1160;
            size = 1;
            requirements(Category.defense, ItemStack.with(ModItems.phaseAlloy, 6));
            lightningChance = 0.2f;
        }};
        grapheniteWallLarge = new Wall("graphenite-wall-large") {{
            localizedName = "Large Graphenite Wall";
            description = "A bigger version of standard graphenite wall.";
            health = 2160;
            size = 2;
            requirements(Category.defense, ItemStack.with(ModItems.graphenite, 20, Items.silicon, 6));
        }};
        grapheniteWall = new Wall("graphenite-wall") {{
            localizedName = "Graphenite Wall";
            description = "A purple, medium strength wall.";
            size = 1;
            health = 620;
            requirements(Category.defense, ItemStack.with(ModItems.graphenite, 6, Items.silicon, 2));

        }};
        odinumWallLarge = new Wall("odinum-wall-large") {{
            localizedName = "Odinum Wall Large";
            description = "Bigger Odinum Wall, can deflect bullets.";
            health = 2860;
            size = 2;
            requirements(Category.defense, ItemStack.with(ModItems.odinum, 28));
            chanceDeflect = 14;
            flashHit = true;
        }};
        odinumWall = new Wall("odinum-wall") {{
            localizedName = "Odinum Wall";
            description = "Medium strength wall, a little radioactive.";
            size = 1;
            health = 620;
            requirements(Category.defense, ItemStack.with(ModItems.odinum, 8));
            chanceDeflect = 12;
            flashHit = true;
        }};
        plasticWallLarge = new Wall("plastic-wall-large") {{
            localizedName = "Plastic Wall Large";
            description = "A bigger Plastic wall, can deflect some bullets.";
            health = 3820;
            size = 2;
            requirements(Category.defense, ItemStack.with(ModItems.plastic, 24, Items.metaglass, 10));
            insulated = true;
            absorbLasers = true;
        }};
        plasticWall = new Wall("plastic-wall") {{
            localizedName = "Plastic Wall";
            description = "A Plastic wall, can deflect some bullets.";
            health = 980;
            size = 1;
            requirements(Category.defense, ItemStack.with(ModItems.plastic, 6, Items.metaglass, 4));
            insulated = true;
            absorbLasers = true;
        }};
        largeChloroWall = new ReflectionWall("chloro-wall-large") {{
            laserReflect = true;
            lightningReflect = true;
            localizedName = "Large Chloro Wall";
            description = "Big organic wall with ability to reflect enemy lasers.";
            size = 2;
            health = 5450;
            requirements(Category.defense, ItemStack.with(ModItems.chloroAlloy, 60));
        }};

        chloroWall = new ReflectionWall("chloro-wall") {{
            localizedName = "Chloro Wall";
            description = "Strong organic wall with ability to reflect enemy lasers.";
            size = 1;
            laserReflect = true;
            lightningReflect = true;
            health = 2010;
            requirements(Category.defense, ItemStack.with(ModItems.chloroAlloy, 20));
        }};
    }
}
