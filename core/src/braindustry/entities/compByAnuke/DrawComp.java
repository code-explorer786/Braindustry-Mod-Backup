package braindustry.entities.compByAnuke;

import mindustry.annotations.Annotations.*;
import mindustry.gen.*;


@braindustry.annotations.ModAnnotations.Component
abstract class DrawComp implements Posc {

    float clipSize() {
        return Float.MAX_VALUE;
    }

    void draw() {
    }
}