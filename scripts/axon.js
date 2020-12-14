const electroBasicBullet = extend(BasicBulletType, {
update(b){
if(b.fin()<0.001){
for(var i = 0; i < this.lightning; i++){
Lightning.create(b, this.lightningColor, this.lightningDamage < 0 ? this.damage : this.lightningDamage, b.x, b.y + 2, b.rotation() + Mathf.range(this.lightningCone) + this.lightningAngle, this.lightningLength + Mathf.random(this.lightningLengthRand));
}
}
this.super$update(b);
}
});
electroBasicBullet.backColor = Color.valueOf("c2cc37");
electroBasicBullet.width = 10;
electroBasicBullet.height = 10;
electroBasicBullet.shrinkY = 0.1;
electroBasicBullet.shrinkX = 0.3;
electroBasicBullet.spin = 3.5;
electroBasicBullet.speed = 4;
electroBasicBullet.damage = 43;
electroBasicBullet.shootEffect = Fx.railShoot;
electroBasicBullet.hitColor = electroBasicBullet.frontColor = Color.valueOf("f1fc58");
electroBasicBullet.despawnEffect = Fx.railHit;
electroBasicBullet.lifetime = 90;
electroBasicBullet.knockback = 1;
electroBasicBullet.lightning = 5;//количество молний
electroBasicBullet.lightningLength = 5;//длина молнии
electroBasicBullet.lightningLengthRand = 50;//рандомное число от 0 до 50 будет прибавляться к длине молнии, то есть рандомизация длины
electroBasicBullet.lightningDamage = 20;//урон молнии
electroBasicBullet.lightningAngle = 43;//угол направления молний относительно угла пули
electroBasicBullet.lightningCone = 32;//рандомизация угла направления молний 

const axon = extendContent(ItemTurret, "axon", {});
axon.health = 1260;
axon.size = 3;
axon.shots = 5;
axon.hasItems = true;
axon.hasLiquids = true;
axon.localizedName = "Axon";
axon.description = "Powerful Electric shotgun.";
axon.buildVisibility = BuildVisibility.shown;
axon.requirements = ItemStack.with(Items.titanium, 200, Vars.content.getByName(ContentType.item,"braindustry-graphenite"), 480, Items.silicon, 350, Vars.content.getByName(ContentType.item,"braindustry-exotic-alloy"), 140);
axon.category = Category.turret;
synaps.shootType = electroSapBullet;
axon.ammo(
    Vars.content.getByName(ContentType.item, "braindustry-exotic-alloy"), electroBasicBullet

);
