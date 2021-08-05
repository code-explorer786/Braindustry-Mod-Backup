package braindustry.world.blocks.production;

import acontent.world.meta.AStats;
import arc.Core;
import arc.func.Func;
import arc.graphics.Blending;
import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.PixmapRegion;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import braindustry.cfunc.Cons3;
import braindustry.type.Rotator;
import braindustry.type.Rotator.DrillRotorCons;
import braindustry.type.SelfIconGenerator;
import braindustry.world.meta.BDStat;
import mindustry.entities.units.BuildPlan;
import mindustry.graphics.MultiPacker;
import mindustry.world.blocks.production.Drill;

public class MultiRotatorDrill extends Drill implements SelfIconGenerator {
    public Seq<Rotator> rotators = new Seq<>();
    public boolean drawRotator = true;
    public AStats aStats = new AStats();

    public MultiRotatorDrill(String name) {
        super(name);
        stats = aStats.copy(stats);
    }

    @Override
    public void setStats() {
        super.setStats();
        aStats.add(BDStat.rotatorsCount, rotators.size);
    }

    public void rotators(Rotator... rotators) {
        this.rotators = Seq.with(rotators);
        for (Rotator rotator : rotators) {
            if (rotator.size == -1) rotator.size = size;
        }
    }

    @Override
    public TextureRegion[] getGeneratedIcons() {
        return super.getGeneratedIcons();
    }

    @Override
    public void createIcons(MultiPacker packer) {
        super.createIcons(packer);

        if (!synthetic()) {
            PixmapRegion image = Core.atlas.getPixmap(fullIcon);
            mapColor.set(image.get(image.width / 2, image.height / 2));
        }

        if (variants > 0) {
            for (int i = 0; i < variants; i++) {
                String rname = name + (i + 1);
                packer.add(MultiPacker.PageType.editor, "editor-" + rname, Core.atlas.getPixmap(rname));
            }
        }

        Pixmap last = null;

        TextureRegion[] gen = icons();

        if (outlineIcon) {
            PixmapRegion region = Core.atlas.getPixmap(gen[outlinedIcon >= 0 ? outlinedIcon : gen.length - 1]);
            Pixmap out = last = Pixmaps.outline(region, outlineColor, outlineRadius);
            if (Core.settings.getBool("linear")) {
                Pixmaps.bleed(out);
            }
            packer.add(MultiPacker.PageType.main, name, out);
        }

        PixmapRegion editorBase = Core.atlas.getPixmap(fullIcon);

        if (gen.length > 1) {
            Pixmap base = Core.atlas.getPixmap(gen[0]).crop();
            for (int i = 1; i < gen.length; i++) {
                if (i == gen.length - 1 && last != null) {
                    base.draw(last, 0, 0, true);
                } else {
                    base.draw(Core.atlas.getPixmap(gen[i]), true);
                }
            }

            packer.add(MultiPacker.PageType.main, "block-" + name + "-full", base);

            editorBase = new PixmapRegion(base);
        }

        packer.add(MultiPacker.PageType.editor, name + "-icon-editor", editorBase);
    }

    public Rotator rotator(float x, float y, float size) {
        return new Rotator(x, y, size);
    }

    public Rotator rotator(float x, float y) {
        return new Rotator(x, y);
    }

