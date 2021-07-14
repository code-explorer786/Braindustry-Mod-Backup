package braindustry.ui.fragments;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.ui.Button;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.util.Align;
import arc.util.Tmp;
import braindustry.graphics.ModMenuShaderRender;
import braindustry.graphics.ModShaders;
import braindustry.input.ModBinding;
import braindustry.tools.MenuButtons;
import braindustry.tools.MenuButtons.MenuButton;
import braindustry.tools.MenuButtons.MenuButtonUnClose;
import mindustry.core.Version;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.ui.MobileButton;
import mindustry.ui.Styles;
import mindustry.ui.fragments.MenuFragment;

import static ModVars.modFunc.fullName;
import static ModVars.modVars.modUI;
import static arc.Core.scene;
import static mindustry.Vars.*;

public class ModMenuFragment extends MenuFragment {
    protected static boolean xAxis = false;
    protected static float pixels = 1f;
    protected static int otherAxisMul = 50;
    protected static float timeScl = 1f;
    private static ModMenuShaderRender lastRenderer;
    private Table container;
    private Table submenu;
    private Button currentMenu;
    private ModMenuShaderRender renderer;

    public ModMenuFragment(Group nullGroup) {

        Events.on(EventType.DisposeEvent.class, (event) -> {
            this.renderer.dispose();
        });
        super.build(nullGroup);
    }

    public static void init() {
//    ui.menuGroup.remove();
//    ui.menuGroup = new WidgetGroup();
//    ui.menuGroup.setFillParent(true);
//    ui.menuGroup.touchable = Touchable.childrenOnly;
//    ui.menuGroup.visible(() -> {
//        return Vars.state.isMenu();
//    });
//    Core.scene.add(ui.menuGroup);
//    ui.menufrag = new ModMenuFragment(nullGroup);
        lastRenderer = new ModMenuShaderRender();
        Events.on(EventType.DisposeEvent.class, (event) -> {
            lastRenderer.dispose();
        });
        try {
            WidgetGroup widgetGroup = (WidgetGroup) ui.menuGroup.getChildren().first();
            widgetGroup.getChildren().set(0, new Element() {
                @Override
                public void draw() {
                    lastRenderer.render();
                }
            });
            widgetGroup.getChildren().set(mobile ? 4 : (becontrol.active() ? 3 : 2), new Element() {
                {
                    touchable = Touchable.disabled;
                }

                @Override
                public void draw() {
                    drawTitle();
                }
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Runnable update = () -> {
            MenuButtons.menuButton(new MenuButton("@menu.title", Icon.menu,
                    new MenuButtonUnClose("@rebuild_menu", Icon.refresh, ModMenuFragment::rebuildMenu),
                    new MenuButtonUnClose("@background.styles", Icon.effect, () -> modUI.backgroundStyle.show()),
                    new MenuButtonUnClose("@background.screenshot", Icon.copy, ModMenuFragment::takeBackgroundScreenshot)
            ));
        };
        update.run();
        Events.on(EventType.ResizeEvent.class, e -> update.run());
//        Vars.ui.menufrag.
//    ui.menufrag.build(ui.menuGroup);
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
        ModShaders.waveShader.rect(logo, fx, fy, logow, logoh).forcePercent(pixels / (float) (!xAxis ? logo.height : logo.width)).xAxis(xAxis).otherAxisMul(otherAxisMul).timeScl(timeScl);
        Draw.shader();

        Fonts.def.setColor(Color.white);
        Fonts.def.draw(versionText, fx, fy - logoh / 2f, Align.center);
    }
}
