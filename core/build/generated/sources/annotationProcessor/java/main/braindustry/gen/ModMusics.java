package braindustry.gen;

import arc.audio.Music;
import java.lang.Exception;

public class ModMusics {
  public static Music darknessHarmony = new arc.audio.Music();

  public static Music frozenIslands = new arc.audio.Music();

  public static Music stellarSonata = new arc.audio.Music();

  public static void load() throws Exception {
    darknessHarmony=new arc.audio.Music(ModVars.modVars.modAssets.get("music","darknessHarmony.mp3"));;
    frozenIslands=new arc.audio.Music(ModVars.modVars.modAssets.get("music","frozenIslands.mp3"));;
    stellarSonata=new arc.audio.Music(ModVars.modVars.modAssets.get("music","stellarSonata.mp3"));;
  }
}
