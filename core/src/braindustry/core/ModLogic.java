package braindustry.core;

import arc.ApplicationListener;
import arc.Events;
import arc.func.Prov;
import arc.struct.IntSeq;
import arc.struct.Seq;
import braindustry.content.ModBullets;
import braindustry.type.ModUnitType;
import braindustry.world.blocks.DebugBlock;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Call;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

import static braindustry.BDVars.settings;

public class ModLogic implements ApplicationListener {
    boolean debug;
    private Prov<Seq<Block>> debugListGetter = () -> Seq.<Block>with(
            Blocks.duct,
            Blocks.ductRouter,
            Blocks.ductBridge,
            Blocks.blockForge,
            Blocks.blockLoader,
            Blocks.blockUnloader,
            null
    ).select(c -> c != null && c.buildVisibility == BuildVisibility.debugOnly);
    private Seq<Block> debugList;

    public ModLogic() {
        debug = settings.debug();
//        Log.info("debugList: @",debugList=debugListGetter.get());
        Events.on(EventType.WorldLoadEvent.class, e -> {
            Groups.build.each(this::removeUnitPowerGenerators);
        });
        Events.on(EventType.UnitDestroyEvent.class, (e) -> {
            Unit unit = e.unit;
            if (!(unit.type instanceof ModUnitType)) return;
            ModUnitType type = (ModUnitType) unit.type;
            if (type.hasAfterDeathLaser) {

                float anglePart = 360f / (float) type.afterDeathLaserCount;
                for (float i = 0; i < 360f / anglePart; i++) {
//                    print("i: @,anglePart: @",i,anglePart);
                    Call.createBullet(ModBullets.deathLaser, unit.team, unit.x, unit.y, (360f + unit.rotation + 90f + anglePart / 2f + anglePart * i) % 360f, 1200f, 1f, 30f);
                }
            }
            if (type.dropItems.length > 0) {
                for (Team team : Team.all) {
                    if (!team.active() || team.cores().isEmpty() || team == unit.team) continue;
                    for (ItemStack stack : type.dropItems) {
                        team.core().items.add(stack.item, stack.amount);
                    }
                }
            }
        });
    }

    @Override
    public void update() {
        if (debug != settings.debug()) {
            debug = settings.debug();
            Vars.content.blocks().each(content -> {
                if (isDebug(content)) {
                    content.buildVisibility = debug ? BuildVisibility.shown : BuildVisibility.debugOnly;
                }
            });
        }
    }

    private boolean isDebug(Block content) {
        if (debugList==null)debugList=debugListGetter.get();
        return content instanceof DebugBlock || debugList.contains(content);
    }

    protected void removeUnitPowerGenerators(Building build) {
        if (build != null && build.power != null) {
            for (int i : build.power.links.toArray()) {
                if (i<0) {
                    build.power.links.removeValue(i);
                }
            }
        }
    }
}
