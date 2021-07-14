package braindustry.core;

import ModVars.Classes.UI.Cheat.ModCheatMenu;
import ModVars.Classes.UI.CheatUI;
import ModVars.Classes.UI.ModControlsDialog;
import ModVars.Classes.UI.settings.ModOtherSettingsDialog;
import ModVars.Classes.UI.settings.ModSettingsDialog;
import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.scene.Group;
import arc.scene.event.Touchable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Dialog;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Seq;
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
import braindustry.ui.dialogs.ModColorPicker;
import braindustry.ui.dialogs.ModPlanetDialog;
import braindustry.ui.fragments.ModHudFragment;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;

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

    public void init() {
        ModStyles.load();
        colorPicker = new ModColorPicker();
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

    private <T extends UnlockableContent> void openSelector(Seq<T> list, Prov<T> prov, Cons<T> listener) {
        new Dialog("@background.styles.selector.title") {{
            ItemSelection.buildTable(cont, list, prov, (o) -> {
                hide();
                listener.get(o);
            });
            closeOnBack();
        }}.show();
    }

    public void showBackgroundStyles() {
        new Dialog("@background.styles.title") {{
//            cont.margin(30).add(dtext).padRight(6f);
            cont.table(t -> {
                t.label(() -> "unit: ").colspan(2);
                t.button(new TextureRegionDrawable(Core.atlas.find("cross")), () -> {
                    openSelector(content.units(), () -> {
                        int id = settings.getInt("background.style.unit", -1);
                        return id == -1 ? null : content.unit(id);
                    }, unit -> {
                        settings.put("background.style.unit", unit == null ? -1 : (int) unit.id);
                    });
                }).colspan(2).update(button -> {
                    int id = settings.getInt("background.style.unit", -1);
                    if (id >= content.units().size) {
                        id = -1;
                        settings.put("background.style.unit", -1);
                    }
                    button.getStyle().imageUp = (new TextureRegionDrawable(id == -1 ? Core.atlas.find("cross") : content.unit(id).uiIcon));
                }).size(60f).row();

                Boolf<Block> floorFilter = b -> b instanceof Floor && !(b instanceof OreBlock) && b != Blocks.spawn;
                Boolf<Block> wallFilter = b -> b instanceof StaticWall;
                t.label(() -> "wall1");
                t.button(new TextureRegionDrawable(Core.atlas.find("cross")), () -> {
                    openSelector(content.blocks().select(wallFilter), () -> {
                        int id = settings.getInt("background.style.wall1", -1);
                        Block block = content.block(id);
                        return wallFilter.get(block) ? block : null;
                    }, unit -> {
                        settings.put("background.style.wall1", unit == null ? -1 : (int) unit.id);
                    });
                }).update(button -> {
                    int id = settings.getInt("background.style.wall1", -1);
                    if (!wallFilter.get(content.block(id))) {
                        id = -1;
                        settings.put("background.style.wall1", -1);
                    }
                    button.getStyle().imageUp = (new TextureRegionDrawable(id == -1 ? Core.atlas.find("cross") : content.block(id).uiIcon));

                }).size(60f);
                t.label(() -> "floor1");
                t.button(new TextureRegionDrawable(Core.atlas.find("cross")), () -> {
                    openSelector(content.blocks().select(floorFilter), () -> {
                        int id = settings.getInt("background.style.floor1", -1);
                        Block block = content.block(id);
                        return floorFilter.get(block) ? block : null;
                    }, unit -> {
                        settings.put("background.style.floor1", unit == null ? -1 : (int) unit.id);
                    });
                }).update(button -> {
                    int id = settings.getInt("background.style.floor1", -1);
                    if (!floorFilter.get(content.block(id))) {
                        id = -1;
                        settings.put("background.style.floor1", -1);
                    }
                    button.getStyle().imageUp = (new TextureRegionDrawable(id == -1 ? Core.atlas.find("cross") : content.block(id).uiIcon));
                }).size(60f).row();

                /**============================================*/
                t.label(() -> "wall2");
                t.button(new TextureRegionDrawable(Core.atlas.find("cross")), () -> {
                    openSelector(content.blocks().select(wallFilter), () -> {
                        int id = settings.getInt("background.style.wall2", -1);
                        Block block = content.block(id);
                        return wallFilter.get(block) ? block : null;
                    }, unit -> {
                        settings.put("background.style.wall2", unit == null ? -1 : (int) unit.id);
                    });
                }).update(button -> {
                    int id = settings.getInt("background.style.wall2", -1);
                    if (!wallFilter.get(content.block(id))) {
                        id = -1;
                        settings.put("background.style.wall2", -1);
                    }
                    button.getStyle().imageUp = (new TextureRegionDrawable(id == -1 ? Core.atlas.find("cross") : content.block(id).uiIcon));

                }).size(60f);
                t.label(() -> "floor2");
                t.button(new TextureRegionDrawable(Core.atlas.find("cross")), () -> {
                    openSelector(content.blocks().select(floorFilter), () -> {
                        int id = settings.getInt("background.style.floor2", -1);
                        Block block = content.block(id);
                        return floorFilter.get(block) ? block : null;
                    }, unit -> {
                        settings.put("background.style.floor2", unit == null ? -1 : (int) unit.id);
                    });
                }).update(button -> {
                    int id = settings.getInt("background.style.floor2", -1);
                    if (!floorFilter.get(content.block(id))) {
                        id = -1;
                        settings.put("background.style.floor2", -1);
                    }
                    button.getStyle().imageUp = (new TextureRegionDrawable(id == -1 ? Core.atlas.find("cross") : content.block(id).uiIcon));
                }).size(60f).row();

                t.check("use custom style",settings.getBool("background.style.use",failedToLaunch),(b)-> settings.put("background.style.use",b));
            });


            buttons.defaults().size(210f, 64f);
            buttons.button("@back", Icon.left, this::hide).size(210f, 64f);

            closeOnBack();

        }}.show();
    }
}
