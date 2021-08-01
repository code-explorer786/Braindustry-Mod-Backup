package braindustry.world.blocks.logic;

import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import braindustry.gen.ModBuilding;
import braindustry.world.ModBlock;
import braindustry.world.blocks.DebugBlock;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.logic.LAccess;
import mindustry.world.blocks.logic.LogicBlock;
import mindustry.world.blocks.logic.MessageBlock;

import static mindustry.Vars.tilesize;

public class MessageReader extends ModBlock implements DebugBlock {
    public boolean runtimeConfigBuilding=false;
    public MessageReader(String name) {
        super(name);
        update=true;
        destructible = true;
        logicConfigurable = false;
        enableDrawStatus = true;
    }

    @Override
    public boolean configSenseable() {
        return true;
    }

    public class MessageReaderBuild extends ModBuilding {
        Object config = "";
        @Override
        public void update() {
            super.update();
            config = null;
            StringBuilder configBuilder = new StringBuilder();
            Seq<MessageBlock.MessageBuild> messageBuilds = proximity.select(b -> b instanceof MessageBlock.MessageBuild).as();
            for (int i = 0; i < messageBuilds.size; i++) {
                String str = messageBuilds.get(i).message.toString();
                configBuilder.append(str);
                if (i != messageBuilds.size - 1) {
                    configBuilder.append("\n");
                }
            }
            config = configBuilder.toString();
            if (messageBuilds.isEmpty()) {
                enabled = false;
            }
        }

        @Override
        public Object senseObject(LAccess sensor) {
            Object config=this.config;
            if (runtimeConfigBuilding) {
                StringBuilder configBuilder = new StringBuilder();
                Seq<MessageBlock.MessageBuild> messageBuilds = proximity.select(b -> b instanceof MessageBlock.MessageBuild).as();
                for (int i = 0; i < messageBuilds.size; i++) {
                    String str = messageBuilds.get(i).message.toString();
                    configBuilder.append(str);
                    if (i != messageBuilds.size - 1) {
                        configBuilder.append("\n");
                    }
                }
                config = configBuilder.toString();
            }
            if (sensor == LAccess.config) {
                return config;
            }
            return super.senseObject(sensor);
        }

        @Override
        public Object config() {
            return config;
        }

        @Override
        public void drawStatus() {
            if (block.enableDrawStatus) {
                float multiplier = block.size > 1 ? 1 : 0.64f;
                float brcx = x + (block.size * tilesize / 2f) - (tilesize * multiplier / 2f);
                float brcy = y - (block.size * tilesize / 2f) + (tilesize * multiplier / 2f);

                Draw.z(Layer.power + 1);
                Draw.color(Pal.gray);
                Fill.square(brcx, brcy, 2.5f * multiplier, 45);
                Draw.color(status().color);
                Fill.square(brcx, brcy, 1.5f * multiplier, 45);
                Draw.color();
            }
        }
    }
}