    public void rotators(float size, Rotator... rotators) {
        this.rotators = Seq.with(rotators);
        for (Rotator rotator : rotators) {
            rotator.size = size;
        }
    }

    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        Draw.rect(this.region, req.drawx(), req.drawy(), !this.rotate ? 0.0F : (float) (req.rotation * 90));
        if (drawRotator) {
            float x = req.drawx() - Mathf.floor(this.size / 2f) * 8,
                    y = req.drawy() - Mathf.floor(this.size / 2f) * 8;
            rotators.each((vec) -> {
                Draw.rect(rotatorRegion, x + vec.x * 8, y + vec.y * 8);
                Draw.rect(topRegion, x + vec.x * 8, y + vec.y * 8);
            });
        }
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        }
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{this.region};
    }

    @Override
    public Pixmap generate(Pixmap icon, Func<TextureRegion, Pixmap> pix) {
        Pixmap rotator = pix.get(rotatorRegion);
        Pixmap top = pix.get(topRegion);
        Cons3<Pixmap,Integer,Integer> draw= (pixmap,drawx,drawy) -> {
            icon.draw(pixmap.copy(),
                    0,0,
                    pixmap.width,pixmap.height,
                    drawx-pixmap.width/2, drawy-pixmap.height/2,
                    pixmap.width, pixmap.height,
                    false, true);
        };
        for (Rotator rotor : rotators) {
            int x = (int) (rotor.x  * 32+16);
            int y = (int) ((size-rotor.y) * 32-16);
            draw.get(rotator,x,y);
            draw.get(top,x,y);
        }
        return icon;
    }

    public class MultiRotorDrillBuild extends DrillBuild {
        @Override
        public void updateTile() {
            //Anuke code
            if (dominantItem == null) {
                return;
            }

            if (timer(timerDump, dumpTime)) {
                dump(dominantItem);
            }

            timeDrilled += warmup * delta();

            if (items.total() < itemCapacity && dominantItems > 0 && consValid()) {

                float speed = 1f;

                if (cons.optionalValid()) {
                    speed = liquidBoostIntensity;
                }

                speed *= efficiency(); // Drill slower when not at full power

                lastDrillSpeed = (speed * dominantItems * warmup) / (drillTime + hardnessDrillMultiplier * dominantItem.hardness);
                warmup = Mathf.lerpDelta(warmup, speed, warmupSpeed);
                progress += delta() * dominantItems * speed * warmup;

                if (Mathf.chanceDelta(updateEffectChance * warmup))
                    effectAboveRotor((x, y, size) -> updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f)));
            } else {
                lastDrillSpeed = 0f;
                warmup = Mathf.lerpDelta(warmup, 0f, warmupSpeed);
                return;
            }

            float delay = drillTime + hardnessDrillMultiplier * dominantItem.hardness;

            if (dominantItems > 0 && progress >= delay && items.total() < itemCapacity) {
                offload(dominantItem);

                progress %= delay;
                effectAboveRotor((x, y, size) -> drillEffect.at(x + Mathf.range(size), y + Mathf.range(size), dominantItem.color));
            }
        }

        protected void effectAboveRotor(DrillRotorCons cons2) {
            Rotator rotator = rotators.random();
            float xo = this.x - Mathf.floor(this.block.size / 2f) * 8;
            float yo = this.y - Mathf.floor(this.block.size / 2f) * 8;
            cons2.get(xo + rotator.x * 8, yo + rotator.y * 8, rotator.size);
        }

        public void draw() {
            float s = 0.3F;
            float ts = 0.6F;
            Draw.rect(region, this.x, this.y);
            super.drawCracks();
            if (drawRim) {
                Draw.color(heatColor);
                Draw.alpha(this.warmup * ts * (1.0F - s + Mathf.absin(Time.time, 3.0F, s)));
                Draw.blend(Blending.additive);
                Draw.rect(rimRegion, this.x, this.y);
                Draw.blend();
                Draw.color();
            }
            if (rotators.size > 0) {
                float x = this.x - Mathf.floor(this.block.size / 2f) * 8,
                        y = this.y - Mathf.floor(this.block.size / 2f) * 8;
                rotators.each((rotator) -> {
                    Draw.rect(rotatorRegion, x + rotator.x * 8, y + rotator.y * 8, this.timeDrilled * rotateSpeed);
                    Draw.rect(topRegion, x + rotator.x * 8, y + rotator.y * 8);
                });
            }
            if (this.dominantItem != null && drawMineItem) {
                Draw.color(this.dominantItem.color);
                Draw.rect(itemRegion, this.x, this.y);
                Draw.color();
            }

        }
    }
}