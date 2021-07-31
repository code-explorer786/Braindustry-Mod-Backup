package braindustry.ui.fragments;

import arc.func.Boolp;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.SnapshotSeq;
import arc.util.Log;
import arc.util.Tmp;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.ModPal;
import braindustry.graphics.ModShaders;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static mindustry.Vars.player;
import static mindustry.Vars.ui;

public class ModHudFragment {
    public static void init() {

        Table overlaymarker = ui.hudGroup.find("overlaymarker");
        Table mobile_buttons = overlaymarker.find("mobile buttons");
        Table status = overlaymarker.<Stack>find("waves/editor").<Table>find("waves").<Table>find("status");
        Stack stack = status.<Stack>find(el -> el.getClass().getSimpleName().equals("Stack") && el.toString().contains("HudFragment$1"));
        SnapshotSeq<Element> children = stack.getChildren();
        int fragIndex = children.indexOf(el -> el.getClass().getSimpleName().equals("HudFragment$1") || el.toString().equals("HudFragment$1"));
        if (fragIndex == -1) {
            Log.info("status_ERROR: @", status);
            Log.info("stack_ERROR: @", stack);
            return;
        }
        Element oldFrag = children.get(fragIndex);
        oldFrag.parent = null;
        oldFrag.remove();
        Element unitBackground = new Element() {
            @Override
            public void draw() {
                boolean me = player != null && player.unit() instanceof StealthUnitc && !player.unit().dead() && player.unit().isValid();
                if (!me){
                    oldFrag.setBounds(x,y,width,height);
                    oldFrag.draw();
                    return;
                }
                Draw.color(Pal.darkerGray);
                float radius = height / Mathf.sqrt3;
                Fill.poly(x + width / 2f, y + height / 2f, 6, radius);
                Draw.reset();
                if (me) {
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
        children.set(fragIndex, unitBackground);
        return;


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
