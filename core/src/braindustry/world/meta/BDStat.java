package braindustry.world.meta;

import acontent.world.meta.AStat;
import acontent.world.meta.AStatCat;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatCat;

public class BDStat {
    public static final AStat
            recipes=AStat.get("recipes", StatCat.function),
            maxConnections=AStat.get("maxConnections", StatCat.function),
            rotatorsCount =AStat.get("rotatorsCount", StatCat.general, Stat.size.ordinal() + 1)
                    ;
}
