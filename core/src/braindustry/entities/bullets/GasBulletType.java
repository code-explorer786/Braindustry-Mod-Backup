package braindustry.entities.bullets;

import Gas.entities.Clouds;
import Gas.type.Gas;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.math.geom.Geometry;
import arc.math.geom.Point2;
import arc.util.Nullable;
import mindustry.content.Fx;
import mindustry.entities.Fires;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.Bullet;
import mindustry.world.Tile;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class GasBulletType extends BulletType {
    public Gas gas;
    public float puddleSize = 6f;
    public float orbSize = 3f;

    public GasBulletType(@Nullable Gas gas){
        super(3.5f, 0);

        if(gas != null){
            this.gas = gas;
            this.status = gas.effect;
            lightColor = gas.lightColor;
            lightOpacity = gas.lightColor.a;
        }

        ammoMultiplier = 1f;
        lifetime = 34f;
        statusDuration = 60f * 2f;
        despawnEffect = Fx.none;
        hitEffect = Fx.hitLiquid;
        smokeEffect = Fx.none;
        shootEffect = Fx.none;
        drag = 0.001f;
        knockback = 0.55f;
    }

    public GasBulletType(){
        this(null);
    }

    @Override
    public float range(){
        return speed * lifetime / 2f;
    }

    @Override
    public void update(Bullet b){
        super.update(b);

        if(true){
            Tile tile = world.tileWorld(b.x, b.y);
            if(tile != null && Fires.has(tile.x, tile.y)){
                Fires.extinguish(tile, 100f);
                b.remove();
                hit(b);
            }
        }
    }

    @Override
    public void draw(Bullet b){
        Draw.color(gas.color, Color.white, b.fout() / 100f);

        Fill.circle(b.x, b.y, orbSize);
    }

    @Override
    public void despawned(Bullet b){
        super.despawned(b);

        //don't create liquids when the projectile despawns
        hitEffect.at(b.x, b.y, b.rotation(), gas.color);
    }

    @Override
    public void hit(Bullet b, float hitx, float hity){
        hitEffect.at(hitx, hity, gas.color);
        Clouds.deposit(world.tileWorld(hitx, hity), gas, puddleSize);

        if(gas.temperature <= 0.5f && gas.flammability < 0.3f){
            float intensity = 400f * puddleSize/6f;
            Fires.extinguish(world.tileWorld(hitx, hity), intensity);
            for(Point2 p : Geometry.d4){
                Fires.extinguish(world.tileWorld(hitx + p.x * tilesize, hity + p.y * tilesize), intensity);
            }
        }
    }
}
