package braindustry.entities.compByAnuke;
import mindustry.gen.Entityc;

import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class RotComp implements Entityc{

    @braindustry.annotations.ModAnnotations.SyncField(false) @braindustry.annotations.ModAnnotations.SyncLocal float rotation;
}