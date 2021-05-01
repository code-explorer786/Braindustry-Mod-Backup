package braindustry.gen;

import arc.struct.Seq;

public class AssetsFolders {
  public static Seq<String> getFolders() {
    return Seq.with(".DS_Store","bundles","icon.png","maps","music","schematics","shaders","sounds","sprites","sprites-override");
  }
}
