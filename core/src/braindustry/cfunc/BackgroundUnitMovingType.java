package braindustry.cfunc;

import arc.func.Cons2;
import arc.func.Floatc2;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import braindustry.graphics.ModMenuShaderRenderer;
import mindustry.content.Blocks;
import mindustry.graphics.Layer;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import static mindustry.Vars.world;

public enum BackgroundUnitMovingType {
    flying((renderer, cons) -> {
        float tw = (float) (renderer.width() * 8) * 1.0F + 8.0F;
        float th = (float) (renderer.height() * 8) * 1.0F + 8.0F;
        float range = 500.0F;
        float offset = -100.0F;
        float time = renderer.time();
        for (int i = 0; i < renderer.flyers(); ++i) {
            Tmp.v1.trns(renderer.flyerRot(), time * (2.0F + renderer.flyerType().speed));
            cons.get(
                    (Mathf.randomSeedRange((long) i, range) + Tmp.v1.x + Mathf.absin(time + Mathf.randomSeedRange((long) (i + 2), 500.0F), 10.0F, 3.4F) + offset) % (tw + (float) Mathf.randomSeed((long) (i + 5), 0, 500)),
                    (Mathf.randomSeedRange((long) (i + 1), range) + Tmp.v1.y + Mathf.absin(time + Mathf.randomSeedRange((long) (i + 3), 500.0F), 10.0F, 3.4F) + offset) % th);
        }
    }),
    naval((renderer, cons) -> {
        float tw = (float) (renderer.width() * 8) * 1.0F + 8.0F;
        float th = (float) (renderer.height() * 8) * 1.0F + 8.0F;
        float range = 500.0F;
        float offset = -100.0F;
        float time = renderer.time();
        float rotation = renderer.flyerRot();
        UnitType type = renderer.flyerType();
        for (int i = 0; i < renderer.flyers(); ++i) {

        }
    }),
    ;
    private final Cons2<ModMenuShaderRenderer, Floatc2> updater;

    BackgroundUnitMovingType(Cons2<ModMenuShaderRenderer, Floatc2> updater) {
        this.updater = updater;
    }

    public void update(ModMenuShaderRenderer renderer, Floatc2 floatc2) {
        updater.get(renderer, floatc2);
    }
}
