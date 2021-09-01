package braindustry.tools;

import arc.Core;
import arc.files.Fi;
import arc.func.Cons;
import arc.func.Func;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.ArcRuntimeException;
import arc.util.Log;
import arc.util.Tmp;
import arc.util.noise.Noise;
import arc.util.noise.Ridged;
import arc.util.noise.VoronoiNoise;
import braindustry.entities.abilities.ModAbility;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import braindustry.entities.abilities.PowerGeneratorAbility;
import braindustry.gen.BDContentRegions;
import braindustry.type.ModUnitType;
import braindustry.type.SelfIconGenerator;
import mindustry.game.Team;
import mindustry.gen.Legsc;
import mindustry.gen.Mechc;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.type.Weapon;
import mindustry.world.Block;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.environment.*;
import mindustry.world.blocks.legacy.LegacyBlock;
import mindustry.world.meta.BuildVisibility;
import mma.gen.ModContentRegions;
import mma.tools.ModGenerators;

import static braindustry.tools.BDImagePacker.*;
import static mindustry.Vars.*;
import static mma.tools.gen.MindustryImagePacker.drawCenter;

public class BDGenerators extends ModGenerators {
    @Override
    protected void blockIcons() {
        Pixmap colors = new Pixmap(content.blocks().size, 1);

        for (Block block : content.blocks()) {
            ModContentRegions.loadRegions(block);
            BDContentRegions.loadRegions(block);
            if (block.isAir() || block instanceof ConstructBlock || block instanceof OreBlock || block instanceof LegacyBlock)
                continue;

            block.load();
            block.loadIcon();

            TextureRegion[] regions = block.getGeneratedIcons();

            if (block.variants > 0 || block instanceof Floor) {
                for (TextureRegion region : block.variantRegions()) {
                    GenRegion gen = (GenRegion) region;
                    if (gen.path == null) continue;
                    gen.path.copyTo(Fi.get("../editor/editor-" + gen.path.name()));
                }
            }

            for (TextureRegion region : block.makeIconRegions()) {
                GenRegion gen = (GenRegion) region;
                save(get(region).outline(block.outlineColor, block.outlineRadius), gen.name + "-outline");
            }

            Pixmap shardTeamTop = null;

            if (block.teamRegion.found()) {
                Pixmap teamr = get(block.teamRegion);

                for (Team team : Team.all) {
                    if (team.hasPalette) {
                        Pixmap out = new Pixmap(teamr.width, teamr.height);
                        teamr.each((x, y) -> {
                            int color = teamr.getRaw(x, y);
                            int index = color == 0xffffffff ? 0 : color == 0xdcc6c6ff ? 1 : color == 0x9d7f7fff ? 2 : -1;
                            out.setRaw(x, y, index == -1 ? teamr.getRaw(x, y) : team.palette[index].rgba());
                        });
                        save(out, block.name + "-team-" + team.name);

                        if (team == Team.sharded) {
                            shardTeamTop = out;
                        }
                    }
                }
            }

            if (regions.length == 0) {
                continue;
            }

            try {
                Pixmap last = null;
                if (block.outlineIcon) {
                    GenRegion region = (GenRegion) regions[block.outlinedIcon >= 0 ? block.outlinedIcon : regions.length - 1];
                    Pixmap base = get(region);
                    Pixmap out = last = base.outline(block.outlineColor, block.outlineRadius);

                    //do not run for legacy ones
                    if (block.outlinedIcon >= 0) {
                        //prevents the regions above from being ignored/invisible/etc
                        for (int i = block.outlinedIcon + 1; i < regions.length; i++) {
                            out.draw(get(regions[i]), true);
                        }
                    }


                    if (false) {
                        region.path.delete();
                        save(out, block.name);
                    }
                }

                if (!regions[0].found()) {
                    continue;
                }
                boolean selfGenerator = block instanceof SelfIconGenerator;
                Pixmap image = get(regions[0]);

                int i = 0;

                for (TextureRegion region : regions) {
                    i++;
                    if (i == 1 && selfGenerator) {
                        image.draw(((SelfIconGenerator) block).generate(get(regions[0]), BDImagePacker::get));
                    } else if (i != regions.length || last == null) {
                        image.draw(get(region), true);
                    } else {
                        image.draw(last, true);
                    }

                    //draw shard (default team top) on top of first sprite
                    if (region == block.teamRegions[Team.sharded.id] && shardTeamTop != null) {
                        image.draw(shardTeamTop, true);
                    }
                }
                if (!(regions.length == 1 && regions[0] == Core.atlas.find(block.name) && shardTeamTop == null) || selfGenerator) {
                    save(image, /*"block-" +*/ block.name + "-full");
                }

                save(image, "../editor/" + block.name + "-icon-editor");

                if (block.buildVisibility != BuildVisibility.hidden) {
                    saveScaled(image, block.name + "-icon-logic", logicIconSize);
                }
                saveScaled(image, "../ui/block-" + block.name + "-ui", Math.min(image.width, maxUiIcon));

                boolean hasEmpty = false;
                Color average = new Color(), c = new Color();
                float asum = 0f;
                for (int x = 0; x < image.width; x++) {
                    for (int y = 0; y < image.height; y++) {
                        Color color = c.set(image.get(x, y));
                        average.r += color.r * color.a;
                        average.g += color.g * color.a;
                        average.b += color.b * color.a;
                        asum += color.a;
                        if (color.a < 0.9f) {
                            hasEmpty = true;
                        }
                    }
                }

                average.mul(1f / asum);

                if (block instanceof Floor) {
                    average.mul(0.77f);
                } else {
                    average.mul(1.1f);
                }
                //encode square sprite in alpha channel
                average.a = hasEmpty ? 0.1f : 1f;
                colors.setRaw(block.id, 0, average.rgba());
            } catch (NullPointerException e) {
                Log.err("Block &ly'@'&lr has an null region!", block);
            }
        }

        save(colors, "../../../assets/sprites/block_colors");
    }

