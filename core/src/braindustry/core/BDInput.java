package braindustry.core;

import arc.*;
import arc.input.*;
import arc.input.GestureDetector.*;
import arc.math.geom.*;
import braindustry.gen.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.gen.*;
import mindustry.input.*;
import mindustry.world.*;

import static mindustry.Vars.*;

public class BDInput implements GestureListener{
    static GestureDetector detector;

    public static void init(){
        detector = new GestureDetector(20, 0.5f, 0.3f, 0.15f, new BDInput());
        Core.input.getInputProcessors().insert(0, detector);
    }

    int tileX(float cursorX){
        Vec2 vec = Core.input.mouseWorld(cursorX, 0);
        if(control.input.selectedBlock()){
            vec.sub(control.input.block.offset, control.input.block.offset);
        }
        return World.toTile(vec.x);
    }

    int tileY(float cursorY){
        Vec2 vec = Core.input.mouseWorld(0, cursorY);
        if(control.input.selectedBlock()){
            vec.sub(control.input.block.offset, control.input.block.offset);
        }
        return World.toTile(vec.y);
    }

    protected Tile tileAt(float x, float y){
        return world.tile(tileX(x), tileY(y));
    }

    @Override
    public boolean longPress(float x, float y){
        if(!(control.input instanceof MobileInput mobileInput)) return false;
        if(!state.isMenu() && !player.dead()){
//            Tile cursor = this.tileAt(x, y);
            if(!Core.scene.hasMouse(x, y) && !mobileInput.schematicMode){
                if(mobileInput.mode == PlaceMode.none){
                    Vec2 pos = Core.input.mouseWorld(x, y);
                    Unit target = player.unit();
                    float epsilon = 2f - (renderer.getDisplayScale() - renderer.minScale()) / (renderer.maxScale() - renderer.minScale());
                    if(target instanceof Stealthc stealthc && pos.epsilonEquals(target.x, target.y, Math.min(4f * epsilon, target.hitSize()))){
                        mobileInput.detector.cancel();
                        stealthc.longPress(true);
                    }
                }
            }
        }
        return false;
    }
}
