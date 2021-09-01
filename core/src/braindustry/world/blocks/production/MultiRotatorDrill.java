package braindustry.world.blocks.production;

import acontent.world.meta.AStats;
import braindustry.world.meta.BDStat;

public class MultiRotatorDrill extends mma.world.blocks.production.MultiRotatorDrill {
    public AStats aStats = new AStats();

    public MultiRotatorDrill(String name) {
        super(name);
        stats = aStats.copy(stats);
    }

    @Override
    public void setStats() {
        super.setStats();
        aStats.add(BDStat.rotatorsCount, rotators.length);
    }
    public class MultiRotorDrillBuild extends mma.world.blocks.production.MultiRotatorDrill.MultiRotorDrillBuild {
    }
}
