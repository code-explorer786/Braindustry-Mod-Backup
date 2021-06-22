package braindustry.entities.compByAnuke;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Nullable;
import arc.util.Time;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.content.Fx;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Drawc;
import mindustry.gen.Itemsc;
import mindustry.gen.Posc;
import mindustry.gen.Rotc;
import mindustry.gen.Teamc;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.world.Tile;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.type.*;
import mindustry.world.*;

import static mindustry.Vars.*;

@braindustry.annotations.ModAnnotations.Component
abstract class MinerComp implements Itemsc, Posc, Teamc, Rotc, Drawc{
    @braindustry.annotations.ModAnnotations.Import float x, y, rotation, hitSize;
    @braindustry.annotations.ModAnnotations.Import UnitType type;

    transient float mineTimer;
    @Nullable @braindustry.annotations.ModAnnotations.SyncLocal Tile mineTile;
   public boolean canMine(Item item) {
      return this.type.mineTier >= item.hardness;
   }

   public boolean offloadImmediately() {
      return ((Unit)this.self()).isPlayer();
   }

   boolean mining() {
      return this.mineTile != null && !((Unit)this.self()).activelyBuilding();
   }

   public boolean validMine(Tile tile, boolean checkDst) {
      return tile != null && tile.block() == Blocks.air && (this.within(tile.worldx(), tile.worldy(), 70.0F) || !checkDst) && tile.drop() != null && this.canMine(tile.drop());
   }

   public boolean validMine(Tile tile) {
      return this.validMine(tile, true);
   }

   public boolean canMine() {
      return this.type.mineSpeed > 0.0F && this.type.mineTier >= 0;
   }

   public void update() {
      Building core = this.closestCore();
      if (core != null && this.mineTile != null && this.mineTile.drop() != null && !this.acceptsItem(this.mineTile.drop()) && this.within(core, 220.0F) && !this.offloadImmediately()) {
         int accepted = core.acceptStack(this.item(), this.stack().amount, this);
         if (accepted > 0) {
            Call.transferItemTo((Unit)this.self(), this.item(), accepted, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
            this.clearItem();
         }
      }

      if (!this.validMine(this.mineTile)) {
         this.mineTile = null;
         this.mineTimer = 0.0F;
      } else if (this.mining()) {
         Item item = this.mineTile.drop();
         this.mineTimer += Time.delta * this.type.mineSpeed;
         if (Mathf.chance(0.06D * (double)Time.delta)) {
            Fx.pulverizeSmall.at(this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), 0.0F, item.color);
         }

         if (this.mineTimer >= 50.0F + (float)item.hardness * 15.0F) {
            this.mineTimer = 0.0F;
            if (Vars.state.rules.sector != null && this.team() == Vars.state.rules.defaultTeam) {
               Vars.state.rules.sector.info.handleProduction(item, 1);
            }

            if (core != null && this.within(core, 220.0F) && core.acceptStack(item, 1, this) == 1 && this.offloadImmediately()) {
               if (this.item() == item && !Vars.net.client()) {
                  this.addItem(item);
               }

               Call.transferItemTo((Unit)this.self(), item, 1, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), core);
            } else if (this.acceptsItem(item)) {
               InputHandler.transferItemToUnit(item, this.mineTile.worldx() + Mathf.range(4.0F), this.mineTile.worldy() + Mathf.range(4.0F), this);
            } else {
               this.mineTile = null;
               this.mineTimer = 0.0F;
            }
         }

         if (!Vars.headless) {
            Vars.control.sound.loop(this.type.mineSound, this, this.type.mineSoundVolume);
         }
      }

   }

   public void draw() {
      if (this.mining()) {
         float focusLen = this.hitSize / 2.0F + Mathf.absin(Time.time, 1.1F, 0.5F);
         float swingScl = 12.0F;
         float swingMag = 1.0F;
         float flashScl = 0.3F;
         float px = this.x + Angles.trnsx(this.rotation, focusLen);
         float py = this.y + Angles.trnsy(this.rotation, focusLen);
         float ex = this.mineTile.worldx() + Mathf.sin(Time.time + 48.0F, swingScl, swingMag);
         float ey = this.mineTile.worldy() + Mathf.sin(Time.time + 48.0F, swingScl + 2.0F, swingMag);
         Draw.z(115.1F);
         Draw.color(Color.lightGray, Color.white, 1.0F - flashScl + Mathf.absin(Time.time, 0.5F, flashScl));
         Drawf.laser(this.team(), Core.atlas.find("minelaser"), Core.atlas.find("minelaser-end"), px, py, ex, ey, 0.75F);
         if (this.isLocal()) {
            Lines.stroke(1.0F, Pal.accent);
            Lines.poly(this.mineTile.worldx(), this.mineTile.worldy(), 4, 4.0F * Mathf.sqrt2, Time.time);
         }

         Draw.color();
      }
   }
}