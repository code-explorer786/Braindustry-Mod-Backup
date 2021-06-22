package braindustry.entities.compByAnuke;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Posc;

import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class TeamComp implements Posc{
    @braindustry.annotations.ModAnnotations.Import float x, y;

    Team team = Team.derelict;
   TeamComp() {
      this.team = Team.derelict;
   }

   public boolean cheating() {
      return this.team.rules().cheat;
   }

   @Nullable
   public Building core() {
      return this.team.core();
   }

   @Nullable
   public Building closestCore() {
      return Vars.state.teams.closestCore(this.x, this.y, this.team);
   }

   @Nullable
   public Building closestEnemyCore() {
      return Vars.state.teams.closestEnemyCore(this.x, this.y, this.team);
   }
}