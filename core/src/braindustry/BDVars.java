package braindustry;

import arc.Core;
import arc.Events;
import arc.util.Log;
import arc.util.Strings;
import braindustry.core.ModLogic;
import braindustry.core.ModNetClient;
import braindustry.core.ModUI;
import braindustry.customArc.ModSettings;
import braindustry.gen.ModNetServer;
import braindustry.ui.dialogs.ModOtherSettingsDialog;
import braindustry.ui.dialogs.ModSettingsDialog;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.mod.Mods;

import static mindustry.Vars.headless;
import static mindustry.Vars.ui;

public class BDVars {
    public static ModSettings settings;
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
    public static boolean neededInit = true;


    public static void init() {
    }

    public static void load() {
        ModListener.load();
        settings = new ModSettings();
        if (!headless) listener.add(modUI = new ModUI());
        listener.add(netClient = new ModNetClient());
        listener.add(netServer = new ModNetServer());
        listener.add(logic = new ModLogic());
    }

    public static boolean showCheatMenu() {
//        if (Vars.player.isLocal()) return ui.hudfrag.shown;
        return ui.hudfrag.shown && netClient.showCheatMenu();
    }

    public static String modName() {
        return modInfo == null ? "no name" : modInfo.name;
    }

    public static void inTry(ThrowableRunnable runnable) {
        try {
            runnable.run();
        } catch (Exception ex) {
            showException(ex);
        }
    }

    public static void checkTranslate(UnlockableContent content) {
        content.localizedName = Core.bundle.get(content.getContentType() + "." + content.name + ".name", content.localizedName);
        content.description = Core.bundle.get(content.getContentType() + "." + content.name + ".description", content.description);
    }

    public static void addResearch(UnlockableContent parentContent, UnlockableContent unlock) {
        TechTree.TechNode node = new TechTree.TechNode(TechTree.getNotNull(parentContent), unlock, unlock.researchRequirements());
        TechTree.TechNode parent = TechTree.getNotNull(parentContent);
        if (parent == null) {
            showException(new IllegalArgumentException("Content '" + parentContent.name + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched."));
//            throw new IllegalArgumentException("Content '" + researchName + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched.");
        } else {
            if (!parent.children.contains(node)) {
                parent.children.add(node);
            }

            node.parent = parent;
        }
    }

    public static void inspectBuilding() {

    }

    public static void addResearch(String researchName, UnlockableContent unlock) {
        TechTree.TechNode node = new TechTree.TechNode(null, unlock, unlock.researchRequirements());
        TechTree.TechNode parent = TechTree.all.find((t) -> {
            return t.content.name.equals(researchName) || t.content.name.equals(fullName(researchName));
        });

        if (parent == null) {
            showException(new IllegalArgumentException("Content '" + researchName + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched."));
//            throw new IllegalArgumentException("Content '" + researchName + "' isn't in the tech tree, but '" + unlock.name + "' requires it to be researched.");
        } else {
            if (!parent.children.contains(node)) {
                parent.children.add(node);
            }

            node.parent = parent;
        }
    }

    public static String fullName(String name) {
        if (packSprites) return name;
        return Strings.format("@-@", modInfo == null ? "braindustry" : modInfo.name, name);
    }


    public static String getTranslateName(String name) {
        return Strings.format("@.@", modInfo.name, name);
    }

    public static void showException(Exception exception) {
        Log.err(exception);
        if (settings!=null &&!settings.debug())return;
        try {
            Vars.ui.showException(Strings.format("@: error", modInfo.meta.displayName), exception);
        } catch (NullPointerException n) {
            Events.on(EventType.ClientLoadEvent.class, event -> {
                ModUI.showExceptionDialog(Strings.format("@: error", modInfo == null ? null : modInfo.meta.displayName), exception);
            });
        }
    }

    public static void modLog(String text, Object... args) {
        Log.info("[@] @", modInfo == null ? "braindustry-java" : modInfo.name, Strings.format(text, args));
    }

    public interface ThrowableRunnable {
        void run() throws Exception;
    }
}
