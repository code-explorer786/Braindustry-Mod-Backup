package braindustry.entities.comp;

import arc.graphics.g2d.Draw;
import arc.math.geom.Rect;
import arc.math.geom.Vec2;
import arc.util.io.Reads;
import mindustry.annotations.Annotations;
import braindustry.gen.BDCall;
import braindustry.gen.UnitEntry;
import braindustry.gen.UnitEntryc;
import mindustry.game.Team;
import mindustry.gen.Drawc;
import mindustry.gen.Entityc;
import mindustry.gen.Syncc;
import mindustry.type.UnitType;
import braindustry.graphics.Drawm;
import mindustry.annotations.Annotations;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

@Annotations.EntityDef(value = {UnitEntryc.class},serialize = false)
@Annotations.Component(base = true)
public abstract class UnitEntryComp implements Drawc, Entityc, Syncc {
    private static final Rect viewport = new Rect();
    public int amount;
    public Vec2 pos=new Vec2(
    
    );
    public Team team;
    private UnitType unitType;
public float posX(){
    return pos.x;
}
public float posY(){
    return pos.y;
}
    public static UnitEntry create(UnitType unitType, Team team, int amount, Vec2 pos) {
        if (unitType == null) throw new IllegalArgumentException("UnitType cannot be null");
        UnitEntry unitEntry = UnitEntry.create();
        unitEntry.unitType = (unitType);
        unitEntry.amount = amount;
        unitEntry.pos = pos;
        unitEntry.team = team;
        return unitEntry;
    }

    public static UnitEntry readEntry(Reads reads) {
        UnitEntry unitEntry = UnitEntry.create();
        unitEntry.read(reads);
        return unitEntry;
    }
    public static UnitEntry readEntry(Reads reads,int revision) {
        return readEntry(reads);
    }

    @Override
    @Annotations.Replace
    public float clipSize() {
        if (world != null) {
            return Math.max(world.width(), world.height()) * tilesize;
        }
        return Float.MAX_VALUE;
    }

    public UnitType unitType() {
        if (unitType == null) throw new IllegalArgumentException("UnitType cannot be null");
        return unitType;
    }

    @Override
    public void update() {
        if (unitType == null) {
            remove();
        }
        set(world.width() / 2f * tilesize, world.height() / 2f * tilesize);
    }

    @Override
    public boolean equals(Object obj) {
        UnitEntry entry = obj instanceof UnitEntry ? (UnitEntry) obj : null;
        if (entry == null) return super.equals(obj);

        return super.equals(obj) || customEquals(entry);
    }

    private boolean customEquals(UnitEntry entry) {
        return entry.unitType == unitType &&
               entry.amount == amount &&
               entry.pos == pos &&
               entry.team == team
                ;
    }

    public void draw() {
        Draw.reset();
        Draw.color(team.color);
        Draw.alpha(0.5f);
        Draw.rect(unitType.region, pos.x, pos.y);
        if (amount > 1) Drawm.drawLabel(pos, amount + "");
        Draw.reset();
    }

    public void spawn() {
        BDCall.spawnUnits(unitType, pos.x, pos.y, amount, false, team);
    }

}
