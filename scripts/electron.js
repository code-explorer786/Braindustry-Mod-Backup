const electroContinuousLaserBullet = extend(ContinuousLaserBulletType, {
update(b){
if(b.fin()<0.001){
for(var i = 0; i < this.lightning; i++){
Lightning.create(b, this.lightningColor, this.lightningDamage < 0 ? this.damage : this.lightningDamage, b.x, b.y + 2, b.rotation() + Mathf.range(this.lightningCone) + this.lightningAngle, this.lightningLength + Mathf.random(this.lightningLengthRand));
}
}
this.super$update(b);
}
});
//electroContinuousLaserBullet.colors = [Color.valueOf("c2cc37"), Color.valueOf("f1fc58"), Color.valueOf("fbffcc")];
electroContinuousLaserBullet.hitSize = 14;
electroContinuousLaserBullet.drawSize = 520;
electroContinuousLaserBullet.width = 34;
electroContinuousLaserBullet.length = 390;
electroContinuousLaserBullet.largeHit = true;
electroContinuousLaserBullet.hitColor = Color.valueOf("f1fc58");
electroContinuousLaserBullet.incendAmount = 4;
electroContinuousLaserBullet.incendSpread = 10;
electroContinuousLaserBullet.incendChance = 0.7;
electroContinuousLaserBullet.lightColor = Color.valueOf("fbffcc");
electroContinuousLaserBullet.keepVelocity = true;
electroContinuousLaserBullet.collides = true;
electroContinuousLaserBullet.pierce = true;
electroContinuousLaserBullet.hittable = true;
electroContinuousLaserBullet.absorbable = false;
electroContinuousLaserBullet.damage = 74;
electroContinuousLaserBullet.shootEffect = Fx.railShoot;
electroContinuousLaserBullet.despawnEffect = Fx.railHit;
electroContinuousLaserBullet.knockback = 1;
electroContinuousLaserBullet.lightning = 4;//количество молний
electroContinuousLaserBullet.lightningLength = 30;//длина молнии
electroContinuousLaserBullet.lightningLengthRand = 30;//рандомное число от 0 до 50 будет прибавляться к длине молнии, то есть рандомизация длины
electroContinuousLaserBullet.lightningDamage = 78;//урон молнии
electroContinuousLaserBullet.lightningAngle = 15;//угол направления молний относительно угла пули
electroContinuousLaserBullet.lightningCone = 50;//рандомизация угла направления молний 
electroContinuousLaserBullet.lightningColor = Color.valueOf("f1fc58");

const electroMeltdown = extendContent(LaserTurret, "electron", {
load(){
	    this.super$load();

	    this.baseRegion = Core.atlas.find("block-10");
    }, 
  
	icons(){
		return [
			Core.atlas.find("block-10");
      Core.atlas.find("electron");
		];
   }
});
electroMeltdown.health = 6460;
electroMeltdown.size = 10;
electroMeltdown.recoilAmount = 11;
electroMeltdown.shootShake = 4;
electroMeltdown.shootCone = 15;
electroMeltdown.rotateSpeed = 0.9;
electroMeltdown.shots = 1;
electroMeltdown.hasItems = true;
electroMeltdown.hasLiquids = true;
electroMeltdown.rotate = true;
electroMeltdown.shootDuration = 170;
electroMeltdown.powerUse = 42;
electroMeltdown.range = 400;
electroMeltdown.firingMoveFract = 0.2;
electroMeltdown.localizedName = "Dendrite";
electroMeltdown.description = "Monstruous turret with Electric Laser.";
electroMeltdown.buildVisibility = BuildVisibility.shown;
electroMeltdown.category = Category.turret;
electroMeltdown.shootType = electroContinuousLaserBullet;
electroMeltdown.baseRegion = Core.atlas.find("block-10");
electroMeltdown.icon = Core.atlas.find("electron");
new TechTree.TechNode(TechTree.all.find(boolf(t=>t.content.name == "synaps")), electroMeltdown, ItemStack.with());
