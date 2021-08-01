package braindustry.world.blocks.logic;

import arc.util.Strings;
import braindustry.gen.ModBuilding;
import braindustry.world.ModBlock;
import braindustry.world.blocks.DebugBlock;
import mindustry.ctype.Content;
import mindustry.logic.LAccess;
import mindustry.logic.Senseable;

public class NumberConverter extends ModBlock implements DebugBlock {
    public NumberConverter(String name) {
        super(name);
        destructible = true;
    }

    public class NumberConverterBuild extends ModBuilding {
        Object config = null;

        @Override

        public void control(LAccess type, double p1, double p2, double p3, double p4) {
            if (type == LAccess.config) {
                config = p1;
            } else
            super.control(type, p1, p2, p3, p4);
        }

        @Override
        public void control(LAccess type, Object p1, double p2, double p3, double p4) {
            if (type == LAccess.config && p1 instanceof String) {
                config = null;
                String val = p1.toString();
                if (Strings.canParseInt(val)) {
                    config = (float) Strings.parseInt(val);
                } else if (Strings.canParseFloat(val)) {
                    config = Strings.parseFloat(val);
                }
            } else
                super.control(type, p1, p2, p3, p4);
        }

        @Override
        public double sense(Content content) {
            return super.sense(content);
        }

        @Override
        public double sense(LAccess sensor) {
            if (sensor == LAccess.config) {
                return config instanceof Float ? (float) config : Double.MIN_VALUE;
            }
            return super.sense(sensor);
        }

        @Override
        public Object senseObject(LAccess sensor) {
            if (sensor == LAccess.config) {
                return config instanceof Float? Senseable.noSensed:null;
            }
            return super.senseObject(sensor);
        }

        public Object config() {
            return config;
        }
    }
}

