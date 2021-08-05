package braindustry.tools;

import arc.Core;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.annotations.ModAnnotations;
import braindustry.gen.BackgroundStyle;
import braindustry.io.ZelReads;
import braindustry.io.ZelWrites;
import mindustry.type.UnitType;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;

import static mindustry.Vars.content;

public class BackgroundConfig {
    @ModAnnotations.BackgroundStyleSources()
  static final UnitType unit = null;
    @ModAnnotations.BackgroundStyleSources(setting = true)
  static final boolean
            useStyles = false;
    @ModAnnotations.BackgroundStyleSources
    static final boolean
            useSeed = false,
            hasFloor3 = false,
            hasFloor4 = false;
    @ModAnnotations.BackgroundStyleSources
    static final int seed = -1;
    @ModAnnotations.BackgroundStyleSources()
    static final UnitMovingType unitMovingType = UnitMovingType.flying;
    @ModAnnotations.BackgroundStyleSources()
    static final ViewType
            ore = ViewType.classic,
            heat = ViewType.classic,
            units = ViewType.classic;
    @ModAnnotations.BackgroundStyleSources()
    static final State
            tendrils = State.random,
            tech = State.random;
    @ModAnnotations.BackgroundStyleSources()
   static final Floor  floor1 = null, floor2 =null, floor3 = null, floor4 = null;
    @ModAnnotations.BackgroundStyleSources()
   static final StaticWall wall1 = null, wall2 = null;
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
        ;
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
        classic,
        custom,
        disable;

        public boolean classic() {
            return this == classic;
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
