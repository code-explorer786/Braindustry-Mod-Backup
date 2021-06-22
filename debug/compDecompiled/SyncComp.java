package mindustry.entities.comp;

import arc.util.io.Reads;
import arc.util.io.Writes;
import java.nio.FloatBuffer;
import mindustry.Vars;
import mindustry.gen.Entityc;

abstract class SyncComp implements Entityc {
   transient long lastUpdated;
   transient long updateSpacing;

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
