package braindustry.content;

import Gas.content.GasBasicBlocks;
import Gas.content.Gasses;
import arc.struct.Seq;
import ModVars.Classes.TechTreeManager;
import braindustry.content.Blocks.ModBlocks;
import braindustry.world.ModBlock;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.game.Objectives;
//import static mindustry.game.Objectives.*;

public class ModTechTree extends TechTreeManager implements ContentList {
    public TechTreeManager techTree = new TechTreeManager();
    boolean loaded = false;

    public ModTechTree() {
        TechTree.class.getClass();
    }

    public void load() {
        if (loaded) return;
        loaded = true;

         //osore sectors
        node(SectorPresets.planetaryTerminal, () -> {
            node(ModSectorPresets.spacePort, Seq.with(
                    new Objectives.Research(Blocks.interplanetaryAccelerator),
                    new Objectives.Research(ModBlocks.spark)), () -> {
                node(ModSectorPresets.FFOf, () -> {
                    node(ModSectorPresets.meltingPoint,Seq.with(new Objectives.Research(ModBlocks.hyperMultiplicativeReconstructor)), () -> {
                        node(ModSectorPresets.magmaticElectrostation,Seq.with(new Objectives.Research(ModBlocks.magmaGenerator)), () -> {
                            node(ModSectorPresets.polarRift, () -> {
                                node(ModSectorPresets.methaneLaboratories,Seq.with(
                                        new Objectives.Research(ModBlocks.magmaMixer),
                                        new Objectives.Research(ModBlocks.methaneLiquifier),
                                        new Objectives.Research(ModBlocks.brain)), () -> {
                                    node(ModSectorPresets.icyBeach, () -> {
                                        node(ModSectorPresets.ritual,Seq.with(
                                                new Objectives.Research(ModBlocks.mind),
                                                new Objectives.Research(ModBlocks.odinumReactor)), () -> {
                                            //shinrin sectors
                                            node(ModSectorPresets.jungleExplorationComplex,Seq.with(
                                                    new Objectives.Research(ModBlocks.shinigami),
                                                    new Objectives.Research(ModBlocks.voidwave)),  () -> {
                                                node(ModSectorPresets.emeraldSwamp,  () -> {
                                                    node(ModSectorPresets.deentForest,  () -> {
                                                        node(ModSectorPresets.icyDarkness,  () -> {
                                                            node(ModSectorPresets.azureLandscape);
                                                        });
                                                    });
                                                });
                                            });
                                        });
                                    });
                                });
                            });
                        });
                    });
                });
            });
        });

        parentNode(ModBlocks.hyperTetrativeReconstructor, ModBlocks.ultraReconstructor,Seq.with(new Objectives.Produce(ModItems.chloroAlloy)), ()->{
            node(ModUnitTypes.griffon);
            node(ModUnitTypes.moray);
            node(ModUnitTypes.litix);
            node(ModUnitTypes.penumbra);
        });

        parentNode(Blocks.groundFactory, ModBlocks.hyperGroundFactory, () -> {
            node(ModBlocks.hyperAdditiveReconstructor,Seq.with(new Objectives.SectorComplete(ModSectorPresets.FFOf)), ()->{
                node(ModBlocks.hyperMultiplicativeReconstructor,Seq.with(new Objectives.SectorComplete(ModSectorPresets.polarRift)), ()->{
                    node(ModBlocks.hyperExponentialReconstructor,Seq.with(new Objectives.SectorComplete(ModSectorPresets.icyBeach)), ()->{
                        node(ModBlocks.hyperTetrativeReconstructor,Seq.with(new Objectives.SectorComplete(ModSectorPresets.ritual)));
                    });
                });
            });
            node(ModUnitTypes.ibis, () -> {
                node(ModUnitTypes.aries,Seq.with(new Objectives.Research(ModBlocks.hyperAdditiveReconstructor)), () -> {
                    node(ModUnitTypes.capra,Seq.with(new Objectives.Research(ModBlocks.hyperMultiplicativeReconstructor)), () -> {
                        node(ModUnitTypes.lacerta,Seq.with(new Objectives.Research(ModBlocks.hyperExponentialReconstructor)), () -> {
                            node(ModUnitTypes.aquila, Seq.with(new Objectives.Research(ModBlocks.hyperTetrativeReconstructor)));
                        });
                    });
                });
            });
            node(ModUnitTypes.tyzen, () -> {
                node(ModUnitTypes.kryox,Seq.with(new Objectives.Research(ModBlocks.hyperAdditiveReconstructor)), () -> {
                    node(ModUnitTypes.intelix,Seq.with(new Objectives.Research(ModBlocks.hyperMultiplicativeReconstructor)), () -> {
                        node(ModUnitTypes.nemesis,Seq.with(new Objectives.Research(ModBlocks.hyperExponentialReconstructor)), () -> {
                            node(ModUnitTypes.maverix, Seq.with(new Objectives.Research(ModBlocks.hyperTetrativeReconstructor)));
                        });
                    });
                });
            });
        });
        parentNode(Blocks.airFactory, ModBlocks.hyperAirFactory,Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)), () -> {
            node(ModUnitTypes.armor, () -> {
                node(ModUnitTypes.shield, () -> {
                    node(ModUnitTypes.chestplate, () -> {
                        node(ModUnitTypes.chainmail, () -> {
                            node(ModUnitTypes.broadsword);
                        });
                    });
                });
            });
        });
        parentNode(Blocks.navalFactory, ModBlocks.hyperNavalFactory,Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)), () -> {
            node(ModUnitTypes.venti, () -> {
                node(ModUnitTypes.lyra, () -> {
                    node(ModUnitTypes.tropsy, () -> {
                        node(ModUnitTypes.cenda,()-> {
                            node(ModUnitTypes.vyvna);
                        });
                    });
                });
            });
        });
        node(Blocks.blastDrill, () -> {
            node(ModBlocks.quarryDrill, ()->{
                node(ModBlocks.geothermicDrill);
            });
        });
        node(Blocks.siliconSmelter, () -> {
                node(ModBlocks.grapheniteForge, () -> {
                    node(ModBlocks.chromiumForge,Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)));
                    node(ModBlocks.grapheniteFluidizer,()->{
                        node(ModBlocks.magmaMixer, ()->{
                            node(ModBlocks.methaneGasifier);
                            node(ModBlocks.methaneLiquifier);
                        });
                    });
                    node(ModBlocks.grapheniteKiln);
                    node(ModBlocks.odinumExtractor,Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)),()-> {
                    node(ModBlocks.exoticAlloySmelter,Seq.with(new Objectives.SectorComplete(ModSectorPresets.magmaticElectrostation)),()->{
                        node(ModBlocks.multiCrafter, ()->{
                            node(ModBlocks.largeMultiCrafter);
                            });
                            node(ModBlocks.hyperExoAlloySmelter);
                            node(ModBlocks.phaseAlloySmelter,Seq.with(new Objectives.SectorComplete(ModSectorPresets.polarRift)), ()->{
                                node(ModBlocks.hyperDenseCompositeSmelter);
                            });
                            node(ModBlocks.plasticForge);
                        });
                    });
                });
            });
        node(Blocks.surgeWall, ()->{
            node(ModBlocks.exoticAlloyWall,()->{
                node(ModBlocks.exoticAlloyWallLarge,()->{
                    node(ModBlocks.chloroWall,()->{
                        node(ModBlocks.largeChloroWall);
                    });
                });
            });
        });
        node(Blocks.titaniumWallLarge, () -> {
            node(ModBlocks.grapheniteWall, () -> {
                node(ModBlocks.grapheniteWallLarge, () -> {
                    node(ModBlocks.odinumWall, () -> {
                        node(ModBlocks.odinumWallLarge);
                    });
                });
            });
        });
        node(Blocks.plastaniumWall, () -> {
            node(ModBlocks.plasticWall);
            node(ModBlocks.plasticWallLarge);
        });
        node(Blocks.armoredConveyor, () -> {
            node(ModBlocks.armoredPlastaniumConveyor, () -> {
                node(ModBlocks.plasticConveyor);
            });
        });
        node(Blocks.largeSolarPanel, () -> {
            node(ModBlocks.grapheniteSolarCollectorLarge, () -> {
                node(ModBlocks.phaseAlloySolarPanel);
            });
        });
        node(Blocks.differentialGenerator, ()->{
            node(ModBlocks.magmaGenerator,()->{
                node( ModBlocks.differentialMagmaGenerator);
            });
        });
        node(Blocks.thoriumReactor, () -> {
            node(ModBlocks.odinumReactor,Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), ()->{
                node(ModBlocks.materialReactor,()->{
                    node(ModBlocks.blackHoleReactor);
                });
            });
            node(ModBlocks.refrigerantReactor);
        });
        parentNode(Blocks.surgeTower, ModBlocks.phaseTower);
        node(Blocks.arc, () -> {
            node(ModBlocks.impulse,  () -> {
                node(ModBlocks.synaps, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)), () -> {
                    node(ModBlocks.axon, () -> {
                        node(ModBlocks.electron, Seq.with(new Objectives.Research(ModBlocks.shinigami),
                        new Objectives.Research(ModSectorPresets.jungleExplorationComplex)));
                    });
                });
            });
        });
        parentNode(Blocks.fuse, ModBlocks.blaze, Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)), ()->{
            node(ModBlocks.rapier, Seq.with(new Objectives.SectorComplete(ModSectorPresets.magmaticElectrostation)));
        });
        node(Blocks.lancer, () -> {
            node(ModBlocks.soul,Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.mind, Seq.with(new Objectives.SectorComplete(ModSectorPresets.FFOf)), () -> {
                    node(ModBlocks.voidwave, Seq.with(new Objectives.SectorComplete(ModSectorPresets.polarRift)));
                });
            });
            node(ModBlocks.neuron,Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)), ()->{
                node(ModBlocks.brain, Seq.with(new Objectives.SectorComplete(ModSectorPresets.FFOf)), ()->{
                    node(ModBlocks.shinigami, Seq.with(new Objectives.SectorComplete(ModSectorPresets.polarRift)), ()->{
                        node(ModBlocks.gem, Seq.with(new Objectives.SectorComplete(ModSectorPresets.jungleExplorationComplex)));
                    });
                });
            });
        });
        node(Blocks.differentialGenerator, ()->{
            node(ModBlocks.magmaGenerator, Seq.with(new Objectives.Research(ModSectorPresets.spacePort)),  ()->{
                node(ModBlocks.differentialMagmaGenerator);
            });
            node(ModBlocks.methaneBurner, Seq.with(new Objectives.Research(ModSectorPresets.icyBeach)),  ()->{
                node(ModBlocks.hyperMethaneBurner);
            });
        });

        nodeProduce(ModItems.graphenite, ()->{
            nodeProduce(ModItems.odinum, ()->{
                nodeProduce(ModItems.exoticAlloy, ()->{
                    nodeProduce(ModItems.phaseAlloy, ()->{
                        nodeProduce(ModItems.chloroAlloy, ()->{
                        });
                    });
                    nodeProduce(ModItems.plastic, ()->{
                    });
                });
            });
            nodeProduce(ModItems.chromium, ()->{
            });

        });

        nodeProduce(Gasses.methane);

        nodeProduce(ModLiquids.liquidGraphenite, ()->{
            nodeProduce(ModLiquids.thoriumRefrigerant,()->{
                nodeProduce(ModLiquids.liquidMethane);
            });

        });

        parentNode(Blocks.parallax, ModBlocks.perlin);
        parentNode(Blocks.swarmer, ModBlocks.stinger);
        parentNode(Blocks.router, ModBlocks.smartRouter);
        parentNode(Blocks.segment, ModBlocks.fragment);
        parentNode(Blocks.plastaniumConveyor, ModBlocks.plasticConveyor);
        parentNode(Blocks.armoredConveyor, ModBlocks.armoredPlastaniumConveyor);
        parentNode(Blocks.pulseConduit, ModBlocks.chromiumConduit);
        parentNode(Blocks.phaseConveyor, ModBlocks.phaseAlloyConveyor);
        parentNode(Blocks.phaseWeaver, ModBlocks.hyperPhaseWeaver);
        parentNode(Blocks.surgeSmelter, ModBlocks.hyperAlloySmelter);
        parentNode(ModBlocks.chromiumConduit, GasBasicBlocks.gasConduit, Seq.with(new Objectives.SectorComplete(ModSectorPresets.spacePort)));
        parentNode(Blocks.liquidTank, ModBlocks.gasTank);
        parentNode(Blocks.salvo, ModBlocks.spark);
        parentNode(Blocks.liquidRouter, ModBlocks.gasRouter);
        parentNode(Blocks.laserDrill, ModBlocks.hydraulicDrill);
        parentNode(Blocks.cryofluidMixer, ModBlocks.refrigerantmixer);
    }
}


