package braindustry.ui.dialogs;

import arc.Core;
import arc.func.Boolf;
import arc.func.Cons;
import arc.func.Prov;
import arc.graphics.g2d.TextureAtlas;
import arc.math.Mathf;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.*;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Strings;
import braindustry.cfunc.BackgroundUnitMovingType;
import braindustry.gen.BackgroundSettings;
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

public class BackgroundStyle extends Dialog {
    final static Boolf<Block> floorFilter = b -> b instanceof Floor && !(b instanceof OreBlock) && b != Blocks.spawn;
    final static Boolf<Block> wallFilter = b -> b instanceof StaticWall;

    public BackgroundStyle() {
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
                BackgroundSettings.seed(Strings.parseInt(value, 0));
//                BackG.parseInt(value, 0)
            }).colspan(2).get();
            t.check("@background.style.seed.use.title", useSeed(), BackgroundSettings::useSeed);
            t.button(Icon.refresh, () -> {
                field.setProgrammaticChangeEvents(true);
                field.setText("" + Mathf.randomSeed(System.nanoTime(), 0, Integer.MAX_VALUE - 1));
            }).size(60f).row();
            t.label(() -> "@background.style.unit").colspan(2).right();
            t.add();
            TextureAtlas.AtlasRegion crossRegion = Core.atlas.find("cross");
            t.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(unitKey, content.units(), BackgroundSettings::unit, BackgroundSettings::unit);
            }).colspan(2).update(button -> {
                UnitType block = BackgroundSettings.unit();
                if (block == null) BackgroundSettings.unit(null);

                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(60f).row();

           if (useWalls()){
               this.<StaticWall>addBlockField(t,1,wall1Key,"wall",BackgroundSettings::wall1,wallFilter,BackgroundSettings::wall1);
           } else {
               this.<Floor>addBlockField(t,3,floor3Key,"floor",BackgroundSettings::floor3,floorFilter,BackgroundSettings::floor3);
           }
            t.add();
            this.<Floor>addBlockField(t,1,floor1Key,"floor",BackgroundSettings::floor1,floorFilter,BackgroundSettings::floor1);
            t.row();

            /**============================================*/
            if (useWalls()){
                this.<StaticWall>addBlockField(t,2,wall2Key,"wall",BackgroundSettings::wall2,wallFilter,BackgroundSettings::wall2);
            } else {
                this.<Floor>addBlockField(t,4,floor4Key,"floor",BackgroundSettings::floor4,floorFilter,BackgroundSettings::floor4);
            }
            t.add();
            this.<Floor>addBlockField(t,2,floor2Key,"floor",BackgroundSettings::floor2,floorFilter,BackgroundSettings::floor2);
            t.row();
            t.label(()->"@background.style.movingType.title").colspan(1);
            t.add().colspan(4-BackgroundUnitMovingType.values().length);
            ButtonGroup<CheckBox> buttonGroup=new ButtonGroup<>();
            for (BackgroundUnitMovingType value : BackgroundUnitMovingType.values()) {
                CheckBox checkBox = t.check("@unitMovingType." + value, (bool) -> {
                    if (bool) unitMovingType(value);
                }).get();
                buttonGroup.add(checkBox);
            }
            t.defaults().reset();

            t.row();
            t.check("@background.style.walls.title", useWalls(), bool->{
                BackgroundSettings.useWalls(bool);
                setup();
            }).colspan(5).height(60f);
            t.row();
            t.check("@background.style.units.title", hasUnits(), BackgroundSettings::hasUnits).colspan(5).height(60f);
            t.row();
            t.check("@background.style.use.title", useStyles(), BackgroundSettings::useStyles).colspan(5).height(60f);
        });
    }

    private <T extends Block> void addBlockField(Table table, int index, String key,String name, Prov<Block> getter, Boolf<Block> filter, Cons<T> setter) {
        TextureAtlas.AtlasRegion crossRegion = Core.atlas.find("cross");
        table.label(() -> bundle.format("background.style."+name, index));
        table.button(new TextureRegionDrawable(crossRegion), () -> {
            openSelector(key, content.blocks().select(filter), getter, unit -> {
                if (filter.get(unit)) setter.get((T) unit);
            });
        }).update(button -> {
            Block block = getter.get();
            if (block == null) setter.get(null);

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
