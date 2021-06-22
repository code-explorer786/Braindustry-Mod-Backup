package braindustry.entities.compByAnuke;
import arc.func.Cons;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.Vars;
import mindustry.entities.EntityGroup;
import mindustry.gen.Entityc;
import mindustry.gen.Unitc;

import arc.func.*;
import arc.util.io.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
@braindustry.annotations.ModAnnotations.BaseComponent
abstract class EntityComp{
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

@braindustry.annotations.ModAnnotations.MethodPriority(1)
@braindustry.annotations.ModAnnotations.InternalImpl
   void read(Reads read) {
      this.afterRead();
   }

   void write(Writes write) {
   }

   void afterRead() {
   }
}