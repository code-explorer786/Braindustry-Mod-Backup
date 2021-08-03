package braindustry.world.meta;

import acontent.world.meta.AStat;
import acontent.world.meta.AStatCat;
import mindustry.world.meta.StatCat;

public class BDStat {
    public static final AStat recipes=AStat.get("recipes", AStatCat.get(StatCat.function)),
            maxConnections=AStat.get("maxConnections", AStatCat.get(StatCat.function));
//    maxConnections(AStatCat.function),
//    recipes(AStatCat.function),
}
