package braindustry.ui.dialogs;

import arc.Core;
import arc.func.*;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectSet;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import braindustry.gen.BackgroundSettings;
import braindustry.tools.BackgroundConfig;
import braindustry.ui.ModStyles;
import mindustry.content.Blocks;
import mindustry.content.UnitTypes;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;

import static arc.Core.bundle;
import static arc.Core.settings;
import static braindustry.gen.BackgroundSettings.*;
import static mindustry.Vars.content;

public class BackgroundStyleDialog extends BaseDialog {
    final static Boolf<Block> floorFilter = b -> b instanceof Floor && !(b instanceof OreBlock) && b != Blocks.spawn;
    final static Boolf<Block> wallFilter = b -> b instanceof StaticWall;
    final static Boolf<Block> oreFilter = b -> b instanceof OreBlock;
    private static final ObjectSet<String> unexistBundles = new ObjectSet<>();

    public BackgroundStyleDialog() {
        super("@background.style.title");
        resized(true, this::setup);
        addCloseButton();
//        buttons.defaults().size(210f, 64f);
//        buttons.button("@back", Icon.left, this::hide).size(210f, 64f);
//        buttons.button("@rebuild_menu", Icon.refresh, ModMenuFragment::rebuildMenu);
//        closeOnBack();
    }

    private void setup() {
        cont.clear();
        Table table = new Table();
        ScrollPane pane = new ScrollPane(table);
        table.table(this::buildTable).self(cell -> {
            Table el = cell.get();
            cell.size(el.getPrefWidth(), el.getPrefHeight());
        });
        table.fillParent = true;
        cont.add(pane).growY().growX().bottom().center();
    }

    private void buildTable(Table t) {
        t.defaults().height(height());
        seedField(t, worldSeedKey, useWorldSeedKey,
                BackgroundSettings::useWorldSeed, BackgroundSettings::useWorldSeed,
                BackgroundSettings::worldSeed, BackgroundSettings::worldSeed);
        seedField(t, heatKey, useHeatSeedKey,
                BackgroundSettings::useHeatSeed, BackgroundSettings::useHeatSeed,
                BackgroundSettings::heatSeed, BackgroundSettings::heatSeed);
        seedField(t, oreSeedKey, useOreSeedKey,
                BackgroundSettings::useOreSeed, BackgroundSettings::useOreSeed,
                BackgroundSettings::oreSeed, BackgroundSettings::oreSeed);
        t.defaults().height(height()).width(width());
        addPaneSelect(t, 1, 3, BackgroundSettings::hasFloor3, BackgroundSettings::hasFloor3,
                BackgroundSettings::floor3, BackgroundSettings::floor3,
                BackgroundSettings::wall1, BackgroundSettings::wall1);
        addPaneSelect(t, 3, 1, () -> !hasWall3(), b -> hasWall3(!b),
                BackgroundSettings::floor1, BackgroundSettings::floor1,
                BackgroundSettings::wall3, BackgroundSettings::wall3);
        this.<OreBlock>addBlockField(t, 1, "ore", BackgroundSettings::ore1, oreFilter, BackgroundSettings::ore1);
        t.row();

        /**============================================*/
        addPaneSelect(t, 2, 4, BackgroundSettings::hasFloor4, BackgroundSettings::hasFloor4,
                BackgroundSettings::floor4, BackgroundSettings::floor4,
                BackgroundSettings::wall2, BackgroundSettings::wall2);
        addPaneSelect(t, 4, 2, () -> !hasWall4(), b -> hasWall4(!b),
                BackgroundSettings::floor2, BackgroundSettings::floor2,
                BackgroundSettings::wall4, BackgroundSettings::wall4);
        this.<OreBlock>addBlockField(t, 2, "ore", BackgroundSettings::ore2, oreFilter, BackgroundSettings::ore2);
        t.row();
        t.add(new Label(formatKey("background.style.movingType.title"))).colspan(1);
        movingType(t,"moving_type",BackgroundSettings::unitMovingType,BackgroundSettings::unitMovingType);
        /*t.add().colspan(4 - BackgroundConfig.UnitMovingType.values().length);
        for (BackgroundConfig.UnitMovingType value : BackgroundConfig.UnitMovingType.values()) {
            String text = formatKey("unitMovingType." + value);

            t.check(icon(value), (bool) -> {
                if (bool) unitMovingType(value);
            }).update(checkBox -> checkBox.setChecked(BackgroundSettings.unitMovingType() == value));
        }*/
        t.row();
        t.defaults().reset();
        t.row();
        viewType(t, (table) -> {
            TextureAtlas.AtlasRegion crossRegion = Core.atlas.find("cross");
            table.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(unitKey, content.units(), BackgroundSettings::unit, BackgroundSettings::unit);
            }).update(button -> {
                UnitType block = unit();
                if (block == null) unit(null);

                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(height());
        }, "units", BackgroundSettings::units, BackgroundSettings::units).row();
        viewType(t, table -> {
            table.slider(0, 1f, 0.01f, BackgroundSettings.heatValue(), BackgroundSettings::heatValue).fillX().height(height());
        }, "heat", BackgroundSettings::heat, BackgroundSettings::heat).row();
        viewType(t, Table::add, "ore", BackgroundSettings::ore, BackgroundSettings::ore).row();
        state(t, "tech", BackgroundSettings::tech, BackgroundSettings::tech).row();
        state(t, "tendrils", BackgroundSettings::tendrils, BackgroundSettings::tendrils).row();
        t.row();
        t.check(formatKey(useStylesKey), useStyles(), BackgroundSettings::useStyles).colspan(5).height(height());
    }

