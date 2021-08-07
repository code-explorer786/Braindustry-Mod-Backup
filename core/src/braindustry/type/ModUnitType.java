package braindustry.type;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.math.Angles;
import arc.math.Mathf;
import arc.struct.Seq;
import braindustry.ModListener;
import braindustry.content.ModItems;
import braindustry.entities.abilities.ModAbility;
import mindustry.entities.abilities.Ability;
import mindustry.game.Team;
import mindustry.gen.EntityMapping;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;

import java.util.Objects;

import static braindustry.BDVars.fullName;

public class ModUnitType extends UnitType {
    public ItemStack[] dropItems=ItemStack.empty;
    public boolean hasAfterDeathLaser = false;
    public int afterDeathLaserCount=5;
    public ModUnitType(String name) {
        super(name);
        if (constructor==null){
            constructor= EntityMapping.nameMap.get(name);
        }
        ModListener.updaters.add(this::triggerUpdate);
    }

    protected void triggerUpdate() {
    }

    @Override
    public void drawBody(Unit unit) {
        Seq<ModAbility> modAbilities = getModAbilities();
        Seq<ModAbility> select = modAbilities.select(ModAbility::drawBody);
        if (select.size == 0) {
            super.drawBody(unit);
        } else {
            select.each(a -> a.drawBody(unit));
        }
    }

    public ModAbility toModAbility(Ability ability) {
        if (ability instanceof ModAbility) return (ModAbility) ability;
        return null;
    }

    @Override
    public void init() {
        super.init();
        getModAbilities().each(ModAbility::init);
    };

    public Seq<ModAbility> getModAbilities() {
        return abilities.map(this::toModAbility).select(Objects::nonNull);
    }

    @Override
    public void load() {
        super.load();
        getModAbilities().each(ModAbility::load);

        /*if(hasDroppingItems = true){
            ItemStack[] dropItems=ItemStack.with(ModItems.chloroAlloy, 320);
        }*/
    }

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
}
