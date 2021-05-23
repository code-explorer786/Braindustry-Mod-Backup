package braindustry.world.blocks.distribution;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Vec2;
import arc.util.Eachable;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.BufferItem;
import mindustry.gen.Building;
import mindustry.gen.Teamc;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.world.Block;
import mindustry.world.DirectionalItemBuffer;
import mindustry.world.meta.BlockGroup;
import mindustryAddition.graphics.ModLines;

public class SideJunction extends Block {
    public float speed = 26.0F;
    public int capacity = 6;
    public @ModAnnotations.Load("@-top") TextureRegion regionTop;
    public @ModAnnotations.Load("@-bottom") TextureRegion regionBottom;
    public SideJunction(String name) {
        super(name);
        this.update = true;
        this.solid = true;
        this.group = BlockGroup.transportation;
        this.unloadable = false;
        this.noUpdateDisabled = true;
    }

    public boolean outputsItems() {
        return true;
    }


    public void drawPlace(int x, int y, int rotation, boolean valid) {
        super.drawPlace(x, y, rotation, valid);
    }
    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        TextureRegion reg = this.getRequestRegion(req, list);
        float x = req.drawx();
        float y = req.drawy();
        float halfTile = 4.0F;
        Draw.rect(reg, x, y, (float)(this.size * 8), (float)(this.size * 8), 0.0F);
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        }

        float rotation = (float)req.rotation * 90.0F;
        float radius = halfTile * (float)req.block.size;
        Vec2 trns = (new Vec2()).trns(rotation, -radius, radius);
        Draw.color(Pal.accent);
        Lines.stroke(2.0F);
        ModLines.swirl(trns.x + x, trns.y + y, radius, 0.25F, 180.0F + rotation + 90.0F);
        trns.rotate(180.0F);
        ModLines.swirl(trns.x + x, trns.y + y, radius, 0.25F, rotation + 90.0F);
    }
    public class SideJunctionBuild extends Building {
        public DirectionalItemBuffer buffer;

        public SideJunctionBuild() {
            this.buffer = new DirectionalItemBuffer(capacity);
        }

        public int acceptStack(Item item, int amount, Teamc source) {
            return 0;
        }

        public void updateTile() {
            for(int i = 0; i < 4; ++i) {
                if (this.buffer.indexes[i] > 0) {
                    if (this.buffer.indexes[i] > capacity) {
                        this.buffer.indexes[i] = capacity;
                    }

                    long l = this.buffer.buffers[i][0];
                    float time = BufferItem.time(l);
                    if (Time.time >= time + speed / this.timeScale || Time.time < time) {
                        Item item = Vars.content.item(BufferItem.item(l));
                        Building dest = this.nearby(i);
                        if (item != null && dest != null && dest.acceptItem(this, item) && dest.team == this.team) {
                            dest.handleItem(this, item);
                            System.arraycopy(this.buffer.buffers[i], 1, this.buffer.buffers[i], 0, this.buffer.indexes[i] - 1);
                            int var10002 = this.buffer.indexes[i]--;
                        }
                    }
                }
            }

        }

        public void handleItem(Building source, Item item) {
            int relative = source.relativeTo(this.tile);
            this.buffer.accept(relative, item);
        }

        public boolean acceptItem(Building source, Item item) {
            int relative = source.relativeTo(this.tile);
            if (relative != -1 && this.buffer.accepts(relative)) {
                Building to = this.nearby(relative);
                return to != null && to.team == this.team;
            } else {
                return false;
            }
        }

        public void write(Writes write) {
            super.write(write);
            this.buffer.write(write);
        }

        public void read(Reads read, byte revision) {
            super.read(read, revision);
            this.buffer.read(read);
        }
    }
}
