package braindustry.entities.abilities;

import arc.math.geom.Vec2;
import arc.util.Tmp;
import braindustry.gen.PowerGeneratorc;
import braindustry.type.ModUnitType;
import mindustry.gen.Unit;

public class BlackHoleReactorAbility extends PowerGeneratorAbility {
    public float blackHoleHitSize;

    public BlackHoleReactorAbility(ModUnitType unitType, float range, float powerProduction, int maxNodes, float blackHoleHitSize, Vec2 reactorOffset) {
        super(unitType, range, powerProduction, maxNodes, reactorOffset);
        this.blackHoleHitSize = blackHoleHitSize;
    }

    public BlackHoleReactorAbility(ModUnitType unitType, float range, float powerProduction, int maxNodes, float blackHoleHitSize) {
        this(unitType, range, powerProduction, maxNodes, blackHoleHitSize, Vec2.ZERO.cpy());
    }

    @Override
    public void drawReactor(Unit unit) {
        float rotation = unit.rotation;
        Vec2 reactorPos = Tmp.v1.set(reactorOffset).rotate(rotation).add(unit);
        if (unit instanceof PowerGeneratorc) {
            unit.<PowerGeneratorc>as().blackHoleDrawer().drawBlackHole(reactorPos.x, reactorPos.y, 0, 1, blackHoleHitSize);
        }
    }
}
