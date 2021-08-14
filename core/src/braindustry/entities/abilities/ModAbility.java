package braindustry.entities.abilities;

import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import mindustry.entities.abilities.Ability;
import mindustry.gen.Unit;
import mindustry.type.Weapon;

public abstract class ModAbility extends Ability {
    public boolean drawBody=false;
    public boolean drawBody(){
        return drawBody;
    }
    public void drawBody(Unit unit){

    }

    public void init() {

    }

    public void load() {

    }

    public Weapon[] weapons() {
        return new Weapon[]{};
    }

    public Seq<? extends TextureRegion> outlineRegions() {
        return new Seq<>();
    }
}