    private TextureRegion icon(BackgroundConfig.UnitMovingType value) {
        return switch (value) {
            case flying:
                yield UnitTypes.flare.fullIcon;
            case naval:
                yield UnitTypes.risso.fullIcon;
            case legs:
                yield UnitTypes.atrax.fullIcon;
            case mech:
                yield UnitTypes.dagger.fullIcon;
            default:
                throw new IllegalArgumentException("Cannot find icon for " + value.getClass().getSimpleName() + "." + value);
        };
    }

    private Drawable icon(BackgroundConfig.ViewType value) {
        return switch (value) {
            case random:
                yield Core.atlas.getDrawable("error");
            case custom:
                yield Icon.settings;
            case disable:
                yield Icon.cancel;
            default:
                throw new IllegalArgumentException("Cannot find icon for " + value.getClass().getSimpleName() + "." + value);
        };
    }

    private Drawable icon(BackgroundConfig.State value) {
        return switch (value) {
            case random:
                yield Core.atlas.getDrawable("error");
            case enable:
                yield Icon.ok;
            case disable:
                yield Icon.cancel;
            default:
                throw new IllegalArgumentException("Cannot find icon for " + value.getClass().getSimpleName() + "." + value);
        };
    }


    private void seedField(Table t, String key, String useKey, Boolp boolp, Boolc boolc, Intp prov, Intc cons) {
        t.add(new Label(formatKey(key + ".title"))).width(width());
        TextField field = t.field("" + prov.get(), TextField.TextFieldFilter.digitsOnly, (value) -> {
            cons.get(Strings.parseInt(value, 0));
//                BackG.parseInt(value, 0)
        }).width(width()).get();
        t.check(formatKey(useKey), boolp.get(), boolc).colspan(2).fillX().growX();
        t.button(Icon.refresh, () -> {
            field.setProgrammaticChangeEvents(true);
            field.setText("" + Mathf.randomSeed(System.nanoTime(), 0, Integer.MAX_VALUE - 1));
        }).size(height());
        t.add().row();
    }

    private float height() {
        return 60f * uiScl();
    }

    private float uiScl() {
//        return 1f / 860f * Math.min(860f, Core.graphics.getWidth()*Scl.scl());
        return Scl.scl();
//        return 1f;
    }

    private float width() {
        return 160.0f * uiScl();
    }

    private String formatKey(String key) {
        if (bundle.getOrNull(key) == null && unexistBundles.add(key)) {
            Log.err("unexitsts bundle: @", key);
        }
        Seq<String> split = Seq.with(key.split("\\."));
        split.remove("title");
        return bundle.get(key, Strings.capitalize(split.peek()));
    }

    private void addPaneSelect(Table t, int wall, int floor, Boolp boolp, Boolc boolc, Prov<Block> floorProv, Cons<Floor> floorCons, Prov<Block> wallProv, Cons<StaticWall> wallCons) {

//        Prov<Block> floorProv = BackgroundSettings::floor2;
//        Cons<Floor> floorCons = BackgroundSettings::floor2;
//        Prov<Block> wallProv = BackgroundSettings::wall4;
//        Cons<StaticWall> wallCons = BackgroundSettings::wall4;
        t.button("", ModStyles.buttonPanet, () -> {
            boolc.get(!boolp.get());
            setup();
        }).update(button -> button.setText(bundle.format("background.style." + (boolp.get() ? "floor" : "wall"), boolp.get() ? floor : wall)));
        if (boolp.get()) {
            this.<Floor>addBlockButton(t, floorKey + floor, floorProv, floorFilter, floorCons);
        } else {
            this.<StaticWall>addBlockButton(t, wallKey + wall, wallProv, wallFilter, wallCons);

        }
    }

    private Table viewType(Table table, String name, Prov<BackgroundConfig.ViewType> prov, Cons<BackgroundConfig.ViewType> cons) {
        return viewType(table, null, name, prov, cons);
    }

