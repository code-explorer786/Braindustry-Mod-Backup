package braindustry.entities.comp;

import mindustry.annotations.Annotations;
import braindustry.type.ModUnitType;
import mindustry.gen.Entityc;
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;

@Annotations.Component
abstract class ModEntityComp implements Entityc, Unitc {
    private transient ModUnitType modUnitType;

    public ModUnitType modUnitType() {
        return modUnitType;
    }

    @Annotations.MethodPriority(10000)
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
