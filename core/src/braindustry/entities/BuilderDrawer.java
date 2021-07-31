package braindustry.entities;

import arc.math.geom.Vec2;
import mindustry.gen.Buildingc;

public interface BuilderDrawer extends Buildingc {
    void drawer();

    Vec2 getPos();
}