    private Table viewType(Table table, Cons<Table> tableCons, String name, Prov<BackgroundConfig.ViewType> prov, Cons<BackgroundConfig.ViewType> cons) {
        if (tableCons == null) ;
        table.add(new Label(formatKey("background.style." + name + ".title"))).colspan(1);
        if (tableCons != null) tableCons.get(table);
        for (BackgroundConfig.ViewType value : BackgroundConfig.ViewType.values()) {
            String text = formatKey("background.view_type." + value.name());
            table.button(icon(value), ModStyles.buttonPanei, () -> {
                cons.get(value);
            }).update(button -> {
                button.setChecked(prov.get() == value);
            }).height(height()).growX();
        }

        return table;
    }
    private Table movingType(Table table,  String name, Prov<BackgroundConfig.UnitMovingType> prov, Cons<BackgroundConfig.UnitMovingType> cons) {
        table.add(new Label(formatKey("background.style." + name + ".title"))).colspan(1);
        for (BackgroundConfig.UnitMovingType value : BackgroundConfig.UnitMovingType.values()) {
//            String text = formatKey("background.unit.." + value.name());
            table.button(new TextureRegionDrawable(icon(value)), ModStyles.buttonPanei, () -> {
                cons.get(value);
            }).update(button -> {
                button.setChecked(prov.get() == value);
            }).height(height()).growX();
        }

        return table;
    }

    private Table state(Table table, String name, Prov<BackgroundConfig.State> prov, Cons<BackgroundConfig.State> cons) {
        return state(table, null, name, prov, cons);
    }

    private Table state(Table table, Cons<Table> tableCons, String name, Prov<BackgroundConfig.State> prov, Cons<BackgroundConfig.State> cons) {
        table.add(new Label(formatKey("background.style." + name + ".title"))).colspan(tableCons == null ? 2 : 1);
        if (tableCons != null) tableCons.get(table);
        for (BackgroundConfig.State value : BackgroundConfig.State.values()) {
            String text = formatKey("background.state." + value.name());
            table.button(icon(value), ModStyles.buttonPanei, () -> {
                cons.get(value);
            }).update(button -> {
                button.setChecked(prov.get() == value);
            }).height(height()).growX();
        }
        return table;
    }

    private <T extends Block> void addBlockField(Table table, int index, String name, Prov<Block> getter, Boolf<Block> filter, Cons<T> setter) {
        String key = "background.style." + name + index;
        table.label(() -> bundle.format("background.style." + name, index));
        addBlockButton(table, key, getter, filter, setter);
    }

    private <T extends Block> void addBlockButton(Table table, String key, Prov<Block> getter, Boolf<Block> filter, Cons<T> setter) {
        TextureAtlas.AtlasRegion crossRegion = Core.atlas.find("cross");
        table.button(new TextureRegionDrawable(crossRegion), () -> {
            openSelector(key, content.blocks().select(filter), getter, unit -> {
                if (filter.get(unit)) setter.get((T) unit);
            });
        }).update(button -> {
            Block block = getter.get();
//            if (block == null) setter.get(null);

            button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
        }).size(height());
    }

    private <T extends UnlockableContent> void openSelector(String name, Seq<T> items, Prov<T> holder, Cons<T> listener) {
        new Dialog(formatKey(name + "-selector.title")) {{
            buildTable:
            {
                Cons<T> consumer = (o) -> {
                    hide();
                    listener.get(o);
                };
                ButtonGroup<ImageButton> group = new ButtonGroup<>();
                group.setMinCheckCount(0);
                Table table = new Table();
                float scl = 2;
                table.defaults().size(40 * scl);
                float isize = 24 * scl;

                int i = 0;

                int perRow = 5;
                for (T item : items) {
                    if (!item.unlockedNow()) continue;

                    ImageButton button = table.button(Tex.whiteui, Styles.clearToggleTransi, isize, () -> {
//                        hide();
                    }).group(group).get();
                    button.changed(() -> consumer.get(button.isChecked() ? item : null));
                    button.getStyle().imageUp = new TextureRegionDrawable(item.uiIcon);
                    button.update(() -> button.setChecked(holder.get() == item));

                    if (i++ % perRow == perRow - 1) {
                        table.row();
                    }
                }

                //add extra blank spaces so it looks nice
                if (i % perRow != 0) {
                    int remaining = perRow - (i % perRow);
                    for (int j = 0; j < remaining; j++) {
                        table.image(Styles.black6);
                    }
                }

                ScrollPane pane = new ScrollPane(table, Styles.smallPane);
                pane.setScrollingDisabled(true, false);
                pane.setScrollYForce(settings.getFloat(Strings.format("braindustry.ui.@-selector.scrollPos", name)));
                pane.update(() -> {
                    settings.put(Strings.format("@-selector.scrollPos", name), pane.getScrollY());
                });

                pane.setOverscroll(false, false);
                this.cont.add(pane).maxHeight(Scl.scl(40 * 5) * scl);
            }
            closeOnBack();
        }}.show();
    }
}
