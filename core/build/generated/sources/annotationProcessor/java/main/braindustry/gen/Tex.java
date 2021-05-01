package braindustry.gen;

import arc.scene.style.TextureRegionDrawable;

public class Tex {
  public static TextureRegionDrawable clear;

  public static void load() {
    clear = (arc.scene.style.TextureRegionDrawable)arc.Core.atlas.drawable("clear");
  }
}
