package braindustry.entities;

import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.gen.ModEntityMapping;
import mindustry.gen.UnitWaterMove;
import mindustry.world.Tile;

public class PowerGeneratorUnit extends UnitWaterMove {
    public PowerGeneratorUnit() {
        super();
    }

    public static PowerGeneratorUnit create() {
        return new PowerGeneratorUnit();
    }




    @Override
    public void afterSync() {
        super.afterSync();
    }

    @Override
    public void move(float cx, float cy) {
        super.move(cx, cy);
    }

    public String toString() {
        return "PowerGeneratorUnit#" + this.id;
    }




    @Override
    public void write(Writes write) {
        super.write(write);
    }

    @Override
    public void read(Reads read) {
        super.read(read);
    }

    public boolean serialize() {
        return true;
    }



    @Override
    public void clearCommand() {
        super.clearCommand();
    }

    @Override
    public void kill() {
        super.kill();
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public int classId() {
        return ModEntityMapping.getId(getClass());
    }
}
