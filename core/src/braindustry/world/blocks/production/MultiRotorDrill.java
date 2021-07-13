package braindustry.world.blocks.production;

import arc.graphics.Blending;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Time;
import braindustry.type.Rotor;
import braindustry.type.Rotor.DrillRotorCons;
import mindustry.entities.units.BuildPlan;
import mindustry.world.blocks.production.Drill;

public class MultiRotorDrill extends Drill {
    public Seq<Rotor> rotors = new Seq<>();
    public boolean drawRotator = true;

    public MultiRotorDrill(String name) {
        super(name);
    }

    @Override
    public void init() {
        super.init();
    }

    public void rotors(Rotor... rotors) {
        this.rotors = Seq.with(rotors);
        for (Rotor rotor : rotors) {
            if (rotor.size==-1)rotor.size=size;
        }
    }
    public Rotor rotor(float x,float y,float size){
        return new Rotor(x,y,size);
    }
    public Rotor rotor(float x,float y){
        return new Rotor(x,y);
    }
    public void rotors(float size,Rotor... rotors) {
        this.rotors = Seq.with(rotors);
        for (Rotor rotor : rotors) {
            rotor.size=size;
        }
    }

    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        Draw.rect(this.region, req.drawx(), req.drawy(), !this.rotate ? 0.0F : (float) (req.rotation * 90));
        if (drawRotator) {
            float x = req.drawx() - Mathf.floor(this.size / 2f) * 8,
                    y = req.drawy() - Mathf.floor(this.size / 2f) * 8;
            rotors.each((vec) -> {
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
                    effectAboveRotor((x,y,size)->updateEffect.at(x + Mathf.range(size * 2f), y + Mathf.range(size * 2f)));
            } else {
                lastDrillSpeed = 0f;
                warmup = Mathf.lerpDelta(warmup, 0f, warmupSpeed);
                return;
            }

            float delay = drillTime + hardnessDrillMultiplier * dominantItem.hardness;

            if (dominantItems > 0 && progress >= delay && items.total() < itemCapacity) {
                offload(dominantItem);

                progress %= delay;
                effectAboveRotor((x,y,size)->drillEffect.at(x + Mathf.range(size), y + Mathf.range(size), dominantItem.color));
            }
        }

        protected void effectAboveRotor(DrillRotorCons cons2) {
            Rotor rotor = rotors.random();
            float xo = this.x - Mathf.floor(this.block.size / 2f) * 8;
            float yo = this.y - Mathf.floor(this.block.size / 2f) * 8;
            cons2.get(xo + rotor.x*8, yo + rotor.y*8, rotor.size);
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
            if (rotors.size > 0) {
                float x = this.x - Mathf.floor(this.block.size / 2f) * 8,
                        y = this.y - Mathf.floor(this.block.size / 2f) * 8;
                rotors.each((rotor) -> {
                    Draw.rect(rotatorRegion, x + rotor.x * 8, y + rotor.y * 8, this.timeDrilled * rotateSpeed);
                    Draw.rect(topRegion, x + rotor.x * 8, y + rotor.y * 8);
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
