package braindustry.entities.compByAnuke;

import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;


@braindustry.annotations.ModAnnotations.Component
abstract class TimedComp implements Entityc, Scaled {

    float time, lifetime;

    // called last so pooling and removal happens then.
    @braindustry.annotations.ModAnnotations.MethodPriority(100)
    @Override
    public void update() {
        time = Math.min(time + Time.delta, lifetime);
        if (time >= lifetime) {
            remove();
        }
    }

    @Override
    public float fin() {
        return time / lifetime;
    }
}