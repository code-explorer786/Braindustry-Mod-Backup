package braindustry.world.blocks.logic;

import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import braindustry.gen.ModBuilding;
import braindustry.world.ModBlock;
import braindustry.world.blocks.DebugBlock;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
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
        });
    }

    public LaserRuler(String name) {
        super(name);
        update = true;
        configurable = true;
        config(Integer.class, (LaserRulerBuild build, Integer i) -> {
            build.target = i;
        });
        configClear((LaserRulerBuild build) -> build.target = -1);
        size = 1;
    }

    public class LaserRulerBuild extends ModBuilding implements Ranged {
        int target = -1;

        @Override
        public void draw() {
            super.draw();
            if (!validTarget(target)) return;
            Draw.color(Pal.health);
            Lines.stroke(2.0f);
            Tile tile = Vars.world.tile(target);
            Lines.line(tile.drawx(), tile.drawy(), x, y);

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
            configure(lastTaped.pos());
            return false;
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
            return pos != -1 && Vars.world.tile(pos) != null;
        }

        private float dstToTarget() {
            if (!validTarget(target)) return -1;
            return Vars.world.tile(target).dst(tile);
        }
    }
}
