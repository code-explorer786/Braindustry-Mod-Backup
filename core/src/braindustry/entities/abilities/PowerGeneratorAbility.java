package braindustry.entities.abilities;

import arc.Core;
import arc.func.Boolf2;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import braindustry.type.ModUnitType;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;

import static braindustry.BDVars.fullName;

public abstract class PowerGeneratorAbility extends ModAbility {
    public TextureRegion bottomRegion;

    public final ModUnitType unitType;
    public final float laserRange;
    public Boolf2<Building, Unit> good = (building, unit) -> {
        if (building == null) return false;
        return building.block.hasPower && building.team == unit.team /*&& building.block instanceof ReceivingPowerNode*/;
    };

    public final float powerProduction;
    public final int maxNodes;
    private UnitPowerGenerator generatorBlock;
    private UnitPowerNode nodeBlock;
    public final Vec2 reactorOffset;

    public PowerGeneratorAbility(ModUnitType unitType, float laserRange, float powerProduction, int maxNodes, Vec2 reactorOffset) {
        this.unitType = unitType;
        this.laserRange = laserRange;
        this.powerProduction = powerProduction;
        this.maxNodes = maxNodes;
        this.reactorOffset = reactorOffset;
        drawBody=true;
    }

    public UnitPowerGenerator generatorBlock() {
        return generatorBlock;
    }

    public UnitPowerNode nodeBlock() {
        return nodeBlock;
    }

    public abstract void drawReactor(Unit unit);

    public void drawLaser(Team team, float x1, float y1, float x2, float y2, int size1, int size2) {
        float angle1 = Angles.angle(x1, y1, x2, y2);
        float vx = Mathf.cosDeg(angle1);
        float vy = Mathf.sinDeg(angle1);
        float len1 = (float) (size1 * 8) / 2.0F - 1.5F;
        float len2 = (float) (size2 * 8) / 2.0F - 1.5F;
        TextureRegion laser = Core.atlas.find(fullName("laser"), "laser"),
        laserEnd = Core.atlas.find(fullName("laser-end"),"laser-end");
        Drawf.laser(team, laser, laserEnd, x1 + vx * len1, y1 + vy * len1, x2 - vx * len2, y2 - vy * len2, 0.25F);
    }
    public void drawOutline(Unit unit){
        Draw.reset();

        if(Core.atlas.isFound(unitType.outlineRegion)){
            Draw.rect(unitType.outlineRegion, unit.x, unit.y, unit.rotation - 90);
        }
    }
    public void drawBody(Unit unit) {
        unitType.applyColor(unit);
        Draw.rect(this.bottomRegion, unit.x, unit.y);
        Draw.color();
        drawReactor(unit);
        unitType.applyColor(unit);
        drawOutline(unit);
        Draw.rect(unitType.region, unit.x, unit.y, unit.rotation - 90.0F);
        Draw.reset();
    }

    public void draw(Unit unit) {
        super.draw(unit);
//        drawReactor(unit);
    }

    @Override
    public void load() {
//        Log.info("load: @==@",unitType.name,getClass().getName());
        bottomRegion = Core.atlas.find(unitType.name + "-bottom");

    }
}
