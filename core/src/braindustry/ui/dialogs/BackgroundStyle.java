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
import static mindustry.Vars.content;
import static mindustry.Vars.failedToLaunch;

public class BackgroundStyle extends Dialog {
    public static final String
            useSeed = "background.style.seed.use",
            seedValue = "background.style.seed.value",
            unit = "background.style.unit",
            wall1 = "background.style.wall1",
            floor1 = "background.style.floor1",
            wall2 = "background.style.wall2",
            floor2 = "background.style.floor2",
            useStyles = "background.style.use",
            zero = "";

    final static Boolf<Block> floorFilter = b -> b instanceof Floor && !(b instanceof OreBlock) && b != Blocks.spawn;
    final static Boolf<Block> wallFilter = b -> b instanceof StaticWall;

    public BackgroundStyle() {
        super("@background.style.title");

        cont.table(t -> {
            t.defaults().width(160.0f).height(70f);
            t.label(() -> "@background.style.seed.title");
            TextField field = t.field("" + seedValue(), TextField.TextFieldFilter.digitsOnly, (value) -> {
                settings.put(seedValue, Strings.parseInt(value, 0));
            }).colspan(2).get();
            t.check("@background.style.seed.use.title", useSeed(), b -> settings.put(useSeed, b));
            t.button(Icon.refresh, () -> {
                field.setProgrammaticChangeEvents(true);
                field.setText("" + Mathf.random(Integer.MAX_VALUE - 1));
            }).size(60f).row();
            t.label(() -> "@background.style.unit").colspan(2).right();
            t.add();
            TextureAtlas.AtlasRegion crossRegion = Core.atlas.find("cross");
            t.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(unit, content.units(), BackgroundStyle::unit, obj -> {
                    settings.put(unit, obj == null ? -1 : (int) obj.id);
                });
            }).colspan(2).update(button -> {
                UnitType block = unit();
                if (block == null) {
                    settings.put(unit, -1);
                }
                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(60f).row();

            t.label(() -> bundle.format("background.style.wall", 1));
            t.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(wall1, content.blocks().select(wallFilter), BackgroundStyle::wall1, unit -> {
                    settings.put(wall1, unit == null ? -1 : (int) unit.id);
                });
            }).update(button -> {
                Block block = wall1();
                if (block == null) {
                    settings.put(wall1, -1);
                }
                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(60f);
            t.add();
            t.label(() -> bundle.format("background.style.floor", 1));
            t.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(floor1, content.blocks().select(floorFilter), BackgroundStyle::floor1, unit -> {
                    settings.put(floor1, unit == null ? -1 : (int) unit.id);
                });
            }).update(button -> {
                Block block = floor1();
                if (block == null) {
                    settings.put(floor1, -1);
                }
                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(60f).row();

            /**============================================*/
            t.label(() -> bundle.format("background.style.wall", 2));
            t.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(wall2, content.blocks().select(wallFilter), BackgroundStyle::wall2, unit -> {
                    settings.put(wall2, unit == null ? -1 : (int) unit.id);
                });
            }).update(button -> {
                Block block = wall2();
                if (block == null) {
                    settings.put(wall2, -1);
                }
                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(60f);
            t.add();
            t.label(() -> bundle.format("background.style.floor", 2));
            t.button(new TextureRegionDrawable(crossRegion), () -> {
                openSelector(floor2, content.blocks().select(floorFilter), BackgroundStyle::floor2, unit -> {
                    settings.put(floor2, unit == null ? -1 : (int) unit.id);
                });
            }).update(button -> {
                Block block = floor2();
                if (block == null) {
                    settings.put(floor2, -1);
                }
                button.getStyle().imageUp = (new TextureRegionDrawable(block == null ? crossRegion : block.uiIcon));
            }).size(60f).row();

            t.defaults().reset();

            t.check("@background.style.use.title", settings.getBool(useStyles, failedToLaunch), (b) -> settings.put(useStyles, b)).colspan(5).height(60f);
        });


        buttons.defaults().size(210f, 64f);
        buttons.button("@back", Icon.left, this::hide).size(210f, 64f);
        buttons.button("@rebuild_menu", Icon.refresh, ModMenuFragment::rebuildMenu);

        closeOnBack();
    }

    public static Floor floor1() {
        int id = settings.getInt(floor1, -1);
        Block block = content.block(id);
        return floorFilter.get(block) ? block.asFloor() : null;
    }

    public static Floor floor2() {
        int id = settings.getInt(floor2, -1);
        Block block = content.block(id);
        return floorFilter.get(block) ? block.asFloor() : null;
    }

    public static StaticWall wall1() {
        int id = settings.getInt(wall1, -1);
        Block block = content.block(id);
        return wallFilter.get(block) ? (StaticWall) block : null;
    }

    public static StaticWall wall2() {
        int id = settings.getInt(wall2, -1);
        Block block = content.block(id);
        return wallFilter.get(block) ? (StaticWall) block : null;
    }

    public static UnitType unit() {
        return content.unit(settings.getInt(unit, -1));
    }

    public static int seedValue() {
        return settings.getInt(seedValue, 0);
    }

    public static boolean useSeed() {
        return settings.getBool(useSeed, false);
    }

    public static boolean useStyles() {
        return settings.getBool(useStyles, false);
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
