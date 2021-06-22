package braindustry.entities.compByAnuke;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.content.Blocks;
import mindustry.entities.EntityCollisions;
import mindustry.entities.EntityCollisions.SolidPred;
import mindustry.gen.Flyingc;
import mindustry.gen.Hitboxc;
import mindustry.gen.Posc;
import mindustry.gen.Unitc;
import mindustry.gen.Velc;
import mindustry.graphics.Trail;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.blocks.environment.Floor;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.ai.*;
import mindustry.annotations.Annotations.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.EntityCollisions.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.environment.*;

//just a proof of concept
@braindustry.annotations.ModAnnotations.Component
abstract class WaterMoveComp implements Posc, Velc, Hitboxc, Flyingc, Unitc{
    @braindustry.annotations.ModAnnotations.Import float x, y, rotation;
    @braindustry.annotations.ModAnnotations.Import UnitType type;

    private transient Trail tleft = new Trail(1), tright = new Trail(1);
    private transient Color trailColor = Blocks.water.mapColor.cpy().mul(1.5f);
   WaterMoveComp() {
      this.trailColor = Blocks.water.mapColor.cpy().mul(1.5F);
   }

   public void update() {
      for(int i = 0; i < 2; ++i) {
         Trail t = i == 0 ? this.tleft : this.tright;
         t.length = this.type.trailLength;
         int sign = i == 0 ? -1 : 1;
         float cx = Angles.trnsx(this.rotation - 90.0F, this.type.trailX * (float)sign, this.type.trailY) + this.x;
         float cy = Angles.trnsy(this.rotation - 90.0F, this.type.trailX * (float)sign, this.type.trailY) + this.y;
         t.update(cx, cy);
      }

   }

@braindustry.annotations.ModAnnotations.Replace
   public int pathType() {
      return 2;
   }

   public void add() {
      this.tleft.clear();
      this.tright.clear();
   }

   public void draw() {
      float z = Draw.z();
      Draw.z(20.0F);
      Floor floor = this.tileOn() == null ? Blocks.air.asFloor() : this.tileOn().floor();
      Color color = Tmp.c1.set(floor.mapColor.equals(Color.black) ? Blocks.water.mapColor : floor.mapColor).mul(1.5F);
      this.trailColor.lerp(color, Mathf.clamp(Time.delta * 0.04F));
      this.tleft.draw(this.trailColor, this.type.trailScl);
      this.tright.draw(this.trailColor, this.type.trailScl);
      Draw.z(z);
   }

@braindustry.annotations.ModAnnotations.Replace
   public SolidPred solidity() {
      return this.isFlying() ? null : EntityCollisions::waterSolid;
   }

@braindustry.annotations.ModAnnotations.Replace
   public float floorSpeedMultiplier() {
      Floor on = this.isFlying() ? Blocks.air.asFloor() : this.floorOn();
      return on.isDeep() ? 1.3F : 1.0F;
   }

   public boolean onLiquid() {
      Tile tile = this.tileOn();
      return tile != null && tile.floor().isLiquid;
   }
}