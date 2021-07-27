package braindustry.ui.fragments;

import arc.Core;
import arc.Events;
import arc.func.Boolp;
import arc.func.Floatp;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Interp;
import arc.math.Mathf;
import arc.scene.Action;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.actions.Actions;
import arc.scene.event.Touchable;
import arc.scene.style.Drawable;
import arc.scene.style.TextureRegionDrawable;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Table;
import arc.scene.ui.layout.WidgetGroup;
import arc.struct.Bits;
import arc.struct.Seq;
import arc.util.*;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.ModPal;
import braindustry.graphics.ModShaders;
import mindustry.Vars;
import mindustry.annotations.Annotations;
import mindustry.core.GameState;
import mindustry.ctype.UnlockableContent;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.*;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.Binding;
import mindustry.net.Net;
import mindustry.net.Packets;
import mindustry.type.Item;
import mindustry.ui.*;
import mindustry.ui.dialogs.PausedDialog;
import mindustry.ui.dialogs.SchematicsDialog;
import mindustry.ui.fragments.HudFragment;

import java.util.Objects;

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
                        b.color.set(player.displayAmmo() ? player.dead() || player.unit() instanceof BlockUnitc ? Pal.ammo : player.unit().type.ammoType.color : Pal.health);
                    });

                    t.getChildren().get(1).toFront();
                })
        ).size(120f, 80).padRight(4);
        Table actor = new Table();
       if (!mobile) {
           actor.add(unitBar).left().top();
           actor.update(() -> actor.setSize(actor.getPrefWidth(), Core.graphics.getHeight()));
       }else {
           Table overlaymarker = ui.hudGroup.find("overlaymarker");
           Table mobile_buttons = overlaymarker.find("mobile buttons");
           Cell<Table> cell = actor.add(unitBar).left().top();
           cell.update(t-> {
               Log.info("height: @==@==@==@==@",mobile_buttons.getPrefHeight(),mobile_buttons.getHeight(),mobile_buttons.getMinHeight(),overlaymarker.getRowHeight(0),overlaymarker.getRowHeight(1));
               cell.padTop(overlaymarker.getRowHeight(0)+overlaymarker.getRowHeight(1));
           });
       }
        actor.visible(()-> ui.hudfrag.shown);
        actor.row();
        actor.add().growY();
        actor.left().bottom();
        actor.setBackground(zero);
        ui.hudGroup.addChild(actor);
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
