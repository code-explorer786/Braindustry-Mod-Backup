package mindustry.entities.comp;

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

abstract class WaterMoveComp implements Posc, Velc, Hitboxc, Flyingc, Unitc {
   float x;
   float y;
   float rotation;
   UnitType type;
   private transient Trail tleft = new Trail(1);
   private transient Trail tright = new Trail(1);
   private transient Color trailColor;

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

   public SolidPred solidity() {
      return this.isFlying() ? null : EntityCollisions::waterSolid;
   }

   public float floorSpeedMultiplier() {
      Floor on = this.isFlying() ? Blocks.air.asFloor() : this.floorOn();
      return on.isDeep() ? 1.3F : 1.0F;
   }

   public boolean onLiquid() {
      Tile tile = this.tileOn();
      return tile != null && tile.floor().isLiquid;
   }
}
