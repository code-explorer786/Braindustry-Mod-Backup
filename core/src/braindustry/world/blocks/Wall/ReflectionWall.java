package braindustry.world.blocks.Wall;

import arc.util.Time;
import arc.util.Tmp;
import mindustry.core.World;
import mindustry.entities.Damage;
import mindustry.entities.bullet.LaserBulletType;
import mindustry.entities.bullet.LightningBulletType;
import mindustry.gen.Bullet;
import mindustry.world.Tile;
import mindustry.world.blocks.defense.Wall;

public class ReflectionWall extends Wall {
    public boolean laserReflect = false;
    public boolean lightningReflect = false;

    public ReflectionWall(String name) {
        super(name);
        absorbLasers = true;
        insulated = true;
    }

    public class ReflectionWallBuild extends WallBuild {
        public void reflectLightning(){

        }
        @Override
        public boolean collision(Bullet bullet) {

            if (bullet.team == team) return false;
            if (bullet.type instanceof LaserBulletType && laserReflect) {

                LaserBulletType type = (LaserBulletType) bullet.type;
                float length = Damage.findLaserLength(bullet, type.length) * 0.75f;
                float angle = bullet.rotation();
                Tmp.v2.trns(bullet.rotation() + 135f, length, length);
                Tmp.v1.set(bullet.x - Tmp.v2.x, bullet.y - Tmp.v2.y);
                Bullet b = bullet.type.create(
                        this,team,Tmp.v1.x,Tmp.v1.y,0, bullet.damage,
                        1,1,null
                );
                b.lifetime = bullet.lifetime;
                b.time = bullet.time;
                float rotate = (180f - angle) * 2f;
                b.vel.set(bullet.vel.cpy()).rotate(rotate);
            }

            if (bullet.data instanceof Bullet) {
                Bullet dataBullet = (Bullet) bullet.data;
                if (dataBullet.type instanceof LightningBulletType && lightningReflect) {
                    LightningBulletType type = (LightningBulletType) dataBullet.type;
                    float angle = dataBullet.rotation();
                    float rotate = (180f - angle) * 2f;
                    dataBullet.type.create(
                            this,team,bullet.x,bullet.y,(angle+rotate)%360, type.damage,
                            1,1,null
                    );
                    return super.collision(bullet);
                }
            }

            return super.collision(bullet);
        }
    }
}
