package mindustry.entities.comp;

import arc.graphics.g2d.TextureRegion;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unitc;
import mindustry.ui.Cicon;

abstract class BlockUnitComp implements Unitc {
   Team team;
   transient Building tile;

   public void tile(Building tile) {
      this.tile = tile;
      this.maxHealth((float)tile.block.health);
      this.health(tile.health());
      this.hitSize((float)(tile.block.size * 8) * 0.7F);
      this.set(tile);
   }

   public void update() {
      if (this.tile != null) {
         this.team = this.tile.team;
      }

   }

   public TextureRegion icon() {
      return this.tile.block.icon(Cicon.full);
   }

   public void killed() {
      this.tile.kill();
   }

   public void damage(float v, boolean b) {
      this.tile.damage(v, b);
   }

   public boolean dead() {
      return this.tile == null || this.tile.dead();
   }

   public boolean isValid() {
      return this.tile != null && this.tile.isValid();
   }

   public void team(Team team) {
      if (this.tile != null && this.team != team) {
         this.team = team;
         if (this.tile.team != team) {
            this.tile.team(team);
         }
      }

   }
}
