package braindustry.tools;

import arc.Core;
import arc.struct.Seq;
import braindustry.annotations.ModAnnotations;
import braindustry.gen.BackgroundStyle;
import braindustry.io.ZelReads;
import braindustry.io.ZelWrites;
import mindustry.type.UnitType;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;

public class BackgroundConfig {
    @ModAnnotations.BackgroundStyleSources()
  static final UnitType unit = null;
    @ModAnnotations.BackgroundStyleSources(keyOnly = true)
    String wall,floor;
    @ModAnnotations.BackgroundStyleSources(setting = true)
  static final boolean
            useStyles = false;
    @ModAnnotations.BackgroundStyleSources
    static final boolean
            useWorldSeed = false,useHeatSeed=false,useOreSeed=false,useTendrilsSeed=false,useTechSeed=false,
            hasFloor3 = false,
            hasFloor4 = false,
            hasWall3 = false,
            hasWall4 = false;
    @ModAnnotations.BackgroundStyleSources
    static final int worldSeed = -1,heatSeed=-1,oreSeed=-1;
    @ModAnnotations.BackgroundStyleSources
    static final float heatValue = 0f;
    @ModAnnotations.BackgroundStyleSources()
    static final UnitMovingType unitMovingType = UnitMovingType.flying;
    @ModAnnotations.BackgroundStyleSources()
    static final ViewType
            ore = ViewType.random,
            heat = ViewType.random,
            units = ViewType.random;
    @ModAnnotations.BackgroundStyleSources()
    static final State
            tendrils = State.random,
            tech = State.random;
    @ModAnnotations.BackgroundStyleSources()
   static final Floor  floor1 = null, floor2 =null, floor3 = null, floor4 = null;
    @ModAnnotations.BackgroundStyleSources()
    static final StaticWall wall1 = null, wall2 = null,wall3=null,wall4=null;
    @ModAnnotations.BackgroundStyleSources()
    static final OreBlock ore1 = null, ore2 = null;
    @ModAnnotations.BackgroundStyleSources()
    public static Seq<BackgroundStyle> getStyles(){
        Seq<BackgroundStyle> backgroundStyles = new Seq<>();
        Object obj = Core.settings.get("braindustry.background.styles", new byte[0]);
        if (obj instanceof byte[]){
            byte[] bytes= (byte[]) obj;
            if (bytes.length>0) {
                ZelReads read = new ZelReads(bytes);
                int size = read.i();
                for (int i = 0; i < size; i++) {
                    backgroundStyles.add(BackgroundStyle.read(read.str(),read));
//                backgroundStyles.add(BackgroundStyle.read());
                }
            }
        } else{
            Core.settings.put("braindustry.background.styles",new byte[0]);
        }
        return backgroundStyles;
    }
    @ModAnnotations.BackgroundStyleSources()
    public static void saveStyles(Seq<BackgroundStyle> backgroundStyles){
        ZelWrites write = new ZelWrites();
        write.i(backgroundStyles.size);
        for (BackgroundStyle style : backgroundStyles) {
            write.str(style.name());
            style.write(write);
        }
        Core.settings.put("braindustry.background.styles",write.getBytes());
    }
    public enum UnitMovingType {
        flying,
        naval,
        legs,
        ;

        public boolean naval() {
            return this==naval;
        }
        public boolean legs() {
            return this==legs;
        }
        public boolean flying() {
            return this==flying;
        }
    }

    public enum State {
        random,
        enable,
        disable;

        public boolean random() {
            return this == random;
        }

        public boolean enable() {
            return this == enable;
        }

        public boolean disabled() {
            return this == disable;
        }
    }

    public enum ViewType {
        random,
        custom,
        disable;

        public boolean random() {
            return this == random;
        }

        public boolean custom() {
            return this == custom;
        }

        public boolean disabled() {
            return this == disable;
        }

        public boolean enabled() {
            return !disabled();
        }
    }

}
