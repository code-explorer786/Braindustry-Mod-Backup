package braindustry.entities.compByAnuke;
import arc.func.Boolf;
import arc.math.geom.Vec3;
import arc.struct.Seq;
import arc.util.Nullable;
import arc.util.Structs;
import java.util.Iterator;
import mindustry.Vars;
import mindustry.ai.formations.DistanceAssignmentStrategy;
import mindustry.ai.formations.Formation;
import mindustry.ai.formations.FormationPattern;
import mindustry.ai.types.FormationAI;
import mindustry.entities.Units;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Entityc;
import mindustry.gen.Posc;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;

import arc.func.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.ai.formations.*;
import mindustry.ai.types.*;
import mindustry.annotations.Annotations.*;
import mindustry.entities.*;
import mindustry.entities.units.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.type.*;

/** A unit that can command other units. */
@braindustry.annotations.ModAnnotations.Component
abstract class CommanderComp implements Entityc, Posc{
    private static final Seq<FormationMember> members = new Seq<>();
    private static final Seq<Unit> units = new Seq<>();

    @braindustry.annotations.ModAnnotations.Import float x, y, rotation, hitSize;
    @braindustry.annotations.ModAnnotations.Import Team team;
    @braindustry.annotations.ModAnnotations.Import UnitType type;

    transient @Nullable Formation formation;
    transient Seq<Unit> controlling = new Seq<>(10);
    /** minimum speed of any unit in the formation. */
    transient float minFormationSpeed;
   public void update() {
      if (this.controlling.isEmpty() && !Vars.net.client()) {
         this.formation = null;
      }

      if (this.formation != null) {
         this.formation.anchor.set(this.x, this.y, 0.0F);
         this.formation.updateSlots();
         this.controlling.removeAll((u) -> {
            boolean var10000;
            if (!u.dead) {
               UnitController ai$temp = u.controller();
               FormationAI ai;
               if (ai$temp instanceof FormationAI && (ai = (FormationAI)ai$temp) == (FormationAI)ai$temp && ai.leader == this.self()) {
                  var10000 = false;
                  return var10000;
               }
            }

            var10000 = true;
            return var10000;
         });
      }

   }

   public void remove() {
      this.clearCommand();
   }

   public void killed() {
      this.clearCommand();
   }

   public void controller(UnitController next) {
      this.clearCommand();
   }

   void commandNearby(FormationPattern pattern) {
      this.commandNearby(pattern, (u) -> {
         return true;
      });
   }

   void commandNearby(FormationPattern pattern, Boolf include) {
      Formation formation = new Formation(new Vec3(this.x, this.y, this.rotation), pattern);
      formation.slotAssignmentStrategy = new DistanceAssignmentStrategy(pattern);
      units.clear();
      Units.nearby(this.team, this.x, this.y, 150.0F, (u) -> {
         if (u.isAI() && include.get(u) && u != this.self() && u.type.flying == this.type.flying && u.hitSize <= this.hitSize * 1.1F) {
            units.add(u);
         }

      });
      if (!units.isEmpty()) {
         units.sort(Structs.comps(Structs.comparingFloat((u) -> {
            return -u.hitSize;
         }), Structs.comparingFloat((u) -> {
            return u.dst2(this);
         })));
         units.truncate(this.type.commandLimit);
         this.command(formation, units);
      }
   }

   void command(Formation formation, Seq units) {
      this.clearCommand();
      units.shuffle();
      float spacing = this.hitSize * 0.8F;
      this.minFormationSpeed = this.type.speed;
      this.controlling.addAll(units);

      Iterator var4;
      Unit unit;
      for(var4 = units.iterator(); var4.hasNext(); this.minFormationSpeed = Math.min(this.minFormationSpeed, unit.type.speed)) {
         unit = (Unit)var4.next();
         FormationAI ai;
         unit.controller(ai = new FormationAI((Unit)this.self(), formation));
         spacing = Math.max(spacing, ai.formationSize());
      }

      this.formation = formation;
      formation.pattern.spacing = spacing;
      members.clear();
      var4 = units.iterator();

      while(var4.hasNext()) {
         Unitc u = (Unitc)var4.next();
         members.add((FormationAI)u.controller());
      }

      formation.addMembers(members);
   }

   boolean isCommanding() {
      return this.formation != null;
   }

   void clearCommand() {
      Iterator var1 = this.controlling.iterator();

      while(var1.hasNext()) {
         Unit unit = (Unit)var1.next();
         if (unit.controller().isBeingControlled((Unit)this.self())) {
            unit.controller(unit.type.createController());
         }
      }

      this.controlling.clear();
      this.formation = null;
   }
}