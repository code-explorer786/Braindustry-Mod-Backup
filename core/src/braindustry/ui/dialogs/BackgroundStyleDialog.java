package braindustry.ui.dialogs;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.g2d.TextureAtlas;
import arc.math.Mathf;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Strings;
import braindustry.gen.BackgroundSettings;
import braindustry.tools.BackgroundConfig;
import braindustry.ui.ModStyles;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.content.Blocks;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.type.UnitType;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;
import mindustry.world.blocks.environment.StaticWall;

import static arc.Core.bundle;
import static arc.Core.settings;
import static braindustry.gen.BackgroundSettings.*;
import static mindustry.Vars.content;
import static mindustry.Vars.state;

public class BackgroundStyleDialog extends Dialog {
    final static Boolf<Block> floorFilter = b -> b instanceof Floor && !(b instanceof OreBlock) && b != Blocks.spawn;
    final static Boolf<Block> wallFilter = b -> b instanceof StaticWall;

    public BackgroundStyleDialog() {
        super("@background.style.title");

        setup();


        buttons.defaults().size(210f, 64f);
        buttons.button("@back", Icon.left, this::hide).size(210f, 64f);
        buttons.button("@rebuild_menu", Icon.refresh, ModMenuFragment::rebuildMenu);

        closeOnBack();
    }

    private void setup() {
        cont.clear();
        cont.table(t -> {
            t.defaults().width(160.0f).height(70f);
            t.label(() -> "@background.style.seed.title");
            TextField field = t.field("" + seed(), TextField.TextFieldFilter.digitsOnly, (value) -> {
                seed(Strings.parseInt(value, 0));
//                BackG.parseInt(value, 0)
            }).colspan(2).get();
            t.check("@background.style.seed.use.title", useSeed(), BackgroundSettings::useSeed);
            t.button(Icon.refresh, () -> {
                field.setProgrammaticChangeEvents(true);
                field.setText("" + Mathf.randomSeed(System.nanoTime(), 0, Integer.MAX_VALUE - 1));
            }).size(60f).row();
            TextureAtlas.AtlasRegion crossRegion = Core.atlas.find("cross");

            t.button("", ModStyles.buttonPane, () -> {
                hasFloor3(!hasFloor3());
                setup();
            }).update(button -> button.setText(bundle.format("background.style." + (hasFloor3() ? "floor" : "wall"), hasFloor3() ? 3 : 1)));
            if (hasFloor3()) {
                this.<Floor>addBlockButton(t, floor3Key, BackgroundSettings::floor3, floorFilter, BackgroundSettings::floor3);
            } else {
                this.<StaticWall>addBlockButton(t, wall1Key, BackgroundSettings::wall1, wallFilter, BackgroundSettings::wall1);
            }
            t.add();
            this.<Floor>addBlockField(t, 1, floor1Key, "floor", BackgroundSettings::floor1, floorFilter, BackgroundSettings::floor1);
            t.row();

            /**============================================*/
            t.button("", ModStyles.buttonPane, () -> {
                hasFloor4(!hasFloor4());
                setup();
            }).update(button -> button.setText(bundle.format("background.style." + (hasFloor4() ? "floor" : "wall"), hasFloor4() ? 4 : 2)));
            if (hasFloor4()) {
                this.<Floor>addBlockButton(t, floor4Key, BackgroundSettings::floor4, floorFilter, BackgroundSettings::floor4);
            } else {
                this.<StaticWall>addBlockButton(t, wall2Key, BackgroundSettings::wall2, wallFilter, BackgroundSettings::wall2);
            }
            t.add();
            this.<Floor>addBlockField(t, 2, floor2Key, "floor", BackgroundSettings::floor2, floorFilter, BackgroundSettings::floor2);
            t.row();
            t.label(() -> "@background.style.movingType.title").colspan(1);
            t.add().colspan(4 - BackgroundConfig.UnitMovingType.values().length);
            ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<>();
            for (BackgroundConfig.UnitMovingType value : BackgroundConfig.UnitMovingType.values()) {
                CheckBox checkBox = t.check("@unitMovingType." + value, (bool) -> {
                    if (bool) unitMovingType(value);
                }).get();
                buttonGroup.add(checkBox);
            }
            t.defaults().reset();
            t.row();
            viewType(t,(table)->{
                table.button(new TextureRegionDrawable(crossRegion), () -> {
                    openSelector(unitKey, content.units(), BackgroundSettings::unit, BackgroundSettings::unit);
                }).update(button -> {
                    UnitType block = unit();
                    if (block == null) unit(null);

                    button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
                }).size(60f);
            }, "units", BackgroundSettings::units, BackgroundSettings::units).row();
            viewType(t, "heat", BackgroundSettings::heat, BackgroundSettings::heat).row();
            viewType(t, "ore", BackgroundSettings::ore, BackgroundSettings::ore).row();
            state(t,"tech",BackgroundSettings::tech,BackgroundSettings::tech).row();
            state(t,"tendrils",BackgroundSettings::tendrils,BackgroundSettings::tendrils).row();
            t.row();
            t.check("@background.style.use.title", useStyles(), BackgroundSettings::useStyles).colspan(5).height(60f);
        });
    }

