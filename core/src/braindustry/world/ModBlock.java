package braindustry.world;

import acontent.world.meta.AStats;
import mindustry.world.Block;

public class ModBlock extends Block {
    AStats aStats = new AStats();
    public ModBlock(String name) {
        super(name);
        super.stats=aStats.copy(stats);
    }

    @Override
    public void load() {
        super.load();
    }
}