    @Override
    protected void unitIcons() {
        content.units().each(type -> {
            if (type.isHidden()) return; //hidden units don't generate

            ObjectSet<String> outlined = new ObjectSet<>();

            try {
                type.load();
                type.loadIcon();
                type.init();
                Func<Pixmap, Pixmap> outline = i -> {
                    int upScale = 0;
                    int x = 0, y = 0;
                    for (x = 0; x < i.width; x++) {
                        for (y = 0; y < 3; y++) {
                            boolean bool = i.getA(x, y) == 0 && i.getA(x, i.height - y - 1) == 0;
                            if (!bool) {
                                upScale = Math.max(y, upScale);
                            }
                        }
                    }
                    for (y = 0; y < i.height; y++) {
                        for (x = 0; x < 3; x++) {
                            boolean bool = i.getA(x, y) == 0 && i.getA(i.width - x - 1, y) == 0;
                            if (!bool) {
                                upScale = Math.max(x, upScale);
                            }
                        }
                    }
                    if (upScale != 0) {
                        Pixmap pixmap = new Pixmap(i.width + upScale * 2, i.height + upScale * 2);
                        pixmap.draw(i, pixmap.width / 2 - i.width / 2, pixmap.height / 2 - i.height / 2);
                        i = pixmap;
                    }
                    return i.outline(Pal.darkerMetal, 3);
                };
                Cons<TextureRegion> outliner = t -> {
                    if (t != null && t.found()) {
                        replace(t, outline.get(get(t)));
                    }
                };


                Seq<Weapon> abilitiesWeapons = new Seq<>();
                Seq<TextureRegion> outlineRegions = new Seq<>();
                Seq<ModAbility> modAbilities = new Seq<>();
                if (type instanceof ModUnitType) {
                    modAbilities.addAll(((ModUnitType) type).getModAbilities());
                    modAbilities.each(modAbility -> {
                        modAbility.load();
                        abilitiesWeapons.addAll(modAbility.weapons());
                        outlineRegions.addAll(modAbility.outlineRegions());
                    });
                }
                for (Weapon weapon : abilitiesWeapons) {
                    if (outlined.add(weapon.name) && has(weapon.name)) {
                        save(outline.get(get(weapon.name)), weapon.name + "-outline");
                    }
                }
                /*for (Weapon weapon : type.weapons.copy().addAll(abilitiesWeapons)) {
                    if (outlined.add(weapon.name) && has(weapon.name)) {
                        save(outline.get(get(weapon.name)), weapon.name + "-outline");
                    }
                }*/
                for (TextureRegion outlineRegion : outlineRegions) {
                    if (!outlineRegion.found()) continue;
                    save(outline.get(get(outlineRegion)), outlineRegion.asAtlas().name + "-outline");
                }
                outliner.get(type.jointRegion);
                outliner.get(type.footRegion);
                outliner.get(type.legBaseRegion);
                outliner.get(type.baseJointRegion);
                Unit inst = type.constructor.get();
                if (inst instanceof Legsc) outliner.get(type.legRegion);

                Pixmap image = outline.get(get(type.region));


//                save(image, type.name + "-outline");

                for (ModAbility modAbility : modAbilities) {
                    if (modAbility instanceof PowerGeneratorAbility) {
                        PowerGeneratorAbility ability = (PowerGeneratorAbility) modAbility;
                        if (!ability.bottomRegion.found()) continue;
                        Pixmap bottom = get(ability.bottomRegion);
                        bottom = drawScaleAt(bottom, image, bottom.width / 2 - image.width / 2, bottom.height / 2 - image.height / 2);
                        image = drawScaleAt(image, bottom, image.width / 2 - bottom.width / 2, image.height / 2 - bottom.height / 2);
                    }
                }
                //draw mech parts
                if (inst instanceof Mechc) {
                    drawCenter(image, get(type.baseRegion));
                    drawCenter(image, get(type.legRegion));
                    drawCenter(image, get(type.legRegion).flipX());
                    drawCenter(image, get(type.region));
//                    image.draw(get(type.region), true);
                }

                //draw outlines
                for (Weapon weapon : type.weapons) {
                    weapon.load();

                    Pixmap pixmap = weapon.flipSprite ? outline.get(get(weapon.region)).flipX() : outline.get(get(weapon.region));
                    int x = (int) (weapon.x / Draw.scl + image.width / 2f - weapon.region.width / 2f);
                    int y = (int) (-weapon.y / Draw.scl + image.height / 2f - weapon.region.height / 2f);
                    image = drawScaleAt(image, pixmap, x, y);
                }

                //draw base region on top to mask weapons
                drawCenter(image, get(type.region));
//                image.draw(get(type.region), true);
                int baseColor = Color.valueOf("ffa665").rgba();

                Pixmap baseCell = get(type.cellRegion);
                Pixmap cell = new Pixmap(type.cellRegion.width, type.cellRegion.height);
                cell.each((x, y) -> cell.set(x, y, Color.muli(baseCell.getRaw(x, y), baseColor)));
//                image.draw(cell, image.width / 2 - cell.width / 2, image.height / 2 - cell.height / 2, image.width / 2 - cell.width / 2, image.height / 2 - cell.height / 2, true);
                drawCenter(image, cell);
                for (Weapon weapon : type.weapons) {
                    weapon.load();

                    Pixmap wepReg = weapon.top ? outline.get(get(weapon.region)) : get(weapon.region);
                    if (weapon.flipSprite) {
                        wepReg = wepReg.flipX();
                    }

                    image = drawScaleAt(image, wepReg, (int) (weapon.x / Draw.scl + image.width / 2f - weapon.region.width / 2f), (int) (-weapon.y / Draw.scl + image.height / 2f - weapon.region.height / 2f));
                }


                image = clearAlpha(image);
                save(image, /*"unit-" +*/ type.name + "-shadow");
                for (ModAbility modAbility : modAbilities) {
                    if (modAbility instanceof OrbitalPlatformAbility) {
                        OrbitalPlatformAbility ability = (OrbitalPlatformAbility) modAbility;
                        if (!ability.region().found()) continue;
                        Pixmap region = outline.get(get(ability.region()));
                        Log.info("type: @", type);
                        for (int i = 0; i < ability.weapons.length; i++) {
                            int dx = 0, dy = 0;
                            Tmp.v1.trns(i / (float) ability.platformsCount() * 360f, type.hitSize, type.hitSize).scl(1f / Draw.scl);
                            dx = (int) Tmp.v1.x;
                            dy = (int) Tmp.v1.y;
                            image = drawScaleAt(image, region, image.width / 2 - region.width / 2 + dx, image.height / 2 - region.height / 2 + dy);

                            Weapon weapon = ability.weapons[i];
                            if (weapon == null) continue;
                            weapon.load();
                            if (!weapon.region.found()) {
                                Log.err("cannot find region @", weapon.region);
                            }
                            Pixmap pixmap = get(weapon.region);
                            image = drawScaleAt(image, outline.get(pixmap), image.width / 2 - pixmap.width / 2 + dx, image.height / 2 - pixmap.height / 2 + dy);

                        }
//                        Pixmap region = get(ability.region());
//                        drawScaleAt(image,bottom,image.width/2-bottom.width/2,image.height/2-bottom.height/2);
                    }
                }
                image = clearAlpha(image);
                save(image, /*"unit-" +*/ type.name + "-full");
                Rand rand = new Rand();
                rand.setSeed(type.name.hashCode());

                //generate random wrecks

                int splits = 3;
                float degrees = rand.random(360f);
                float offsetRange = Math.max(image.width, image.height) * 0.15f;
                Vec2 offset = new Vec2(1, 1).rotate(rand.random(360f)).setLength(rand.random(0, offsetRange)).add(image.width / 2f, image.height / 2f);

                Pixmap[] wrecks = new Pixmap[splits];
                for (int i = 0; i < wrecks.length; i++) {
                    wrecks[i] = new Pixmap(image.width, image.height);
                }

                VoronoiNoise vn = new VoronoiNoise(type.id, true);
                Pixmap imageCache = image;
                image.each((x, y) -> {
                    //add darker cracks on top
                    boolean rValue = Math.max(Ridged.noise2d(1, x, y, 3, 1f / (20f + imageCache.width / 8f)), 0) > 0.16f;
                    //cut out random chunks with voronoi
                    boolean vval = vn.noise(x, y, 1f / (14f + imageCache.width / 40f)) > 0.47;

                    float dst = offset.dst(x, y);
                    //distort edges with random noise
                    float noise = (float) Noise.rawNoise(dst / (9f + imageCache.width / 70f)) * (60 + imageCache.width / 30f);
                    int section = (int) Mathf.clamp(Mathf.mod(offset.angleTo(x, y) + noise + degrees, 360f) / 360f * splits, 0, splits - 1);
                    if (!vval) wrecks[section].setRaw(x, y, Color.muli(imageCache.getRaw(x, y), rValue ? 0.7f : 1f));
                });

                for (int i = 0; i < wrecks.length; i++) {
                    save(wrecks[i], "../rubble/" + type.name + "-wreck" + i);
                }

                int maxd = Math.min(Math.max(image.width, image.height), maxUiIcon);
                Pixmap fit = new Pixmap(maxd, maxd);
                drawScaledFit(fit, image);

                saveScaled(fit, type.name + "-icon-logic", logicIconSize);
                save(fit, "../ui/" + type.name + "-ui");
            } catch (Exception e) {
                Log.err("WARNING: Skipping unit " + type.name + ": @", e);
            }

        });
    }
}
