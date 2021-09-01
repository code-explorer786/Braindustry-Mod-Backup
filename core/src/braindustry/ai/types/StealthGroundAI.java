package braindustry.ai.types;

import arc.func.Prov;
import arc.math.Mathf;
import arc.struct.IntQueue;
import arc.struct.IntSeq;
import braindustry.gen.Stealthc;
import mindustry.Vars;
import mindustry.ai.Pathfinder;
import mindustry.ai.types.FlyingAI;
import mindustry.ai.types.GroundAI;
import mindustry.entities.Predict;
import mindustry.entities.Units;
import mindustry.entities.units.AIController;
import mindustry.game.Team;
import mindustry.gen.Teamc;
import mindustry.world.Tile;
import mindustry.world.blocks.units.RepairPoint;
import mindustry.world.meta.BlockFlag;

public class StealthGroundAI extends AIController {
    public static int fieldRepair = 2;
    static Prov<Pathfinder.Flowfield> dumpObj = StealthFlowfield::new;

    static {
            if (Pathfinder.fieldTypes.size + 1 <= 5) {
                Pathfinder.fieldTypes.add(dumpObj);
            } else if (!Pathfinder.fieldTypes.contains(dumpObj)) {
                throw new RuntimeException("Can't add StealthFlowfield in Pathfinder.fieldTypes");
            }
            fieldRepair = Pathfinder.fieldTypes.indexOf(dumpObj);

    }

    public void updateUnit() {
        if (unit instanceof Stealthc) ((Stealthc) unit).healing(false);
        if (this.useFallback() && (this.fallback != null || (this.fallback = this.fallback()) != null)) {

            if (this.fallback.unit() != this.unit) {
                this.fallback.unit(this.unit);
            }

            this.fallback.updateUnit();
        } else {
            this.updateVisuals();
            this.updateTargeting();
            this.updateMovement();
        }
    }

    @Override
    public void updateMovement() {

        Stealthc sunit = (Stealthc) this.unit;
        Teamc target = targetFlag(unit.x, unit.y, BlockFlag.repair, false);
        float radius = 8f;
        checkRadius:
        {
            try {
                if (target instanceof RepairPoint.RepairPointBuild) {
                    radius = ((RepairPoint) ((RepairPoint.RepairPointBuild) target).block).repairRadius * 0.75f;
                    break checkRadius;
                }
                Class<?> c = target.getClass();

                radius = (float) c.getField("repairRadius").get(target);
            } catch (Exception ex) {

            }
        }
        if (target != null && !unit.within(target, radius)) {
            pathfind(fieldRepair);
        } else if (target != null) {
            sunit.healing(true);
        }
        if (unit.type.canBoost && !unit.onSolid()) {
            unit.elevation = Mathf.approachDelta(unit.elevation, 0f, 0.08f);
        }
        if (!Units.invalidateTarget(target, unit, unit.range()) && unit.type.rotateShooting) {
            if (unit.type.hasWeapons()) {
                unit.lookAt(Predict.intercept(unit, target, unit.type.weapons.first().bullet.speed));
            }
        } else if (unit.moving()) {
            unit.lookAt(unit.vel().angle());
        }
    }

    @Override
    public boolean useFallback() {
        Teamc target = targetFlag(unit.x, unit.y, BlockFlag.repair, false);
        return !(this.unit instanceof Stealthc && ((Stealthc) unit).mustHeal()) || target == null;
    }

    @Override
    public AIController fallback() {
        return unit.type.flying ? new FlyingAI() : new GroundAI();
    }

    @Override
    public boolean shouldShoot() {
        return (this.unit instanceof Stealthc && !((Stealthc) unit).inStealth());
    }

    protected static class StealthFlowfield extends Pathfinder.Flowfield {
        @Override
        protected void getPositions(IntSeq out) {

            for (Tile other : Vars.indexer.getAllied(this.team, BlockFlag.repair)) {
                out.add(other.pos());
            }
        }
    }
}
