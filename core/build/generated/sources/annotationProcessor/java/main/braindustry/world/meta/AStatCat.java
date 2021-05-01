package braindustry.world.meta;

import arc.Core;
import arc.struct.Seq;

import mindustry.world.meta.StatCat;

public enum AStatCat {
  general,

  power,

  liquids,

  items,

  crafting,

  function,

  optional,

  gasses;

  public String localized() {
    {
        return Core.bundle.get("category." + name());
    }
  }

  public static AStatCat fromExist(StatCat stat) {
    {
        return Seq.with(values()).find((v)->v.name().equals(stat.name()));
    }
  }
}
