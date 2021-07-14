package braindustry.core;

import ModVars.ModEnums;
import ModVars.modVars;
import arc.ApplicationListener;
import arc.Events;
import arc.graphics.Color;
import arc.util.CommandHandler;
import arc.util.Log;
import arc.util.io.Reads;
import arc.util.io.ReusableByteInStream;
import braindustry.annotations.ModAnnotations;
import braindustry.cfunc.Couple;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.core.NetClient;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.net.Packets;
import mindustry.net.ValidateException;
import mindustry.world.Tile;
import mindustry.world.modules.ItemModule;
import braindustry.gen.ModEntityMapping;
import mindustryAddition.graphics.ModDraw;

import java.io.DataInputStream;
import java.io.IOException;

import static ModVars.modVars.*;
import static mindustry.Vars.*;
import static mindustry.Vars.netClient;
import static mindustry.Vars.netServer;

public class ModNetClient implements ApplicationListener {
    protected ReusableByteInStream byteStream = new ReusableByteInStream();
    protected DataInputStream dataStream = new DataInputStream(byteStream);

    @ModAnnotations.Remote(targets = Annotations.Loc.both, called = Annotations.Loc.client, forward = true)
    public static void showTeleportCircles(Player player,float x, float y, float radius, Color first, Color second, float fromRadius,float toRadius){
        if (headless || first==null|| second==null)return;
        ModDraw.teleportCircles(x,y,radius,first,second,Couple.of(fromRadius,toRadius));
    }

    @ModAnnotations.Remote(targets = Annotations.Loc.server, called = Annotations.Loc.client)
    public static void setServerCheatLevel(int level) {
        ModEnums.CheatLevel[] values = ModEnums.CheatLevel.values();
        modVars.settings.cheatLevelServer(values[level % values.length]);
    }

    public void registerCommands(CommandHandler handler) {
    }

    public boolean showCheatMenu() {
        boolean result = true;
        ModEnums.CheatLevel cheatLevel = modVars.settings.cheating() ? ModEnums.CheatLevel.onlyAdmins : ModEnums.CheatLevel.off;
//        Log.info("@ @ @", net.client(),net.server(),net.active());
        if (net.client()) {
            cheatLevel = modVars.settings.cheatLevelServer();
//            Log.info("on server: @", cheatLevel);
        }else if(net.server() && net.active()){
            cheatLevel=modVars.settings.cheatLevel();
        } else {
            return modVars.settings.cheating();
//            Log.info("c-@-@", net.client(),net.server());
        }
        switch (cheatLevel) {
            case off:
                result = false;
                break;

            case onlyAdmins:
                result = player.admin();
                break;
            case all:
                result = true;
                break;

        }
        return result;
    }

    @Override
    public void update() {

    }
}
