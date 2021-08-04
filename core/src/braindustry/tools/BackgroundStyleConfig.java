package braindustry.tools;

import braindustry.annotations.ModAnnotations;
import braindustry.cfunc.BackgroundUnitMovingType;
import mindustry.type.UnitType;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;

import static mindustry.Vars.content;

public class BackgroundStyleConfig {
    @ModAnnotations.BackgroundStyleSources(castMethodGet = "content.unit(@)",castMethodSet = "@.id",clazz = "UnitType")
    final int unit = -1;
    @ModAnnotations.BackgroundStyleSources
    private final boolean useSeed = false, useStyles = false,hasUnits=true,useWalls=true;
    @ModAnnotations.BackgroundStyleSources
    private final int seed = -1;
    @ModAnnotations.BackgroundStyleSources(castMethodGet = "BackgroundUnitMovingType.values()[@]",castMethodSet = "@.ordinal()",clazz = "BackgroundUnitMovingType", noDefError =true)
    private final int unitMovingType = BackgroundUnitMovingType.flying.ordinal();
    @ModAnnotations.BackgroundStyleSources(castMethodGet = "(Floor)content.block(@)",castMethodSet = "@.id",clazz = "Floor")
    private final int floor1 = -1,  floor2 = -1,floor3=-1,floor4=-1;
    @ModAnnotations.BackgroundStyleSources(castMethodGet = "(StaticWall)content.block(@)",castMethodSet = "@.id",clazz = "StaticWall")
    private final int wall1 = -1, wall2 = -1;
    static {
        UnitType u;
        StaticWall w;
        Floor f;
        if (content != null) {

        }
        BackgroundUnitMovingType.class.getName();
    }
}
