package braindustry.gen;

import arc.func.Prov;
import arc.struct.ObjectMap;

public class ModEntityMappingG {
  public static Prov[] idMap = new Prov[256];

  public static ObjectMap<String, Prov> nameMap = new ObjectMap<>();

  static {
  }

  public static Prov map(int id) {
    return idMap[id];
  }

  public static Prov map(String name) {
    return nameMap.get(name);
  }
}
