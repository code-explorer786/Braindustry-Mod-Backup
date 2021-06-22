package braindustry.ui.fragments;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.Mathf;
import arc.scene.Element;
import arc.scene.Group;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.SnapshotSeq;
import arc.util.Log;
import braindustry.gen.StealthUnitc;
import braindustry.graphics.ModPal;
import braindustry.graphics.ModShaders;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;

import static mindustry.Vars.player;
import static mindustry.Vars.ui;

public class SpecialUnitStatusFragment extends Element {
    public static void init() {
        SpecialUnitStatusFragment frag = new SpecialUnitStatusFragment();
        Table cont = ui.hudGroup.find("overlaymarker");
        Stack stack1 = cont.find("waves/editor");
        Table wavesMain =  stack1.find("waves");
        Table table = wavesMain.find("status");
        Stack stack2 = table.find(el -> el instanceof Stack &&
                                           ((Stack) el).find(el2 -> el2.getClass().getName().contains("mindustry.ui.HudFragment")) != null);
        Log.info(stack2);
        Log.info(stack2.getChildren().first().getClass().getName());
//        SnapshotSeq<Element> children = wavesMain.getChildren();
//        Element element = table;
//        children.set(children.indexOf(element),frag);
    }

    @Override
    public void draw() {
        Draw.color(Pal.darkerGray);
        float radius = height / Mathf.sqrt3;
        Fill.poly(x + width / 2f, y + height / 2f, 6, radius);
        Draw.reset();
//                        Draw.flush();
        if (player != null && player.unit() instanceof StealthUnitc) {
            StealthUnitc unit = player.unit().as();
            float offset = unit.stealthf();
            Draw.color(ModPal.stealthBarColor);
            ModShaders.iconBackgroundShader.set(y + (radius * 2f) * offset);
            Fill.poly(x + width / 2f, y + height / 2f, 6, radius);
            Draw.shader();
            Draw.reset();
//                            Draw.flush();
        }
        Drawf.shadow(x + width / 2f, y + height / 2f, height * 1.13f);
    }

    //    @Override
    public void build(Group parent) {

    }
}
