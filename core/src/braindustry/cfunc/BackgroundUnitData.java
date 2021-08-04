package braindustry.cfunc;

import arc.graphics.Color;
import mindustry.content.Blocks;
import mindustry.graphics.Trail;

public class BackgroundUnitData {
    public Trail tleft, tright;
    public Color trailColor;
    public void reset(){
        clear();
        set(new Trail(1), new Trail(1), Blocks.water.mapColor.cpy().mul(1.5f));
    }

    public BackgroundUnitData() {
        reset();
    }

    public void set(Trail tleft, Trail tright, Color trailColor) {
        this.tleft = tleft;
        this.tright = tright;
        this.trailColor = trailColor;
    }

    public void clear() {
        tleft=null;
        tright=null;
        trailColor=null;
    }
}
