const eclipseEffect = newEffect(10, e => {
Draw.color(Color.white, Pal.lancerLaser, e.fin());

Drawf.tri(e.x, e.y, 20 * e.fout(), (140 + 50), e.rotation);
Drawf.tri(e.x, e.y, 20 * e.fout(), 10, e.rotation + 180);
Draw.reset();
});

const eclipseBullet = extend(BasicBulletType, {
update: function(b){
if(b.timer.get(1, 17)){
Damage.collideLine(b, b.getTeam(), this.hitEffect, b.x, b.y, b.rot(), this.rayLength, false);
};
},

draw: function(b){
Draw.color(Color.white, Pal.lancerLaser, b.fin());

for(var i = 0; i < 7; i++){
Tmp.v1.trns(b.rot(), i * 8);
var sl = Mathf.clamp(b.fout() - 0.5) * (80 - i * 10);
Drawf.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, 4, sl, b.rot() + 90);
Drawf.tri(b.x + Tmp.v1.x, b.y + Tmp.v1.y, 4, sl, b.rot() - 90);
}
Drawf.tri(b.x, b.y, 20 * b.fout(), (this.rayLength + 30), b.rot());
Drawf.tri(b.x, b.y, 20 * b.fout(), 10, b.rot() + 180);
Draw.reset();
}
});

eclipseBullet.speed = 0.02;
eclipseBullet.damage = 155;
eclipseBullet.lifetime = 20;
eclipseBullet.collidesTeam = false;
eclipseBullet.pierce = true;
eclipseBullet.rayLength = 150 + 20;
eclipseBullet.hitEffect = Fx.hitLancer;
eclipseBullet.despawnEffect = Fx.none;
eclipseBullet.shootEffect = eclipseEffect;
eclipseBullet.smokeEffect = Fx.lightningShoot;

const eclipse = extendContent(ItemTurret, "eclipse", {
init(){
this.ammo(Vars.content.getByName(ContentType.item,"braindustry-graphenite"),eclipseBullet)
this.super$init()
}
});

eclipse.shootShake = 4;
eclipse.recoil = 5;
eclipse.shots = 3;
eclipse.shootEffect = Fx.lightningShoot
