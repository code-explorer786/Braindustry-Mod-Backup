package braindustry.world.blocks.distribution;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Eachable;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import mindustry.entities.units.BuildPlan;
import mindustry.gen.Building;
import mindustry.type.Item;
//import mindustry.ui.Cicon;
import mindustry.world.Block;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.meta.BlockGroup;

import java.util.Arrays;

import static mindustry.Vars.content;

public class SmartSorter extends Block {
    public boolean invert;
    public @ModAnnotations.Load("@-cross")
    TextureRegion crossRegion;
    public @ModAnnotations.Load("@-item")
    TextureRegion itemRegion;

    public SmartSorter(String name) {
        super(name);
        update = true;
        solid = true;
        instantTransfer = true;
        group = BlockGroup.transportation;
        configurable = true;
        unloadable = false;
        saveConfig = true;

        config(String.class, (SmartSorterBuild tile, String str) -> {
            String[] split = str.split(" ");
            for (int i = 0; i < split.length; i++) {
                tile.configSide(i, Strings.parseInt(split[i]));
            }
        });
//        configClear((SmartSorterBuild tile) -> Arrays.fill(tile.sides, -1));
    }

    @Override
    public void drawRequestConfig(BuildPlan req, Eachable<BuildPlan> list) {
        float x = req.drawx();
        float y = req.drawy();
        if (req.config == null) return;
        String[] strings = req.config.toString().split(" ");
        int[] sides = new int[4];
        if (strings.length == 4) {
            for (int i = 0; i < strings.length; i++) {
                sides[i] = Strings.parseInt(strings[i], -1);
            }
        }
        for (int i = 0; i < sides.length; i++) {
            int side = sides[i];
            Item item = content.items().find(it -> {
                return it.id == side;
            });
            if (side != -1 && item != null) {
                Draw.color(item.color);
                Draw.rect(itemRegion, x, y, i * 90);
            } else {
                Draw.rect(crossRegion, x, y, i * 90);
            }
        }
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    public class SmartSorterBuild extends Building {
        final int[] sides = new int[4];
        Table tu, tl, td, tr = td = tu = tl = null;

        public SmartSorterBuild() {
            Arrays.fill(sides, -1);
        }

        public void configSide(int side, int item) {
            sides[side] = item;
        }

        public String getConfig() {
            return Seq.<Integer>withArrays(sides).toString(" ");
        }

        @Override
        public void draw() {
            super.draw();
            for (int i = 0; i < sides.length; i++) {
                int side = sides[i];
                if (side == -1 || content.item(side) == null) {
                    Draw.rect(crossRegion, x, y, i * 90f);
                } else {
                    Draw.color(content.item(side).color);
                    Draw.rect(itemRegion, x, y, i * 90f);
                    Draw.color();
                }

            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            Building to = getTileTarget(item, source, false);

            return to != null && to.acceptItem(this, item) && to.team == team;
        }

        @Override
        public void handleItem(Building source, Item item) {
            getTileTarget(item, source, true).handleItem(this, item);
        }

        public boolean isSame(Building other) {
            return other != null && other.block.instantTransfer;
        }

        private boolean canAccept(Building building, Item item) {
            int dir = building.relativeTo(this);
            return !invert == (sides[Mathf.mod(dir + 2, 4)] == item.id);
        }

        public Building getTileTarget(Item item, Building source, boolean flip) {
            int dir = source.relativeTo(tile.x, tile.y);
            if (dir == -1) return null;
            Building to;
            Building a = nearby(Mathf.mod(dir - 1, 4));
            Building b = nearby(Mathf.mod(dir, 4));
            Building c = nearby(Mathf.mod(dir + 1, 4));
            boolean ac = a != null && canAccept(a, item) && !(a.block.instantTransfer && source.block.instantTransfer) &&
                         a.acceptItem(this, item);
            boolean bc = b != null && canAccept(b, item) && !(b.block.instantTransfer && source.block.instantTransfer) &&
                         b.acceptItem(this, item);
            boolean cc = c != null && canAccept(c, item) && !(c.block.instantTransfer && source.block.instantTransfer) &&
                         c.acceptItem(this, item);

            if (ac && !bc && !cc) {
                to = a;
            } else if (bc && !ac && !cc) {
                to = b;
            } else if (cc && !ac && !bc) {
                to = c;
            } else if (!ac && !bc && !cc) {
                return null;
            } else {
                if (ac && bc) {
                    to = rotation % 2 == 0 ? a : b;
                } else if(bc && cc){
                    to = rotation % 2 == 0 ? b : c;
                } else {
                    to = rotation % 2 == 0 ? c : a;
                }
                if (flip)rotation=Mathf.mod(rotation+1,4);
                /*if (rotation == 0) {
                    to = ac ? a : (bc ? b : c);
                    if (flip) this.rotation = (byte) 1;
                } else if (rotation == 1) {
                    to = b;
                    if (flip) this.rotation = (byte) 2;
                } else {
                    to = c;
                    if (flip) this.rotation = (byte) 0;
                }*/
            }
            return to;
        }

        @Override
        public void updateTableAlign(Table t) {
            float addPos = Mathf.ceil(this.block.size / 2f) - 1;
            Vec2 pos = Core.input.mouseScreen((this.x) + addPos - 0.5f, this.y + addPos);
//            t.setSize(this.block.size * 12f);
            t.setPosition(pos.x, pos.y, 0);
        }

        private void addButton(Table t, int dir) {
            TextureAtlas.AtlasRegion cross = Core.atlas.find("cross");
            ImageButton button = new ImageButton(new TextureRegionDrawable(cross));
            button.update(() -> {
                button.getStyle().imageUp = new TextureRegionDrawable(content.item(sides[dir]) == null ? cross : content.item(sides[dir]).fullIcon);
            });
            button.clicked(() -> {
                t.clearChildren();
                ItemSelection.buildTable(t, content.items(), () -> content.item(sides[dir]), newItem -> {
                    sides[dir] = newItem == null ? -1 : newItem.id;
                    configure(sides[0] + " " + sides[1] + " " + sides[2] + " " + sides[3]);
                    t.clearChildren();
                    buildConfiguration(t);
                });
            });
            t.add(button).minSize(button.getMaxWidth());
        }

        @Override
        public void buildConfiguration(Table t) {
            t.add();
            addButton(t, 1);
            t.add().row();
            addButton(t, 2);
            t.add();
            addButton(t, 0);
            t.row();
            t.add();
            addButton(t, 3);
            t.add();
        }

        private void configure() {
            configure(config());
        }

        @Override
        public boolean onConfigureTileTapped(Building other) {
            deselect();
            //                configure(null);
            return this != other;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(sides[0]);
            write.i(sides[1]);
            write.i(sides[2]);
            write.i(sides[3]);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            sides[0] = read.i();
            sides[1] = read.i();
            sides[2] = read.i();
            sides[3] = read.i();
        }
    }
}
