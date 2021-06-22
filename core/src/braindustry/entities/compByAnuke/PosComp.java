package braindustry.entities.compByAnuke;
import arc.math.geom.Position;
import arc.util.Nullable;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import arc.math.geom.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.core.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class PosComp implements Position{
    @braindustry.annotations.ModAnnotations.SyncField(true) @braindustry.annotations.ModAnnotations.SyncLocal float x, y;
   void set(float x, float y) {
      this.x = x;
      this.y = y;
   }

   void set(Position pos) {
      this.set(pos.getX(), pos.getY());
   }

   void trns(float x, float y) {
      this.set(this.x + x, this.y + y);
   }

   void trns(Position pos) {
      this.trns(pos.getX(), pos.getY());
   }

   int tileX() {
      return World.toTile(this.x);
   }

   int tileY() {
      return World.toTile(this.y);
   }

   Floor floorOn() {
      Tile tile = this.tileOn();
      return tile != null && tile.block() == Blocks.air ? tile.floor() : (Floor)Blocks.air;
   }

   Block blockOn() {
      Tile tile = this.tileOn();
      return tile == null ? Blocks.air : tile.block();
   }

   boolean onSolid() {
      Tile tile = this.tileOn();
      return tile == null || tile.solid();
   }

   @Nullable
   Tile tileOn() {
      return Vars.world.tileWorld(this.x, this.y);
   }

   public float getX() {
      return this.x;
   }

   public float getY() {
      return this.y;
   }
}