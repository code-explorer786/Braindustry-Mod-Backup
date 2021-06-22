package mindustry.entities.comp;

import arc.func.Cons;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.EntityGroup;
import mindustry.gen.Entityc;
import mindustry.gen.Unitc;

abstract class EntityComp {
   private transient boolean added;
   transient int id = EntityGroup.nextId();

   boolean isAdded() {
      return this.added;
   }

   void update() {
   }

   void remove() {
      this.added = false;
   }

   void add() {
      this.added = true;
   }

   boolean isLocal() {
      return this == Vars.player || this instanceof Unitc && ((Unitc)this).controller() == Vars.player;
   }

   boolean isRemote() {
      return this instanceof Unitc && ((Unitc)this).isPlayer() && !this.isLocal();
   }

   boolean isNull() {
      return false;
   }

   Entityc self() {
      return (Entityc)this;
   }

   Object as() {
      return this;
   }

   Object with(Cons cons) {
      cons.get(this);
      return this;
   }

   abstract int classId();

   abstract boolean serialize();

   void read(Reads read) {
      this.afterRead();
   }

   void write(Writes write) {
   }

   void afterRead() {
   }
}
