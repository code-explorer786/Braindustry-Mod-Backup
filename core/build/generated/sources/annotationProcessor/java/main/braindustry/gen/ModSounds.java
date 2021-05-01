package braindustry.gen;

import arc.audio.Sound;
import java.lang.Exception;

public class ModSounds {
  public static Sound blackhole = new arc.audio.Sound();

  public static Sound dendriteCharge = new arc.audio.Sound();

  public static Sound dendriteShoot = new arc.audio.Sound();

  public static Sound electronCharge = new arc.audio.Sound();

  public static Sound electronShoot = new arc.audio.Sound();

  public static Sound exploding1 = new arc.audio.Sound();

  public static Sound gemCharge = new arc.audio.Sound();

  public static Sound gemShoot = new arc.audio.Sound();

  public static Sound railgun2 = new arc.audio.Sound();

  public static Sound railgun3 = new arc.audio.Sound();

  public static Sound shooting1 = new arc.audio.Sound();

  public static void load() throws Exception {
    blackhole=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","blackhole.ogg"));;
    dendriteCharge=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","dendriteCharge.ogg"));;
    dendriteShoot=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","dendriteShoot.ogg"));;
    electronCharge=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","electronCharge.ogg"));;
    electronShoot=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","electronShoot.ogg"));;
    exploding1=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","exploding1.ogg"));;
    gemCharge=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","gemCharge.ogg"));;
    gemShoot=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","gemShoot.ogg"));;
    railgun2=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","railgun2.ogg"));;
    railgun3=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","railgun3.ogg"));;
    shooting1=new arc.audio.Sound(ModVars.modVars.modAssets.get("sounds","shooting1.ogg"));;
  }
}
