package braindustry.world.blocks.legacy;

import arc.util.io.Reads;
import mindustry.gen.Building;
import mindustry.world.blocks.legacy.LegacyBlock;

public class SmartRouter extends LegacyBlock {
    public SmartRouter(String name) {
        super(name);
        this.hasItems = true;
    }
    public class SmartRouterBuild extends Building{
        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            read.str();
        }
    }
}
