package braindustry;

import acontent.ui.*;
import arc.Core;
import arc.Events;
import arc.graphics.g2d.TextureRegion;
import arc.util.CommandHandler;
import arc.util.Log;
import braindustry.annotations.BDAnnotations;
import braindustry.audio.ModAudio;
import braindustry.gen.*;
import braindustry.graphics.ModShaders;
import braindustry.input.ModDesktopInput;
import braindustry.input.ModMobileInput;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.MappableContent;
import mindustry.game.EventType.ClientLoadEvent;
import mindustry.input.DesktopInput;
import mindustry.input.MobileInput;
import mma.MMAMod;
import mma.ModListener;
import mindustry.annotations.Annotations;
import mma.annotations.*;

import static braindustry.BDVars.*;
import static mindustry.Vars.*;

@ModAnnotations.ModAssetsAnnotation
@ModAnnotations.DependenciesAnnotation
public class BraindustryMod extends MMAMod {
    public BraindustryMod() {
        super();
        modLog("DepValid");
        if (!BDDependencies.valid()) return;
        modLog("Creating start");
        BDEntityMapping.init();
        BDCall.registerPackets();
        modInfo = Vars.mods.getMod(getClass());
        BDVars.load();
        BDLogicIO.init();
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

    @Override
    public void registerServerCommands(CommandHandler handler) {
        modLog("Dep.registerServerCommands");
        if (!BDDependencies.valid()) return;
        super.registerServerCommands(handler);
        BDVars.netServer.registerCommands(handler);
    }

    @Override
    public void registerClientCommands(CommandHandler handler) {
        modLog("Dep.registerClientCommands");
        if (!BDDependencies.valid()) return;
        super.registerClientCommands(handler);
        BDVars.netClient.registerCommands(handler);
    }

    @Override
    protected void modContent(Content content) {
        super.modContent(content);
        if (content instanceof MappableContent){
            BDContentRegions.loadRegions((MappableContent) content);
        }
    }

    public void init() {
        modLog("Dep.init");
        if (!BDDependencies.valid()) return;
        if (!loaded) return;
        AdvancedContentInfoDialog.init();
        modLog("init start");
        super.init();
        if (neededInit) listener.init();
        modLog("init end");
    }

    public void loadContent() {
        modLog("Dep.loadContent");
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
            inTry(BDSounds::load);
            inTry(BDMusics::load);
        }
        super.loadContent();
        loaded = true;
        modLog("loadContent end");
    }
}
