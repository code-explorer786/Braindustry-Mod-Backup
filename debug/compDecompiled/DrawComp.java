package mindustry.entities.comp;

import mindustry.gen.Posc;

abstract class DrawComp implements Posc {
   float clipSize() {
      return Float.MAX_VALUE;
   }

   void draw() {
   }
}
