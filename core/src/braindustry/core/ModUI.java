package braindustry.core;

import ModVars.Classes.UI.Cheat.ModCheatMenu;
import ModVars.Classes.UI.CheatUI;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import ModVars.modVars;
import arc.ApplicationListener;
import arc.Core;
import arc.KeyBinds;
import arc.util.Disposable;
import arc.util.Log;
import arc.util.Time;
import braindustry.ModListener;
import braindustry.gen.StealthUnitc;
import braindustry.input.ModBinding;
import braindustry.ui.AdvancedContentInfoDialog;
import braindustry.ui.ModStyles;
import braindustry.ui.dialogs.BackgroundStyle;
import braindustry.ui.dialogs.ModColorPicker;
import braindustry.ui.fragments.ModHudFragment;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

import static ModVars.Classes.UI.CheatUI.*;
import static ModVars.modVars.*;
import static arc.Core.settings;
import static braindustry.input.ModBinding.*;
import static mindustry.Vars.headless;
import static mindustry.Vars.ui;

public class ModUI implements Disposable, ApplicationListener {
    static {
        //x axis or not
        ModMenuFragment.xAxis(true);
        //wave size in pixels
        ModMenuFragment.pixels(3f);
        //wave force
        ModMenuFragment.otherAxisMul(50);
        //waves speed
        ModMenuFragment.timeScl(0.1f);

    }

    public ModColorPicker colorPicker;
    public BackgroundStyle backgroundStyle;
    private boolean inited=false;

    public ModUI() {
        Time.mark();
        KeyBinds.KeyBind[] keyBinds = Core.keybinds.getKeybinds();
        KeyBinds.KeyBind[] modBindings = ModBinding.values();
        KeyBinds.KeyBind[] defs = new KeyBinds.KeyBind[keyBinds.length + modBindings.length];
        for (int i = 0; i < defs.length; i++) {
            if (i<keyBinds.length){
                defs[i]=keyBinds[i];
            } else {
                defs[i]=modBindings[i-keyBinds.length];
            }
        }
        Log.info("[Braindustry]Time to combine arrays: @ms",Time.elapsed());
        Core.keybinds.setDefaults(defs);
        settings.load();
    }

    @Override
    public void init() {
        if (headless) return;
        inited=true;
        ModStyles.load();
//        settings.put("uiscalechanged", false);
        AdvancedContentInfoDialog.init();
        ModMenuFragment.init();
        ModHudFragment.init();
        new ModCheatMenu((table) -> {
            table.button("@cheat-menu.title", () -> {
                BaseDialog dialog = new BaseDialog("@cheat-menu.title");
                dialog.cont.table((t) -> {
                    t.defaults().size(280.0F, 60.0F);
                    t.button("@cheat-menu.change-team", CheatUI::openTeamChooseDialog).growX().row();
                    t.button("@cheat-menu.change-unit", CheatUI::openUnitChooseDialog).growX().row();
                    if (!Vars.net.client())
                        t.button("@cheat-menu.edit-rules", CheatUI::openRulesEditDialog).growX().row();
                    t.button("@cheat-menu.items-manager", CheatUI::openModCheatItemsMenu).growX().row();
                    t.button("@cheat-menu.unlock-content", CheatUI::openUnlockContentDialog).growX().row();
                });
                dialog.addCloseListener();
                dialog.addCloseButton();
                dialog.show();

            }).size(280.0f / 2f, 60.0F);
            table.visibility = () -> CheatUI.visibility.get();
        });

        colorPicker = new ModColorPicker();
        backgroundStyle = new BackgroundStyle();
        otherSettingsDialog = new ModOtherSettingsDialog();
        settingsDialog = new ModSettingsDialog();
    }

    @Override
    public void update() {
        if (!inited)return;
        boolean noDialog = !Core.scene.hasDialog();
        boolean inGame = Vars.state.isGame();

        boolean inMenu = Vars.state.isMenu() || !ui.planet.isShown();
        if (!ui.controls.isShown()) {
            if (Core.input.keyTap(show_unit_dialog) && noDialog && inGame) {
                openUnitChooseDialog();
            } else if (Core.input.keyTap(show_team_dialog) && noDialog && inGame) {
                openTeamChooseDialog();
            } else if (Core.input.keyTap(show_unlock_dialog) && !inMenu) {
                openUnlockContentDialog();
            } else if (Core.input.keyTap(show_item_manager_dialog) && noDialog) {
                openModCheatItemsMenu();
            } else if (Core.input.keyTap(show_rules_edit_dialog) && inGame && noDialog) {
                openRulesEditDialog();
            }
        }
        if (inGame && Vars.state.isPaused() && Vars.player.unit() instanceof StealthUnitc) {
            StealthUnitc unit = (StealthUnitc) Vars.player.unit();
            unit.updateStealthStatus();
        }
    }

    @Override
    public void dispose() {
    }
}
