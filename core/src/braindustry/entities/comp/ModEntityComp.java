package braindustry.entities.comp;

import braindustry.annotations.ModAnnotations;
import braindustry.gen.OrbitalPlatformOwnerc;
import braindustry.gen.PowerGeneratorc;
import braindustry.type.ModUnitType;
import mindustry.gen.Entityc;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;

@ModAnnotations.Component
abstract class ModEntityComp implements Entityc, Unitc {
    private transient ModUnitType modUnitType;

    public ModUnitType modUnitType() {
        return modUnitType;
    }

    @ModAnnotations.MethodPriority(10000)
    @Override
    public void setType(UnitType type) {
        type(type);
    }

    @Override
    public void type(UnitType type) {
        if (type instanceof ModUnitType) {
            modUnitType = (ModUnitType) type;
        } else throw new IllegalArgumentException("unitType cannot be a non-instance of " + ModUnitType.class);
        this.<Unit>self().type = type;
    }
}
