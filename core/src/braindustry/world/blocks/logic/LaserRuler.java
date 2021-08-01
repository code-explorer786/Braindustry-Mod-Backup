package braindustry.world.blocks.logic;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.math.geom.Point2;
import arc.util.Tmp;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.content.ModFx;
import braindustry.gen.ModBuilding;
import braindustry.graphics.ModDraw;
import braindustry.world.ModBlock;
import braindustry.world.blocks.DebugBlock;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.logic.Ranged;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;

public class LaserRuler extends ModBlock implements DebugBlock {
    static Tile lastTaped;

    static {
        Events.on(EventType.TapEvent.class, e -> {
            lastTaped = e.tile;
            Building selectedTile = Vars.control.input.frag.config.getSelectedTile();
            if (selectedTile instanceof LaserRulerBuild) {
                LaserRulerBuild build = (LaserRulerBuild) selectedTile;
                if (build.tile == lastTaped) {
                    build.configure(null);
                    build.deselect();
                } else {
                    build.setTarget(lastTaped.pos());
                }
            }
        });
    }

    boolean laserRuler = false;

    public LaserRuler(String name) {
        super(name);
        update = true;
        this.destructible = true;
        configurable = true;
        this.<Integer, LaserRulerBuild>config(Integer.class, (build, i) -> {
            build.target = i;
        });
        this.<Point2, LaserRulerBuild>config(Point2.class, (tile, i) -> tile.target = Point2.pack(i.x + tile.tileX(), i.y + tile.tileY()));
        this.<LaserRulerBuild>configClear(build -> build.target = -1);
        size = 1;
    }

    public class LaserRulerBuild extends ModBuilding implements Ranged {
        public int target = -1;

        @Override
        public Point2 config() {
            return Point2.unpack(target).sub(tile.x, tile.y);
        }

        @Override
        public void update() {
            super.update();
            if (!validTarget(target)) {
                return;
            }
            Tile target = targetTile();
            Color color = Color.red.cpy().lerp(Color.white, 0.5f);
            ModFx.laserRulerSelected.at(target.drawx(), target.drawy(), 1f, color, target);
            int dx = target.x - tile.x;
            int dy = target.y - tile.y;
            boolean xdir = dy == 0;
            int val = xdir ? dx : dy;
            int mul = val / Math.abs(val);
            for (int i = mul; i != val + mul; i += mul) {
                Tile cur = tile.nearby(xdir ? i : 0, xdir ? 0 : i);
                Tile prev = i == mul ? tile : tile.nearby(xdir ? i - mul : 0, xdir ? 0 : i - mul);
                ModFx.laserRulerLinePart.at((laserRuler ? cur : prev).drawx(), (laserRuler ? cur : prev).drawy(), (cur.angleTo(prev) + 180) % 360, color, laserRuler ? prev : null);
            }

        }

        @Override
        public void draw() {
            super.draw();
            if (!validTarget(target)) return;
            Draw.draw(Layer.flyingUnit + 4, () -> {
                Tile target = targetTile();
                Tmp.v1.trns(tile.angleTo(target), size * tilesize);
                ModDraw.drawLabel(x + Tmp.v1.x, y + Tmp.v1.y, Pal.heal, "" + dstTileToTarget());
            });

        }

        public double sense(LAccess sensor) {
            Tile tile = targetTile();
            switch (sensor) {
                case shootX:
                    return tile == null ? -1 : tile.x;
                case shootY:
                    return tile == null ? -1 : tile.y;
                case shooting:
                    return tile != null ? 1 : 0;
                default:
                    return super.sense(sensor);
            }

        }

        private Tile targetTile() {
            return validTarget(target) ? Vars.world.tile(target) : null;
        }

        @Override
        public Object senseObject(LAccess sensor) {
            return super.senseObject(sensor);
        }

        @Override
        public boolean onConfigureTileTapped(Building other) {
            return lastTaped == tile;
        }

        @Override
        public float range() {
            return dstToTarget();
        }

        private int dstTileToTarget() {
            if (!validTarget(target)) return -1;
            return (int) (dstToTarget() / tilesize);
        }

        private boolean validTarget(int pos) {
            return pos != -1 && Vars.world.tile(pos) != null && (Vars.world.tile(pos).x == tile.x || Vars.world.tile(pos).y == tile.y);
        }

        private float dstToTarget() {
            if (!validTarget(target)) return -1;
            Tile target = Vars.world.tile(this.target);
            return Mathf.dst(target.worldx(), target.worldy(), tile.worldx(), tile.worldy()) - 8f;
        }

        public void setTarget(int target) {
            if (validTarget(target)) {
                this.target = target;
            } else {
                Fx.unitCapKill.at(Vars.world.tile(target));

            }
            ModFx.selectTile.at(Vars.world.tile(target));
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(target);
        }

        @Override
        public byte version() {
            return 1;
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            if (revision == 0) return;
            target = read.i();
        }
    }
}
