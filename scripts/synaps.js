const electroSapBullet = extend(SapBulletType, {
update(b){
if(b.fin()<0.001){
for(var i = 0; i < this.lightning; i++){
Lightning.create(b, this.lightningColor, this.lightningDamage < 0 ? this.damage : this.lightningDamage, b.x, b.y, b.rotation() + Mathf.range(this.lightningCone) + this.lightningAngle, this.lightningLength + Mathf.random(this.lightningLengthRand));
}
}
this.super$update(b);
}
});
electroSapBullet.sapStrength = 0.85;
electroSapBullet.length = 55;
electroSapBullet.damage = 37;
electroSapBullet.shootEffect = Fx.railShoot;
electroSapBullet.hitColor = electroSapBullet.color = Color.valueOf("fbff9e");
electroSapBullet.despawnEffect = Fx.railHit;
electroSapBullet.width = 0.55;
electroSapBullet.lifetime = 30;
electroSapBullet.knockback = -1;
electroSapBullet.lightning = 10;//количество молний
electroSapBullet.lightningLength = 5;//длина молнии
electroSapBullet.lightningLengthRand = 15;//рандомное число от 0 до 15 будет прибавляться к длине молнии, то есть рандомизация длины
electroSapBullet.lightningDamage = 50;//урон молнии
electroSapBullet.lightningAngle = 0;//угол направления молний относительно угла пули
electroSapBullet.lightningCone = 40;//рандомизация угла направления молний 
electroSapBullet.largeHit = true;
electroSapBullet.lightColor = electroSapBullet.lightningColor = Color.valueOf("fbff9e");

const synaps = extendContent(ItemTurret, "synaps", {});
synaps.health = 1200;
synaps.size = 3;
synaps.hasItems = true;
synaps.hasLiquids = true;
synaps.localizedName = "Synaps";
synaps.description = "Sap turret";
synaps.buildVisibility = BuildVisibility.shown;
synaps.requirements = ItemStack.with(Items.titanium, 200, Items.lead, 250, Items.silicon, 350, Vars.content.getByName(ContentType.item,"braindustry-odinum"), 150);
synaps.consumes.items(new ItemStack(Vars.content.getByName(ContentType.item,"braindustry-exotic-alloy"), 150));
synaps.category = Category.turret;
synaps.shootType = electroSapBullet
