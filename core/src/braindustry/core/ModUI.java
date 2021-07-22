package braindustry.core;

import ModVars.Classes.UI.Cheat.ModCheatMenu;
import ModVars.Classes.UI.CheatUI;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.ApplicationListener;
import arc.Core;
import arc.util.Disposable;
import arc.util.Log;
import braindustry.ModListener;
import braindustry.gen.StealthUnitc;
import braindustry.input.ModBinding;
import braindustry.input.ModKeyBinds;
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
        keyBinds = new ModKeyBinds();
        keyBinds.setDefaults(ModBinding.values());
        keyBinds.load();
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
        controls = new ModControlsDialog();
        otherSettingsDialog = new ModOtherSettingsDialog();
        settingsDialog = new ModSettingsDialog();
    }

    @Override
    public void update() {
        if (!inited)return;
        boolean noDialog = !Core.scene.hasDialog();
        boolean inGame = Vars.state.isGame();

        boolean inMenu = Vars.state.isMenu() || !ui.planet.isShown();
        if (!controls.isShown()) {
            if (keyBinds.keyTap(show_unit_dialog) && noDialog && inGame) {
                openUnitChooseDialog();
            } else if (keyBinds.keyTap(show_team_dialog) && noDialog && inGame) {
                openTeamChooseDialog();
            } else if (keyBinds.keyTap(show_unlock_dialog) && !inMenu) {
                openUnlockContentDialog();
            } else if (keyBinds.keyTap(show_item_manager_dialog) && noDialog) {
                openModCheatItemsMenu();
            } else if (keyBinds.keyTap(show_rules_edit_dialog) && inGame && noDialog) {
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
