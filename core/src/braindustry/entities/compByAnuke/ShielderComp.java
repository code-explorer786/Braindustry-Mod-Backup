package braindustry.entities.compByAnuke;
import mindustry.gen.Damagec;
import mindustry.gen.Posc;
import mindustry.gen.Teamc;

import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

@braindustry.annotations.ModAnnotations.Component
abstract class ShielderComp implements Damagec, Teamc, Posc{
   void absorb() {
   }
}