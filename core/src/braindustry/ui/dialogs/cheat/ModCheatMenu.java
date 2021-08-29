package braindustry.ui.dialogs.cheat;

import arc.assets.AssetDescriptor;
import arc.assets.Loadable;
import arc.struct.Seq;
import braindustry.core.CheatUI;
import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Table;
import mindustry.ClientLauncher;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Styles;

import static braindustry.BDVars.modLog;
import static braindustry.BDVars.settings;

public class ModCheatMenu {

    Cons<Table> cons;
    boolean add = false;

    public ModCheatMenu(Cons<Table> cons) {
//        consSeq.add(cons);

        this.cons = cons;
        loadEvent();
    }

    public boolean isPlay() {
        return Vars.state.isPlaying() || Vars.state.isPaused();
    }

    public boolean canAdd() {
        return isPlay() || settings.cheating();
    }

    private void loadEvent() {
        modLog("cheatMenu.ClientLoadEvent_PRE start");
        Table table = new Table(Styles.none);
        table.touchable = Touchable.enabled;
        table.visibility = () -> CheatUI.visibility.get() && Vars.state.isGame();
        cons.get(table);
        table.left().bottom();
        int timeControlOffset = enabledMod("time-control") ? 62 : 0;
        int testUtilsOffsetFull= enabledMod("test-utils")? 60 * 3:0;
        int testUtilsOffset= enabledMod("test-utils")? 60 * 2:0;
        table.marginBottom(timeControlOffset+testUtilsOffsetFull);
        /*table.update(()->{
            Vars.mods.getScripts().readString()
            table.marginBottom(timeControlOffset+testUtilsOffsetFull);
        });*/
        modLog("cheatMenu.ClientLoadEvent_PRE end");
        Events.on(EventType.ClientLoadEvent.class,e->{
            modLog("cheatMenu.ClientLoadEvent start");
            Vars.ui.hudGroup.addChild(table);
            modLog("cheatMenu.ClientLoadEvent end");
        });
    }

    private boolean enabledMod(String mod) {
        return Core.settings.getBool("mod-" + mod + "-enabled", false) && Vars.mods!=null && Vars.mods.getMod(mod)!=null;
    }
}
