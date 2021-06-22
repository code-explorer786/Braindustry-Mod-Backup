package mindustry.entities.comp;

import arc.util.Time;
import mindustry.game.Team;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import mindustry.world.blocks.units.ResupplyPoint;

abstract class AmmoDistributeComp implements Unitc {
   float x;
   float y;
   UnitType type;
   Team team;
   float ammo;
   private transient float ammoCooldown;

   public void update() {
      if (this.ammoCooldown > 0.0F) {
         this.ammoCooldown -= Time.delta;
      }

      if (this.ammo > 0.0F && this.ammoCooldown <= 0.0F && ResupplyPoint.resupply(this.team, this.x, this.y, this.type.ammoResupplyRange, Math.min((float)this.type.ammoResupplyAmount, this.ammo), this.type.ammoType.color, (u) -> {
         return u != this.self();
      })) {
         this.ammo -= Math.min((float)this.type.ammoResupplyAmount, this.ammo);
         this.ammoCooldown = 5.0F;
      }

   }
}
