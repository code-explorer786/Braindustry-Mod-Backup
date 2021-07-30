package braindustry.ui.fragments;

import arc.Core;
import arc.func.Boolp;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.layout.Table;
import arc.util.Log;
import arc.util.Scaling;
import arc.util.Tmp;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.ModPal;
import braindustry.graphics.ModShaders;
import mindustry.gen.BlockUnitc;
import mindustry.gen.Call;
import mindustry.gen.Tex;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static mindustry.Vars.*;

public class ModHudFragment {
    public static void init() {
        Drawable zero = ((TextureRegionDrawable) Tex.whiteui).tint(0, 0, 0, 0);
        Table unitBar = new Table(zero);
        unitBar.marginTop(0).marginBottom(4).marginLeft(4);
        Element unitBackground = new Element() {
            @Override
            public void draw() {
                Draw.color(Pal.darkerGray);
                float radius = height / Mathf.sqrt3;
                Fill.poly(x + width / 2f, y + height / 2f, 6, radius);
                Draw.reset();
                if (player != null && player.unit() instanceof StealthUnitc) {
                    StealthUnitc unit = player.unit().as();
                    float offset = unit.stealthf();
                    Draw.color(ModPal.stealthBarColor);
                    ModShaders.iconBackgroundShader.set(y + (radius * 2f) * offset);
                    Fill.poly(x + width / 2f, y + height / 2f, 6, radius);
                    Draw.shader();
                    Draw.reset();
                }
                Drawf.shadow(x + width / 2f, y + height / 2f, height * 1.13f);
            }
        };
        unitBar.stack(
                unitBackground,
                new Table(t -> {
                    float bw = 40f;
                    float pad = -20;
                    t.margin(0);
                    t.clicked(() -> {
                        if (!player.dead() && mobile) {
                            Call.unitClear(player);
                            control.input.controlledType = null;
                        }
                    });

                    t.add(new SideBar(() -> player.unit().healthf(), () -> true, true)).width(bw).growY().padRight(pad);
                    t.image(() -> player.icon()).scaling(Scaling.bounded).grow().maxWidth(54f);
                    t.add(new SideBar(() -> player.dead() ? 0f : player.displayAmmo() ? player.unit().ammof() : player.unit().healthf(), () -> !player.displayAmmo(), false)).width(bw).growY().padLeft(pad).update(b -> {
                        b.color.set(player.displayAmmo() ? player.dead() || player.unit() instanceof BlockUnitc ? Pal.ammo : player.unit().type.ammoType.color() : Pal.health);
                    });

                    t.getChildren().get(1).toFront();
                })
        ).size(120f, 80).padRight(4);
//        Table unitBar = new Table();
        if (!mobile) {
            unitBar.add(unitBar).left().top();
            unitBar.update(() -> unitBar.setSize(unitBar.getPrefWidth(), Core.graphics.getHeight()));
        } else {
            Table overlaymarker = ui.hudGroup.find("overlaymarker");
            Table mobile_buttons = overlaymarker.find("mobile buttons");
//           Cell<Table> cell = actor.add(unitBar).left().top();
            unitBar.update(() -> {
                Log.info("height--: @ @", mobile_buttons.getHeight(), Core.graphics.getHeight());
//               unitBar.marginTop(mobile_buttons.getHeight()+1);
//               cell.
            });
        }
        unitBar.visible(() -> {
            boolean b = state.isGame() && !ui.minimapfrag.shown() && ui.hudfrag.shown;
            if (mobile) Log.info("b: @", b);
            return b;
//            ui.hudfrag.shown && state.isGame()
        });
        unitBar.row();
        unitBar.add().growY();
        unitBar.left().bottom();
        unitBar.setBackground(zero);
        ui.hudGroup.addChild(unitBar);


    }

    static class SideBar extends Element {
        public final Floatp amount;
        public final boolean flip;
        public final Boolp flash;

        float last, blink, value;

        public SideBar(Floatp amount, Boolp flash, boolean flip) {
            this.amount = amount;
            this.flip = flip;
            this.flash = flash;

            setColor(Pal.health);
        }

        @Override
        public void draw() {
            float next = amount.get();

            if (Float.isNaN(next) || Float.isInfinite(next)) next = 1f;

            if (next < last && flash.get()) {
                blink = 1f;
            }

            blink = Mathf.lerpDelta(blink, 0f, 0.2f);
            value = Mathf.lerpDelta(value, next, 0.15f);
            last = next;

            if (Float.isNaN(value) || Float.isInfinite(value)) value = 1f;

            drawInner(Pal.darkishGray, 1f);
            drawInner(Tmp.c1.set(color).lerp(Color.white, blink), value);
        }

        void drawInner(Color color, float fract) {
            if (fract < 0) return;

            fract = Mathf.clamp(fract);
            if (flip) {
                x += width;
                width = -width;
            }

            float stroke = width * 0.35f;
            float bh = height / 2f;
            Draw.color(color);

            float f1 = Math.min(fract * 2f, 1f), f2 = (fract - 0.5f) * 2f;

            float bo = -(1f - f1) * (width - stroke);

            Fill.quad(
                    x, y,
                    x + stroke, y,
                    x + width + bo, y + bh * f1,
                    x + width - stroke + bo, y + bh * f1
            );

            if (f2 > 0) {
                float bx = x + (width - stroke) * (1f - f2);
                Fill.quad(
                        x + width, y + bh,
                        x + width - stroke, y + bh,
                        bx, y + height * fract,
                        bx + stroke, y + height * fract
                );
            }

            Draw.reset();

            if (flip) {
                width = -width;
                x -= width;
            }
        }
    }
}
