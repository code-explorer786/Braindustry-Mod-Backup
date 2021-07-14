package braindustry.core;

import ModVars.Classes.UI.Cheat.ModCheatMenu;
import ModVars.Classes.UI.CheatUI;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.Core;
import arc.scene.Group;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Disposable;
import braindustry.ModListener;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.g3d.ModPlanetRenderer;
import braindustry.input.ModBinding;
import braindustry.input.ModDesktopInput;
import braindustry.input.ModKeyBinds;
import braindustry.input.ModMobileInput;
import braindustry.ui.AdvancedContentInfoDialog;
import braindustry.ui.ModStyles;
import braindustry.ui.dialogs.BackgroundStyle;
import braindustry.ui.dialogs.ModColorPicker;
import braindustry.ui.dialogs.ModPlanetDialog;
import braindustry.ui.fragments.ModHudFragment;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

import static ModVars.Classes.UI.CheatUI.*;
import static ModVars.modVars.*;
import static arc.Core.settings;
import static braindustry.input.ModBinding.*;
import static mindustry.Vars.*;

public class ModUI implements Disposable {
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

    public void init() {
        ModStyles.load();
        colorPicker = new ModColorPicker();
        backgroundStyle=new BackgroundStyle();
        if (mobile) {
            control.setInput(new ModMobileInput());
        } else {
            control.setInput(new ModDesktopInput());
        }
        ModHudFragment.init();
        Group nullGroup = new Group() {
        };
        settings.put("uiscalechanged", false);
        ModListener.updaters.add(() -> {
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

        });

        ui.menuGroup.remove();
        ui.menuGroup = new WidgetGroup();
        ui.menuGroup.setFillParent(true);
        ui.menuGroup.touchable = Touchable.childrenOnly;
        ui.menuGroup.visible(() -> {
            return Vars.state.isMenu();
        });
        Core.scene.add(ui.menuGroup);
        ui.menufrag = new ModMenuFragment(nullGroup);

//        Vars.ui.menufrag.
        ui.menufrag.build(ui.menuGroup);
        AdvancedContentInfoDialog.init();
        ui.planet.remove();
        Vars.renderer.planets.dispose();
        Vars.renderer.planets = new ModPlanetRenderer();
        ui.planet = new ModPlanetDialog();
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
        keyBinds = new ModKeyBinds();
        keyBinds.setDefaults(ModBinding.values());
        keyBinds.load();
        controls = new ModControlsDialog();
        otherSettingsDialog = new ModOtherSettingsDialog();
        settingsDialog = new ModSettingsDialog();
    }

    @Override
    public void dispose() {
        if (ui.menufrag instanceof ModMenuFragment) ((ModMenuFragment) ui.menufrag).dispose();
    }
}
