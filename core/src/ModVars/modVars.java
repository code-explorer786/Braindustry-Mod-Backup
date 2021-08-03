package ModVars;

import ModVars.Classes.ModAssets;
import ModVars.Classes.ModSettings;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.ReusableByteInStream;
import braindustry.BraindustryMod;
import braindustry.ModListener;
import braindustry.core.ModLogic;
import braindustry.core.ModNetClient;
import braindustry.core.ModUI;
import braindustry.gen.ModBuilding;
import braindustry.gen.ModNetServer;
import mindustry.ClientLauncher;
import mindustry.ctype.Content;
import mindustry.gen.Building;
import mindustry.gen.EntityMapping;
import mindustry.mod.Mods;

import java.io.DataInputStream;
import java.lang.reflect.Constructor;

import static mindustry.Vars.*;

public class modVars {
    public static ModSettings settings;
    public static ModAssets modAssets;
    public static Mods.LoadedMod modInfo;
    public static ModOtherSettingsDialog otherSettingsDialog;
    public static ModSettingsDialog settingsDialog;
    public static ModNetClient netClient;
    public static ModNetServer netServer;
    public static ModUI modUI;
    public static ModLogic logic;
    public static ModListener listener;
    public static BraindustryMod mod;
    public static boolean renderUpdate;
    public static boolean loaded = false;
    public static boolean packSprites;
    public static boolean neededInit =true;


    public static void init() {
    }

    public static void load() {
        ModListener.load();
        settings = new ModSettings();
        modAssets = new ModAssets();
       if(!headless) listener.add(modUI = new ModUI());
        listener.add(netClient = new ModNetClient());
        listener.add(netServer = new ModNetServer());
        listener.add(logic = new ModLogic());
    }

    public static boolean showCheatMenu() {
//        if (Vars.player.isLocal()) return ui.hudfrag.shown;
        return ui.hudfrag.shown && netClient.showCheatMenu();
    }

    public static String modName() {
        return modInfo==null?"no name":modInfo.name;
    }
}
