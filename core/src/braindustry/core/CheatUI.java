package braindustry.core;

import arc.Core;
import arc.func.*;
import arc.graphics.Color;
import arc.graphics.g2d.TextureRegion;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Strings;
import braindustry.BDVars;
import braindustry.annotations.BDAnnotations;
import braindustry.gen.CheatModRulesTable;
import braindustry.gen.BDCall;
import braindustry.ui.ModStyles;
import mindustry.Vars;
import mindustry.entities.EntityCollisions;
import mindustry.game.Rules;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Tile;
import mindustry.world.blocks.Attributes;
import mindustry.world.meta.Attribute;
import mma.ui.dialogs.cheat.ModCheatItemsMenu;
import mma.ui.dialogs.cheat.TeamChooseDialog;
import mma.ui.dialogs.cheat.UnitChooseDialog;
import mma.ui.dialogs.cheat.UnlockContentDialog;

import static braindustry.BDVars.modUI;
import static braindustry.BDVars.showException;
import static braindustry.core.ModUI.getInfoDialog;
import static mindustry.Vars.*;

@BDAnnotations.RulesTable("rulesEditTable")
public class CheatUI {
    public static Prov<Boolean> visibility = BDVars::showCheatMenu;
    static Runnable rebuildTeamValue = () -> {
    };

    public static void rulesEditTable(Table table, String name, Prov<Boolean> bool, Boolc cons) {
        table.check(name, bool.get(), cons);
    }

    public static void rulesEditTable(Table table, String name, Prov<Float> val, Floatc cons) {
        table.label(() -> name + ": ");
        TextArea textArea = table.area("" + val.get(), text -> cons.get(Strings.parseFloat(text, 0f))).minWidth(100).fillX().get();
        textArea.setMaxLength((Float.MAX_VALUE + ".").length());
        textArea.setFilter(TextField.TextFieldFilter.floatsOnly);
    }

    public static void rulesEditTable(Table table, String name, Prov<Integer> val, Intc cons) {
        table.label(() -> name + ": ");
        TextArea textArea = table.area("" + val.get(), text -> cons.get(Strings.parseInt(text, 0))).width(100).get();
        textArea.setMaxLength((Integer.MAX_VALUE + "").length());
        textArea.setFilter(TextField.TextFieldFilter.digitsOnly);
    }

    public static void rulesEditTable(Table table, String name, String val, Cons<String> cons) {
        table.label(() -> name + ": ");
        TextArea textArea = table.area("" + val, cons).width(100).get();
        textArea.setMaxLength((Integer.MAX_VALUE + "").length());
    }

    public static void rulesEditTable(Table table, String name, Func<Integer, Color> val, Cons<Color> cons) {
        table.table(t -> {
            t.label(() -> name).growX();
            t.add(new Image()).update(image -> image.setColor(val.get(0))).fill();
        }).growX();
        table.button("edit", () -> modUI.colorPicker.show(val.get(0).cpy(), cons)).growX();
    }

    public static void rulesEditTable(Table table, String name, Prov<Team> val, Cons<Team> cons) {
        table.label(() -> name + ": [#" + val.get().color.toString() + "]" + val.get().name).growX();
        table.button("edit", () -> {
            new TeamChooseDialog(cons).show();
        }).growX();
    }

    public static void rulesEditTable(Table table, String name, Attributes val, Cons<Attributes> cons) {
        table.label(() -> name).growX().row();
        table.table(t -> {
            for (Attribute attribute : Attribute.all) {
                rulesEditTable(t, attribute.name, () -> val.get(attribute), value -> {
                    val.set(attribute, value);
                    cons.get(val);
                });
                t.row();
            }
        }).padLeft(4f).grow();
    }

    public static void openUnlockContentDialog() {
        new UnlockContentDialog(ModStyles.buttonColor).show();
    }

    public static void openModCheatItemsMenu() {
        if (!visibility.get() || net.client()) return;
        new ModCheatItemsMenu().show(() -> {
        }, () -> {
        });
    }

