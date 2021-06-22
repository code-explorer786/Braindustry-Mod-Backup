package mindustry.entities.comp;

import arc.util.Nullable;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Posc;

abstract class TeamComp implements Posc {
   float x;
   float y;
   Team team;

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
