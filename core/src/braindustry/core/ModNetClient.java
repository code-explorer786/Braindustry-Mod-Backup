package braindustry.core;

import braindustry.BDVars;

import arc.ApplicationListener;
import arc.graphics.Color;
import arc.util.CommandHandler;
import arc.util.io.ReusableByteInStream;
import braindustry.annotations.BDAnnotations;
import braindustry.graphics.Drawm;
import mindustry.annotations.Annotations;
import mindustry.gen.*;
import mindustry.annotations.Annotations;
import mma.customArc.cfunc.Couple;

import java.io.DataInputStream;

import static mindustry.Vars.*;

public class ModNetClient implements ApplicationListener {
    protected ReusableByteInStream byteStream = new ReusableByteInStream();
    protected DataInputStream dataStream = new DataInputStream(byteStream);

    @Annotations.Remote(targets = Annotations.Loc.both, called = Annotations.Loc.client, forward = true)
    public static void showTeleportCircles(Player player,float x, float y, float radius, Color first, Color second, float fromRadius,float toRadius){
        if (headless || first==null|| second==null)return;
        Drawm.teleportCircles(x,y,radius,first,second, Couple.of(fromRadius,toRadius));
    }

    @Annotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.client)
    public static void setServerCheatLevel(int level) {
        BDVars.settings.cheatLevelServer(CheatLevel.all[level % CheatLevel.all.length]);
    }

    public void registerCommands(CommandHandler handler) {
    }

    public boolean showCheatMenu() {
        boolean result = true;
        CheatLevel cheatLevel;
//        Log.info("@ @ @", net.client(),net.server(),net.active());
        if (net.client()) {
            cheatLevel = BDVars.settings.cheatLevelServer();
//            Log.info("on server: @", cheatLevel);
        }else if(net.server() && net.active()){
            cheatLevel= BDVars.settings.cheatLevel();
        } else {
            return BDVars.settings.cheating();
//            Log.info("c-@-@", net.client(),net.server());
        }
        return cheatLevel.cheating(player);
    }

    @Override
    public void update() {

    }
}
