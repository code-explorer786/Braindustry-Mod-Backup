package braindustry;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.CommandHandler;
import braindustry.annotations.ModAnnotations;
import braindustry.audio.ModAudio;
import braindustry.core.ModContentLoader;
import braindustry.gen.*;
import braindustry.graphics.ModShaders;
import braindustry.input.ModDesktopInput;
import braindustry.input.ModMobileInput;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.MappableContent;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.input.DesktopInput;
import mindustry.input.MobileInput;
import mindustry.mod.Mod;

import static braindustry.BDVars.*;
import static mindustry.Vars.*;

@ModAnnotations.CashAnnotation1
@ModAnnotations.CashAnnotation2
public class BraindustryMod extends Mod {
    public BraindustryMod() {
        if (!BDDependencies.valid()) return;
        modLog("Creating start");
        ModEntityMapping.init();
        ModCall.registerPackets();
        modInfo = Vars.mods.getMod(getClass());
        BDVars.load();
        ModLogicIO.init();
        ModListener.addRun(() -> {
            boolean modMobile = (control.input instanceof ModMobileInput);
            boolean modDesktop = (control.input instanceof ModDesktopInput);
            boolean mobile = (control.input instanceof MobileInput);
            boolean desktop = (control.input instanceof DesktopInput);
            if (mobile && !modMobile) control.setInput(new ModMobileInput());
            if (desktop && !modDesktop) control.setInput(new ModDesktopInput());
        });

        Events.on(ClientLoadEvent.class, (e) -> {
            ModAudio.reload();
        });
        modLog("Creating end");
    }

    public static TextureRegion getIcon() {
        if (modInfo == null || modInfo.iconTexture == null) return Core.atlas.find("nomap");
        return new TextureRegion(modInfo.iconTexture);
    }

    public static boolean inPackage(String packageName, Object obj) {
        if (packageName == null || obj == null) return false;
        String name;
        try {
            name = obj.getClass().getPackage().getName();
        } catch (Exception e) {
            return false;
        }
        return name.startsWith(packageName + ".");
    }

    @Override
    public void registerServerCommands(CommandHandler handler) {
        if (!BDDependencies.valid()) return;
        BDVars.netServer.registerCommands(handler);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        if (!BDDependencies.valid()) return;
        BDVars.netClient.registerCommands(handler);
    }

    public void init() {
        if (!BDDependencies.valid()) return;
        if (!loaded) return;
        modLog("init start");
        for (Seq<Content> contents : content.getContentMap()) {
            for (Content content : contents) {
                if (content.minfo.mod == modInfo && inPackage("braindustry", content)) {
                    if (content instanceof UnlockableContent) checkTranslate((UnlockableContent) content);
                    if (content instanceof MappableContent && !headless)
                        ModContentRegions.loadRegions((MappableContent) content);
                }
            }
        }
        if (neededInit) listener.init();
        modLog("init end");
    }

    public void loadContent() {
        modInfo = Vars.mods.getMod(this.getClass());
        if (!BDDependencies.valid()) {
            if (modInfo != null) {
                modInfo.missingDependencies.addAll(modInfo.dependencies.select(mod -> !mod.enabled()).map(l -> l.name));
            }
            return;
        }
        if (modInfo.dependencies.count(l -> l.enabled()) != modInfo.dependencies.size) {
            return;
        }
        modLog("loadContent start");
        ModAudio.reload();
        if (!headless) {
            inTry(ModShaders::init);
            inTry(ModSounds::load);
            inTry(ModMusics::load);
        }
        new ModContentLoader((load) -> {
            try {
                load.load();
            } catch (NullPointerException e) {
                if (!headless) showException(e);
            }
        });
        loaded = true;
        modLog("loadContent end");
    }
}
