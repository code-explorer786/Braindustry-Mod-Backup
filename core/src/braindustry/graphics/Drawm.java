package braindustry.graphics;

import braindustry.customArc.math.ModMath;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Font;
import arc.graphics.g2d.FontCache;
import arc.graphics.g2d.GlyphLayout;
import arc.math.Mathf;
import arc.math.geom.Position;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Tmp;
import braindustry.content.ModFx;
import mindustry.ui.Fonts;
import mma.customArc.cfunc.Couple;
import mma.customArc.cfunc.Couple3;
import mma.graphics.ModDraw;

public class Drawm extends ModDraw {
    public static void teleportCircles(float x, float y, float radius, Color first, Color second, Couple<Float,Float> range){
        Seq<Couple3<Vec2, Float, Color>> couple3s = new Seq<>();
        float lastAngle = 0;
        float maxAngle = 0;
        Vec2 firstO=new Vec2();
        Vec2 lastPos = new Vec2();
        for (int i = 0; true; i++) {
            float mradius = Mathf.random(range.o1, range.o2);
            Color lerp = first.cpy().lerp(second, Mathf.random());
            Vec2 pos = Tmp.v1.trns(0, radius).cpy();
            if (i != 0) {
                float angle = ModMath.atan((mradius + lastAngle) / radius);
                float fangle = angle + lastPos.angle();
                if (fangle > maxAngle) break;
                float pangle =ModMath.atan((mradius) / radius);
                if (pangle + angle > maxAngle) {
                    fangle = maxAngle - (maxAngle - lastAngle) / 2f;
                    pos = Tmp.v1.trns(fangle, radius).cpy();
                    mradius = pos.dst(firstO)/2f;
                    couple3s.add(Couple3.of(pos, mradius, lerp));
                    break;
                }
                pos = Tmp.v1.trns(fangle, radius).cpy();
            } else {
                maxAngle = 360f - ModMath.atan((mradius) / radius);
                Log.info("maxAngle: @",maxAngle);
                firstO.trns(maxAngle, radius);
            }
            couple3s.add(Couple3.of(pos, mradius, lerp));
            lastPos = pos.cpy();
            lastAngle = mradius;
        }
        while (couple3s.size>0){
            Couple3<Vec2, Float, Color> c = couple3s.random();
            ModFx.teleportSircle.at(x + c.o1.x, y + c.o1.y, c.o2, c.o3);
            couple3s.remove(c);
        }
    }
}