    private Table viewType(Table table, String name, Prov<BackgroundConfig.ViewType> prov, Cons<BackgroundConfig.ViewType> cons) {
        return    viewType(table, null, name, prov, cons);
    }
    private Table viewType(Table table,Cons<Table> tableCons, String name, Prov<BackgroundConfig.ViewType> prov, Cons<BackgroundConfig.ViewType> cons) {
        table.label(()->"@background.style."+name+".title").colspan(tableCons==null?2:1);
        if (tableCons!=null)tableCons.get(table);
        for (BackgroundConfig.ViewType value : BackgroundConfig.ViewType.values()) {
            table.button("@background.view_type." + value.name(), ModStyles.buttonPane, () -> {
                cons.get(value);
            }).update(button->{
                button.setChecked(prov.get()==value);
            }).height(60f).growX();
        }

        return table;
    }
    private Table state(Table table, String name, Prov<BackgroundConfig.State> prov, Cons<BackgroundConfig.State> cons) {
       return state(table, null, name, prov, cons);
    }
    private Table state(Table table,Cons<Table> tableCons, String name, Prov<BackgroundConfig.State> prov, Cons<BackgroundConfig.State> cons) {
        table.label(()->"@background.style."+name+".title").colspan(tableCons==null?2:1);
        if (tableCons!=null)tableCons.get(table);
        for (BackgroundConfig.State value : BackgroundConfig.State.values()) {
            table.button("@background.state." + value.name(), ModStyles.buttonPane, () -> {
                cons.get(value);
            }).update(button->{
                button.setChecked(prov.get()==value);
            }).height(60f).growX();
        }
        return table;
    }

    private <T extends Block> void addBlockField(Table table, int index, String key, String name, Prov<Block> getter, Boolf<Block> filter, Cons<T> setter) {
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
        }).size(60f);
    }

    private <T extends UnlockableContent> void openSelector(String name, Seq<T> items, Prov<T> holder, Cons<T> listener) {
        new Dialog("@" + name + "-selector.title") {{
            buildTable:
            {
                Cons<T> consumer = (o) -> {
                    hide();
                    listener.get(o);
                };
                ButtonGroup<ImageButton> group = new ButtonGroup<>();
                group.setMinCheckCount(0);
                Table table = new Table();
                table.defaults().size(40);

                int i = 0;

                for (T item : items) {
                    if (!item.unlockedNow()) continue;

                    ImageButton button = table.button(Tex.whiteui, Styles.clearToggleTransi, 24, () -> {
//                        hide();
                    }).group(group).get();
                    button.changed(() -> consumer.get(button.isChecked() ? item : null));
                    button.getStyle().imageUp = new TextureRegionDrawable(item.uiIcon);
                    button.update(() -> button.setChecked(holder.get() == item));

                    if (i++ % 4 == 3) {
                        table.row();
                    }
                }

                //add extra blank spaces so it looks nice
                if (i % 4 != 0) {
                    int remaining = 4 - (i % 4);
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
                this.cont.add(pane).maxHeight(Scl.scl(40 * 5));
            }
            closeOnBack();
        }}.show();
    }
}
