package braindustry.world.blocks.Unit.power;

import arc.math.geom.Point2;
import arc.struct.Seq;
import braindustry.entities.abilities.ModAbility;
import braindustry.entities.abilities.PowerGeneratorAbility;
import braindustry.gen.PowerGeneratorc;
import mindustry.gen.Groups;
import mindustry.world.blocks.power.PowerGenerator;
import mindustry.world.meta.BuildVisibility;

public class UnitPowerGenerator extends PowerGenerator {
    public UnitPowerGenerator(String name) {
        super(name);
        this.buildVisibility = BuildVisibility.debugOnly;
        this.powerProduction = 10;
    }

    public class UnitPowerGeneratorBuild extends PowerGenerator.GeneratorBuild {
        public PowerGeneratorc parent;


        public UnitPowerGeneratorBuild() {

        }


        public int pos() {
            return Point2.pack((int) this.x / 8, (int) this.y / 8);
        }

        public void setParent(PowerGeneratorc parent) {
            this.parent = parent;
        }

        @Override
        public void draw() {
        }

        @Override
        public void drawStatus() {
        }

        @Override
        public void drawTeam() {
        }

        @Override
        public void drawTeamTop() {
        }

        @Override
        public void update() {
            super.update();
            this.x = parent.x();
            this.y = parent.y();
        }

        public void kill() {
            remove();
//            Call.
//            Call.tileDestroyed(this);
        }

        @Override
        public void add() {
            if (!this.added) {
//                Groups.all.add(this);
                this.added = true;
            }
        }

        public float getPowerProduction() {
            this.x = parent.x();
            this.y = parent.y();

            if (!this.isValid()) {
                this.power.graph.remove(this);
            }
            Seq<ModAbility> modAbilities = parent.modUnitType().getModAbilities();
            ModAbility ability = modAbilities.find(a -> a instanceof PowerGeneratorAbility);
            return ability instanceof PowerGeneratorAbility ? ((PowerGeneratorAbility) ability).powerProduction : 0;
        }

        @Override
        public boolean isValid() {
            boolean near = true;
            /*for (int i = 0; i < this.power.links.size; i++) {
                Building building= Vars.world.build(this.power.links.get(i));
                near=near || (building!=null && parent.validLink(building));
            }*/
            return parent != null && parent.isValid() && near;
        }

        public void set(float x, float y) {
//        this.x = x;
//        this.y = y;
        }
    }
}
