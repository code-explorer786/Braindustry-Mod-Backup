package braindustry.content.Blocks;

import braindustry.world.blocks.TestBlock;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import braindustry.world.blocks.distribution.BufferedPayloadBridge;
import braindustry.world.blocks.legacy.SideJunction;
import braindustry.world.blocks.legacy.SmartRouter;
import braindustry.world.blocks.logic.LaserRuler;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class ModBlocks implements ContentList {
    public static Block

            //transportation
            armoredPlastaniumConveyor, chromiumConduit, phaseAlloyConveyor, plasticConveyor, surgeConveyor, smartRouter,
            sideJunction, smartSorter,

    //environment
    magmaFloor, obsidianBlock, obsidianFloor, oreChromium, oreOdinum,
            crimzesWall, crimzesFloor, jungleWall, jungleFloor, graysand, dirtRocksWall, liquidMethaneFloor, blackIce,
            blackIceWall, blackSnow, blackSnowWall, blackTree, darkPine, darkShrubs, greenTree, metallicPine, swampWater,
            swampSandWater, darkShrubsFloor, fluorescentTree, fluorescentGrass, fluorescentSandWater, fluorescentPine, invisibleWall,

    //power
    differentialMagmaGenerator, grapheniteSolarCollectorLarge, magmaGenerator, odinumReactor, blackHoleReactor, phaseAlloySolarPanel,
            phaseTower, materialReactor, refrigerantReactor,

    //production
    chromiumForge, exoticAlloySmelter, grapheniteFluidizer, grapheniteForge,
            hydraulicDrill, hyperAlloySmelter, hyperPhaseWeaver, magmaMixer, odinumExtractor,
            phaseAlloySmelter, plasticForge, quarryDrill, geothermicDrill, grapheniteKiln, refrigerantmixer, methaneGasifier, methaneLiquifier,
            hyperDenseCompositeSmelter, hyperExoAlloySmelter, multiCrafter, largeMultiCrafter,

    //units
    hyperAdditiveReconstructor, hyperAirFactory, hyperExponentialReconstructor, hyperGroundFactory,
            hyperMultiplicativeReconstructor, hyperNavalFactory, hyperTetrativeReconstructor, ultraReconstructor,

    //turrets
    blaze, brain, fragment, katana, mind, neuron,
            perlin, soul, stinger, impulse, synaps, axon, electron, gem, rapier, shinigami, voidwave, spark,

    //walls
    exoticAlloyWallLarge, exoticAlloyWall, grapheniteWallLarge, grapheniteWall, odinumWallLarge, odinumWall, plasticWallLarge,
            plasticWall, chloroWall, largeChloroWall,
    //gas
    gasTank, gasRouter, gasConduit,

    //logic

    //hided
    unitGenerator, unitNode,

    //experimental
     unitSpawner, examplePayloadBridge, testBlock, laserRuler;

    public static Block methaneBurner, hyperMethaneBurner;
    private ContentList[] blocksContent = {
            new ModEnvironmentBlocks(),
            new ModUnitsBlocks(),
            new ModProduction(),
            new ModPowerBlocks(),
            new ModDistributionBlocks(),
            new ModDefense(),
            new ModLogicBlocks(),
            new ModSandBox(),
    };

    public void load() {
        for (ContentList contentList : blocksContent) {
            contentList.load();
        }
        //region legacy
        smartRouter = new SmartRouter("smart-router") {{
            localizedName = "Smart Router";
            description = "Regular router with a choice of input / output paths";
            size = 1;
            requirements(Category.distribution, BuildVisibility.debugOnly, ItemStack.with(Items.copper, 3, Items.silicon, 10));
            buildCostMultiplier = 4.0F;
        }};
        sideJunction = new SideJunction("side-junction") {{
            localizedName = "Side Junction";
            description = "Transports objects sideways.";
            health = 40;
            buildCostMultiplier = 6;
            requirements(Category.distribution, ItemStack.with(Items.titanium, 2));
        }};
        //endregion

        examplePayloadBridge = new BufferedPayloadBridge("payload-bridge-conveyor") {{
            range = 10;
            requirements(Category.distribution, with(Items.graphite, 10, Items.copper, 20));
            size = 3;
            canOverdrive = false;
            buildVisibility = BuildVisibility.debugOnly;
        }};

        testBlock = new TestBlock("test-block") {{
            size = 2;
            requirements(Category.logic, ItemStack.with(), true);
            buildVisibility = BuildVisibility.debugOnly;
        }};

        unitGenerator = new UnitPowerGenerator("unit-generator") {{
            powerProduction = 10f;
            buildVisibility = BuildVisibility.debugOnly;
        }

            @Override
            public boolean isHidden(){
                return super.isHidden();
            }
        };
        unitNode = new UnitPowerNode("unit-node") {{
            maxNodes = Integer.MAX_VALUE;
            buildVisibility = BuildVisibility.debugOnly;
        }};
    }
}
