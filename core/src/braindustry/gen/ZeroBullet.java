package braindustry.gen;

import mindustry.gen.Bullet;

public class ZeroBullet extends Bullet {
    @Override
    public void update() {
        vel.setZero();
        x=y=0;
        time=0;
        lifetime=10;

        super.update();
    }

    @Override
    public int classId() {
        return ModEntityMapping.getId(getClass());
    }
}
