package braindustry.core;

import arc.func.*;
import mindustry.gen.*;

public enum CheatLevel{
    disable(p->false),
    onlyAdmins(p->p.admin),
    enable(p->true),
    ;
    public final Boolf<Player> filter;
    public static final CheatLevel[] all=values();

    CheatLevel(Boolf<Player> filter){
        this.filter = filter;
    }
    public boolean cheating(Player player){
        return filter.get(player);
    }
}
