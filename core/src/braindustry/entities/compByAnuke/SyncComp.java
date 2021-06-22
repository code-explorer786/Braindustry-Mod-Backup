package braindustry.entities.compByAnuke;
import arc.util.io.Reads;
import arc.util.io.Writes;
import java.nio.FloatBuffer;
import mindustry.Vars;
import mindustry.gen.Entityc;

import arc.util.io.*;
import mindustry.*;
import mindustry.annotations.Annotations.*;
import mindustry.gen.*;

import java.nio.*;

@braindustry.annotations.ModAnnotations.Component
abstract class SyncComp implements Entityc{
    transient long lastUpdated, updateSpacing;
   void snapSync() {
   }

   void snapInterpolation() {
   }

   void readSync(Reads read) {
   }

   void writeSync(Writes write) {
   }

   void readSyncManual(FloatBuffer buffer) {
   }

   void writeSyncManual(FloatBuffer buffer) {
   }

   void afterSync() {
   }

   void interpolate() {
   }

   public void update() {
      if (Vars.net.client() && !this.isLocal() || this.isRemote()) {
         this.interpolate();
      }

   }

   public void remove() {
      if (Vars.net.client()) {
         Vars.netClient.addRemovedEntity(this.id());
      }

   }
}