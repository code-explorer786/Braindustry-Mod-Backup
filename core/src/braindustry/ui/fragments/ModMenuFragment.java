package braindustry.ui.fragments;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.event.Touchable;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.SnapshotSeq;
import arc.util.Align;
import braindustry.graphics.ModMenuRenderer;
import braindustry.graphics.ModShaders;
import braindustry.tools.MenuButtons;
import braindustry.tools.MenuButtons.MenuButton;
import braindustry.tools.MenuButtons.MenuButtonUnClose;
import mindustry.Vars;
import mindustry.core.Version;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Fonts;

import java.io.PrintWriter;
import java.io.StringWriter;

import static arc.Core.graphics;
import static braindustry.BDVars.fullName;
import static braindustry.BDVars.modUI;
import static mindustry.Vars.*;

public class ModMenuFragment {
    protected static boolean xAxis = false;
    protected static float pixels = 1f;
    protected static int otherAxisMul = 50;
    protected static float timeScl = 1f;
    //needs for exception log
    private static WidgetGroup widgetGroup = null;
    private static ModMenuRenderer lastRenderer;

    public static void init() {
        if (lastRenderer != null) return;
        lastRenderer = new ModMenuRenderer();
        Events.on(EventType.DisposeEvent.class, (event) -> {
            lastRenderer.dispose();
        });
        widgetGroup = null;
        try {
            widgetGroup = (WidgetGroup) ui.menuGroup.getChildren().first();
            widgetGroup.getChildren().set(0, new Element() {
                {
                    name = "custom-menu-background";
                    update(() -> {
                        if (!widgetGroup.visible) {
                            lastRenderer.resetData();
                        }
                    });
                }

                @Override
                public void draw() {
                    lastRenderer.render();
                }
            });
            widgetGroup.getChildren().set(mobile ? 4 : (becontrol.active() ? 3 : 2), new Element() {
                {
                    name = "braindustry-logo";
                    touchable = Touchable.disabled;
                }

                @Override
                public void draw() {
                    drawTitle();
                }
            });
        } catch (Exception exception) {
            StringBuilder widgetInfo = new StringBuilder();
            if (widgetGroup != null) {
                SnapshotSeq<Element> children = widgetGroup.getChildren();
                for (int i = 0; i < children.size; i++) {
                    widgetInfo.append(i).append(": ").append(children.get(i));
                    widgetInfo.append(i).append(": ").append(children.get(i));
                }
            } else {
                widgetInfo.append("<none>");
            }
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            throw new RuntimeException(sw.toString() + "\nwidgetInfo: " + widgetInfo.toString());
        }
        Runnable update = () -> {
            MenuButtons.menuButton(new MenuButton("@menu.title", Icon.menu,
                    new MenuButtonUnClose("@rebuild_menu", Icon.refresh, ModMenuFragment::rebuildMenu),
                    new MenuButtonUnClose("@background.styles", Icon.effect, () -> modUI.backgroundStyleDialog.show()),
                    new MenuButtonUnClose("@background.screenshot", Icon.copy, ModMenuFragment::takeBackgroundScreenshot)
            ));
        };
        if (mobile) {

            //Taken from MEEPofFaith / testing-utilities
            Vars.ui.settings.shown(() -> {
                Table table = (Table) ((Group) ((Group) ui.settings.getChildren().get(1)).getChildren().get(0)).getChildren().get(0);
                table.row();
                table.button("@background.styles", () -> modUI.backgroundStyleDialog.show());
            });
        }
        update.run();
        Events.on(EventType.ResizeEvent.class, e -> update.run());
    }


    private static void takeBackgroundScreenshot() {
        lastRenderer.takeBackgroundScreenshot();
    }

    public static void rebuildMenu() {
        lastRenderer.rebuild();
    }

    public static void timeScl(float timeScl) {
        ModMenuFragment.timeScl = timeScl;
    }

    public static void xAxis(boolean xAxis) {
        ModMenuFragment.xAxis = xAxis;
    }

    public static void pixels(float pixels) {
        ModMenuFragment.pixels = pixels;
    }

    public static void otherAxisMul(int otherAxisMul) {
        ModMenuFragment.otherAxisMul = otherAxisMul;
    }

    private static void drawTitle() {
        String versionText = ((Version.build == -1) ? "[#fc8140aa]" : "[#ffffffba]") + Version.combined();
        TextureRegion logo = Core.atlas.find(fullName("logo"), "logo");
        float width = Core.graphics.getWidth(), height = Core.graphics.getHeight() - Core.scene.marginTop;
        float logoscl = Scl.scl(1);
        float logow = Math.min(logo.width * logoscl, Core.graphics.getWidth() - Scl.scl(20));
        float logoh = logow * (float) logo.height / logo.width;

        float fx = (int) (width / 2f);
        float fy = (int) (height - 6 - logoh) + logoh / 2 - (Core.graphics.isPortrait() ? Scl.scl(30f) : 0f);

        Draw.color();
        if (ModShaders.waveShader != null) {
            ModShaders.waveShader
                    .forcePercent(pixels / (float) (!xAxis ? logo.height : logo.width))
                    .xAxis(xAxis)
                    .otherAxisMul(otherAxisMul)
                    .timeScl(timeScl)
                    .region(null);
            renderer.effectBuffer.resize(graphics.getWidth(), graphics.getHeight());
            renderer.effectBuffer.begin(Color.clear);
            Draw.rect(logo, fx, fy, logow, logoh);
            renderer.effectBuffer.end();
            renderer.effectBuffer.blit(ModShaders.waveShader);
        } else {
            Draw.rect(logo, fx, fy, logow, logoh);
        }
        Draw.shader();

        Fonts.def.setColor(Color.white);
        Fonts.def.draw(versionText, fx, fy - logoh / 2f, Align.center);
    }
}