    //    private static boolean openDialog=false;
    public static void openUnitChooseDialog() {
        if (!visibility.get()) return;
        new UnitChooseDialog((unitType) -> {
            Tile tile = Vars.player.tileOn();
            Unit unit = unitType.constructor.get();
            unit.type = unitType;
            EntityCollisions.SolidPred solidity = unit.solidity();
            if ((tile == null) || solidity != null && solidity.solid(tile.x, tile.y)) {
                Color color = Color.valueOf(Strings.format("#@", Color.scarlet));
                getInfoDialog("", "Can't spawn this unit!!!", "The selected unit type cannot be created on this block", color.lerp(Color.white, 0.2f)).show();
                return false;
            }
            BDCall.setNewUnit(unitType);
            return true;
        }).show();
    }

    protected static void addButton(Table cont, Team team) {
        TextureRegion white = new TextureRegion(Core.atlas.white());
        white.width = white.height = 38;
        cont.button(new TextureRegionDrawable(white, 1f).tint(team.color), Styles.clearTransi, () -> {
            BDVars.settings.setInt("cheat-team", team.id);
            rebuildTeamValue.run();
        });
    }

    public static void openRulesEditDialog() {
        if (net.client() || !visibility.get()) {
//            modFunc.getInfoDialog("@cant-open-rules-dialog","","@you-on-server",Color.scarlet);
            return;
        }
        BaseDialog dialog = new BaseDialog("@cheat-menu.rules-edit.title");
        ScrollPane mainPain = dialog.cont.pane(pane -> {
//            Log.info("pane size(@,@)", pane.getWidth(), pane.getHeight());
            ScrollPane scrollPane = pane.pane(p -> {
                p.defaults().size(40).left();
                for (Team team : Team.all) {
                    addButton(p, team);
                    if ((team.id + 1) % (mobile ? 6 : 12) == 0) p.row();
                }
            }).maxHeight(Scl.scl(40 * 2)).get();
            pane.row();
            pane.table((teamValue) -> {
                rebuildTeamValue = () -> {
                    teamValue.clear();
                    teamValue.clearChildren();
                    Rules.TeamRule teamRule = state.rules.teams.get(Team.get(BDVars.settings.getInt("cheat-team")));
                    teamValue.check("ai", teamRule.ai, (b) -> teamRule.ai = b).row();
                    teamValue.add("aiTier: " + teamRule.aiTier).update(label -> {
                        label.setText("aiTier: " + teamRule.aiTier);
                    });
                    teamValue.slider(0, Float.MAX_VALUE, 1f, teamRule.aiTier, (f) -> {
                        teamRule.aiTier = f;
                    }).row();

                    teamValue.check("aiCoreSpawn", teamRule.aiCoreSpawn, (b) -> teamRule.aiCoreSpawn = b).row();
                    teamValue.check("infiniteAmmo", teamRule.infiniteAmmo, (b) -> teamRule.infiniteAmmo = b).row();
                    teamValue.check("infiniteResources", teamRule.infiniteResources, (b) -> teamRule.infiniteResources = b).row();
                };
                rebuildTeamValue.run();
            }).row();
            pane.image(new TextureRegionDrawable(Core.atlas.white()).tint(Pal.accent)).growX().row();
//            Log.info("pane size(@,@)",pane.,pane.getHeight());
            pane.table((t) -> {
                t.defaults().left();
                CheatModRulesTable.build(t, state.rules);

            });
            scrollPane.setScrollingDisabled(true, false);
            scrollPane.setOverscroll(false, false);
        }).minWidth(520f).get();

        mainPain.setScrollingDisabled(true, false);
        mainPain.setOverscroll(false, false);

        dialog.addCloseListener();
        dialog.addCloseButton();
        dialog.show();
    }

    public static void openTeamChooseDialog() {
        if (!visibility.get()) return;
        new TeamChooseDialog((team) -> {
            try {
                BDCall.setTeam(team);
            } catch (Exception exception) {
                showException(exception);
            }
        }).show();
    }

}
