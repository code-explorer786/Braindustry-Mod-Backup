package mindustryAddition.graphics;

import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Tmp;

public class ModGeometry {
    private static Vec2 vec2(float x,float y){
        return new Vec2(x,y);
    }
    public static Seq<Vec2> rectPoints(float x, float y, float width, float height, float rot){
        float vw = width / 2f;
        float vh = height / 2f;
        Seq<Vec2> points=Seq.with(vec2(-vw,-vh),vec2(vw,-vh),vec2(vw,vh),vec2(-vw,vh),vec2(-vw,-vh));
        Seq<Vec2> cords=new Seq<>();
        points.each((point)->{
            cords.add(point.cpy().rotate(rot).add(x+vw,y+vh));
        });
//        if (cords.size>5)cords.remove(cords.size-1);
        return cords;
    }
    public static Seq<Vec2> rectPoints(Rect rect, float rot){
        return rectPoints(rect.x,rect.y,rect.width,rect.height,rot);
    }

    public static Vec2 sqrtByAngle(float size, float angle,Vec2 vec2) {
        int angleOffset = (int) (angle / 90);
        float v1x=Tmp.v1.x,v1y=Tmp.v1.y,v2x=Tmp.v2.x,v2y=Tmp.v2.y;
        Tmp.v1.trns(angle, 100f);
        float na = angle % 90;
        Tmp.v2.set(Tmp.v1).rotate(-angleOffset * 90);
        float sx = Tmp.v2.x, sy = Tmp.v2.y;
        if (na <= 45) {
            Tmp.v2.scl(size / sx, size / sx);
        } else {
            Tmp.v2.scl(size / sy, size / sy);
        }
        Tmp.v2.rotate(angleOffset * 90);
        vec2.set(Tmp.v2);
        Tmp.v1.set(v1x,v1y);
        Tmp.v2.set(v2x,v2y);
        return vec2;
    }
}
