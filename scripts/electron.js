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
electroContinuousLaserBullet.hitSize = 25;
electroContinuousLaserBullet.drawSize = 540;
electroContinuousLaserBullet.width = 50;
electroContinuousLaserBullet.largeHit = true;
electroContinuousLaserBullet.lifetime = 140;
electroContinuousLaserBullet.hitColor = Color.valueOf("f1fc58")
electroContinuousLaserBullet.incendAmount = 4;
electroContinuousLaserBullet.incendSpread = 10;
electroContinuousLaserBullet.incendChance = 0.7;
electroContinuousLaserBullet.lightColor = Color.valueOf("fbffcc")
electroContinuousLaserBullet.keepVelocity = true;
electroContinuousLaserBullet.collides = true;
electroContinuousLaserBullet.pierce = true;
electroContinuousLaserBullet.hittable = false;
electroContinuousLaserBullet.absorbable = false;
electroContinuousLaserBullet.damage = 74;
electroContinuousLaserBullet.shootEffect = Fx.railShoot;
electroContinuousLaserBullet.despawnEffect = Fx.railHit;
electroContinuousLaserBullet.hitEffect = Fx.hitMeltdown;
electroContinuousLaserBullet.knockback = 1;
electroContinuousLaserBullet.lightning = 8;//количество молний
electroContinuousLaserBullet.lightningLength = 20;//длина молнии
electroContinuousLaserBullet.lightningLengthRand = 25;//рандомное число от 0 до 50 будет прибавляться к длине молнии, то есть рандомизация длины
electroContinuousLaserBullet.lightningDamage = 32;//урон молнии
electroContinuousLaserBullet.lightningAngle = 5;//угол направления молний относительно угла пули
electroContinuousLaserBullet.lightningCone = 50;//рандомизация угла направления молний 
electroContinuousLaserBullet.lightningColor = Color.valueOf("f1fc58");

const electroMeltdown = extendContent(ItemTurret, "electron", {});
electroMeltdown.health = 5600;
electroMeltdown.size = 10;
electroMeltdown.rotateSpeed = 0.9;
electroMeltdown.shots = 1;
electroMeltdown.reloadTime = 190;
electroMeltdown.hasItems = true;
electroMeltdown.hasLiquids = true;
electroMeltdown.shootX = 10;
electroMeltdown.shootY = -12;
electroMeltdown.alternate = true;
electroMeltdown.range = 400;
electroMeltdown.localizedName = "Dendrite";
electroMeltdown.description = "Monstruous turret with Electric Laser.";
electroMeltdown.buildVisibility = BuildVisibility.shown;
electroMeltdown.requirements = ItemStack.with(Items.surgeAlloy, 890, Items.phaseFabric, 640, Vars.content.getByName(ContentType.item,"braindustry-graphenite"), 1400, Items.silicon, 2010, Vars.content.getByName(ContentType.item, "braindustry-exotic-alloy"), 1050);
electroMeltdown.category = Category.turret;
electroMeltdown.ammo(
    Vars.content.getByName(ContentType.item, "braindustry-exotic-alloy"), electroContinuousLaserBullet
);
