package braindustry.entities.compByAnuke;
import arc.graphics.g2d.TextureRegion;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unitc;
import mindustry.ui.Cicon;

import arc.graphics.g2d.*;
import mindustry.annotations.Annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.ui.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class BlockUnitComp implements Unitc{
    @braindustry.annotations.ModAnnotations.Import Team team;

    @braindustry.annotations.ModAnnotations.ReadOnly transient Building tile;
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

@braindustry.annotations.ModAnnotations.Replace
   public TextureRegion icon() {
      return this.tile.block.icon(Cicon.full);
   }

   public void killed() {
      this.tile.kill();
   }

@braindustry.annotations.ModAnnotations.Replace
   public void damage(float v, boolean b) {
      this.tile.damage(v, b);
   }

@braindustry.annotations.ModAnnotations.Replace
   public boolean dead() {
      return this.tile == null || this.tile.dead();
   }

@braindustry.annotations.ModAnnotations.Replace
   public boolean isValid() {
      return this.tile != null && this.tile.isValid();
   }

@braindustry.annotations.ModAnnotations.Replace
   public void team(Team team) {
      if (this.tile != null && this.team != team) {
         this.team = team;
         if (this.tile.team != team) {
            this.tile.team(team);
         }
      }

   }
}