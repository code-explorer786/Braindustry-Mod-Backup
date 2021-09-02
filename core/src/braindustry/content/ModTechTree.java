package braindustry.content;

import arc.struct.Seq;
import braindustry.content.Blocks.ModBlocks;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.game.Objectives.Produce;
import mindustry.game.Objectives.Research;
import mindustry.game.Objectives.SectorComplete;
import mma.type.TechTreeManager;

public class ModTechTree extends TechTreeManager implements ContentList {
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
                    new Research(Blocks.interplanetaryAccelerator),
                    new SectorComplete(SectorPresets.planetaryTerminal),
                    new Research(Blocks.meltdown)), () -> {
                node(ModSectorPresets._451F, Seq.with(
                        new SectorComplete(ModSectorPresets.spacePort),
                        new Research(ModBlocks.spark)), () -> {
                    node(ModSectorPresets.meltingPoint, Seq.with(
                            new SectorComplete(ModSectorPresets._451F),
                            new Research(ModBlocks.hyperAdditiveReconstructor)
                    ), () -> {
                        node(ModSectorPresets.magmaticElectrostation, Seq.with(
                                new SectorComplete(ModSectorPresets.meltingPoint),
                                new Research(ModBlocks.magmaGenerator)), () -> {
                            node(ModSectorPresets.polarRift, Seq.with(
                                    new SectorComplete(ModSectorPresets.magmaticElectrostation),
                                    new Research(ModBlocks.refrigerantmixer)
                            ), () -> {
                                node(ModSectorPresets.methaneLaboratories, Seq.with(
                                        new SectorComplete(ModSectorPresets.magmaticElectrostation),
                                        new Research(ModBlocks.magmaMixer),
                                        new Research(ModBlocks.methaneLiquifier),
                                        new Research(ModBlocks.brain)), () -> {
                                    node(ModSectorPresets.icyBeach, Seq.with(
                                            new SectorComplete(ModSectorPresets.methaneLaboratories),
                                            new Research(ModBlocks.plasticConveyor)
                                    ), () -> {
                                        node(ModSectorPresets.ritual, Seq.with(
                                                new SectorComplete(ModSectorPresets.icyBeach),
                                                new Research(ModBlocks.mind),
                                                new Research(ModBlocks.odinumReactor)), () -> {
                                            //shinrin sectors
                                            node(ModSectorPresets.jungleExplorationComplex, Seq.with(
                                                    new SectorComplete(ModSectorPresets.ritual),
                                                    new Research(ModBlocks.shinigami),
                                                    new Research(ModBlocks.voidwave)), () -> {
                                                node(ModSectorPresets.emeraldSwamp, () -> {
                                                    node(ModSectorPresets.deentForest, () -> {
                                                        node(ModSectorPresets.icyDarkness, () -> {
                                                            node(ModSectorPresets.azureLandscape, () -> {
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
        });

        node(ModUnitTypes.griffon);
        node(ModBlocks.hyperTetrativeReconstructor, () -> {
            node( ModBlocks.ultraReconstructor, Seq.with(new Produce(ModItems.chloroAlloy)),()->{

                node(ModUnitTypes.griffon);
                node(ModUnitTypes.moray);
                node(ModUnitTypes.litix);
                node(ModUnitTypes.penumbra);
            });
        });

        node(Blocks.groundFactory, () -> {
            node(ModBlocks.hyperGroundFactory,()->{
                node(ModBlocks.hyperAdditiveReconstructor, Seq.with(new SectorComplete(ModSectorPresets._451F)), () -> {
                    node(ModBlocks.hyperMultiplicativeReconstructor, Seq.with(new SectorComplete(ModSectorPresets.polarRift)), () -> {
                        node(ModBlocks.hyperExponentialReconstructor, Seq.with(new SectorComplete(ModSectorPresets.icyBeach)), () -> {
                            node(ModBlocks.hyperTetrativeReconstructor, Seq.with(new SectorComplete(ModSectorPresets.ritual)));
                        });
                    });
                });
            });
            node(ModUnitTypes.ibis, () -> {
                node(ModUnitTypes.aries, Seq.with(new Research(ModBlocks.hyperAdditiveReconstructor)), () -> {
                    node(ModUnitTypes.capra, Seq.with(new Research(ModBlocks.hyperMultiplicativeReconstructor)), () -> {
                        node(ModUnitTypes.lacerta, Seq.with(new Research(ModBlocks.hyperExponentialReconstructor)), () -> {
                            node(ModUnitTypes.aquila, Seq.with(new Research(ModBlocks.hyperTetrativeReconstructor)));
                        });
                    });
                });
            });
            node(ModUnitTypes.tyzen, () -> {
                node(ModUnitTypes.kryox, Seq.with(new Research(ModBlocks.hyperAdditiveReconstructor)), () -> {
                    node(ModUnitTypes.intelix, Seq.with(new Research(ModBlocks.hyperMultiplicativeReconstructor)), () -> {
                        node(ModUnitTypes.nemesis, Seq.with(new Research(ModBlocks.hyperExponentialReconstructor)), () -> {
                            node(ModUnitTypes.maverix, Seq.with(new Research(ModBlocks.hyperTetrativeReconstructor)));
                        });
                    });
                });
            });
        });
        node(Blocks.airFactory, ()->{
            node(ModBlocks.hyperAirFactory, Seq.with(new SectorComplete(ModSectorPresets.spacePort)), () -> {
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
        });
        node(Blocks.navalFactory, ()->{
            node(ModBlocks.hyperNavalFactory, Seq.with(new SectorComplete(ModSectorPresets.spacePort)), () -> {
                node(ModUnitTypes.venti, () -> {
                    node(ModUnitTypes.lyra, () -> {
                        node(ModUnitTypes.tropsy, () -> {
                            node(ModUnitTypes.cenda, () -> {
                                node(ModUnitTypes.vyvna);
                            });
                        });
                    });
                });
            });
        });
        node(Blocks.blastDrill, () -> {
            node(ModBlocks.quarryDrill, () -> {
                node(ModBlocks.geothermicDrill);
            });
        });
        node(Blocks.siliconSmelter, () -> {
            node(ModBlocks.grapheniteForge, () -> {
                node(ModBlocks.chromiumForge, Seq.with(new SectorComplete(ModSectorPresets.spacePort)));
                node(ModBlocks.grapheniteFluidizer, () -> {
                    node(ModBlocks.magmaMixer, () -> {
                        node(ModBlocks.methaneGasifier);
                        node(ModBlocks.methaneLiquifier);
                    });
                });
                node(ModBlocks.grapheniteKiln);
                node(ModBlocks.odinumExtractor, Seq.with(new SectorComplete(ModSectorPresets.spacePort)), () -> {
                    node(ModBlocks.exoticAlloySmelter, Seq.with(new SectorComplete(ModSectorPresets.magmaticElectrostation)), () -> {
                        node(ModBlocks.multiCrafter, () -> {
                            node(ModBlocks.largeMultiCrafter);
                        });
                        node(ModBlocks.hyperExoAlloySmelter);
                        node(ModBlocks.phaseAlloySmelter, Seq.with(new SectorComplete(ModSectorPresets.polarRift)), () -> {
                            node(ModBlocks.hyperDenseCompositeSmelter);
                        });
                        node(ModBlocks.plasticForge);
                    });
                });
            });
        });
        node(Blocks.surgeWall, () -> {
            node(ModBlocks.exoticAlloyWall, () -> {
                node(ModBlocks.exoticAlloyWallLarge, () -> {
                    node(ModBlocks.chloroWall, () -> {
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
        node(Blocks.differentialGenerator, () -> {
            node(ModBlocks.magmaGenerator, () -> {
                node(ModBlocks.differentialMagmaGenerator);
            });
        });
        node(Blocks.thoriumReactor, () -> {
            node(ModBlocks.odinumReactor, Seq.with(new Research(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.materialReactor, () -> {
                    node(ModBlocks.blackHoleReactor);
                });
            });
            node(ModBlocks.refrigerantReactor);
        });
        node(Blocks.surgeTower, () -> {
            node(ModBlocks.phaseTower);
        });
        node(Blocks.arc, () -> {
            node(ModBlocks.impulse, () -> {
                node(ModBlocks.synaps, Seq.with(new Research(ModSectorPresets.spacePort)), () -> {
                    node(ModBlocks.axon, () -> {
                        node(ModBlocks.electron, Seq.with(new Research(ModBlocks.shinigami),
                                new Research(ModSectorPresets.jungleExplorationComplex)));
                    });
                });
            });
        });
        node(Blocks.fuse, () -> {
            node(ModBlocks.blaze, Seq.with(new SectorComplete(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.rapier, Seq.with(new SectorComplete(ModSectorPresets.magmaticElectrostation)));
            });
        });
        node(Blocks.lancer, () -> {
            node(ModBlocks.soul, Seq.with(new SectorComplete(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.mind, Seq.with(new SectorComplete(ModSectorPresets._451F)), () -> {
                    node(ModBlocks.voidwave, Seq.with(new SectorComplete(ModSectorPresets.polarRift)));
                });
            });
            node(ModBlocks.neuron, Seq.with(new SectorComplete(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.brain, Seq.with(new SectorComplete(ModSectorPresets._451F)), () -> {
                    node(ModBlocks.shinigami, Seq.with(new SectorComplete(ModSectorPresets.polarRift)), () -> {
                        node(ModBlocks.gem, Seq.with(new SectorComplete(ModSectorPresets.jungleExplorationComplex)));
                    });
                });
            });
        });
        node(Blocks.differentialGenerator, () -> {
            node(ModBlocks.magmaGenerator, Seq.with(new Research(ModSectorPresets.spacePort)), () -> {
                node(ModBlocks.differentialMagmaGenerator);
            });
            node(ModBlocks.methaneBurner, Seq.with(new Research(ModSectorPresets.icyBeach)), () -> {
                node(ModBlocks.hyperMethaneBurner);
            });
        });
        node(Items.silicon, () -> {
            nodeProduce(ModGasses.methane);
            nodeProduce(ModItems.graphenite, () -> {
                nodeProduce(ModItems.odinum, () -> {
                    nodeProduce(ModItems.exoticAlloy, () -> {
                        nodeProduce(ModItems.phaseAlloy, () -> {
                            nodeProduce(ModItems.chloroAlloy, () -> {
                            });
                        });
                        nodeProduce(ModItems.plastic, () -> {
                        });
                    });
                });
                nodeProduce(ModItems.chromium, () -> {
                });

            });
        });
        node(Liquids.water, () -> {
            nodeProduce(ModLiquids.liquidGraphenite, () -> {
                nodeProduce(ModLiquids.thoriumRefrigerant, () -> {
                    nodeProduce(ModLiquids.liquidMethane);
                });
            });
        });

        node(Blocks.parallax, () -> {
            node(ModBlocks.perlin);
        });
        node(Blocks.swarmer, () -> {
            node(ModBlocks.stinger);
        });
        node(Blocks.router, () -> {
            node(ModBlocks.laserRuler);
        });
        node(Blocks.segment, () -> {
            node(ModBlocks.fragment);
        });
        node(Blocks.plastaniumConveyor, () -> {
            node(ModBlocks.plasticConveyor);
        });
        node(Blocks.armoredConveyor, ()->{
            node(ModBlocks.armoredPlastaniumConveyor);
        });
        node(Blocks.pulseConduit, ()->{
            node(ModBlocks.chromiumConduit);
        });
        node(Blocks.phaseConveyor, ()->{
            node(ModBlocks.phaseAlloyConveyor);
        });
        node(Blocks.phaseWeaver, ()->{
            node(ModBlocks.hyperPhaseWeaver);
        });
        node(Blocks.surgeSmelter,()->{
            node( ModBlocks.hyperAlloySmelter);
        });
        node(ModBlocks.chromiumConduit, ()->{
            node(ModBlocks.gasConduit, Seq.with(new SectorComplete(ModSectorPresets.spacePort)));
        });
        node(Blocks.liquidTank, ()->{
            node(ModBlocks.gasTank);
        });
        node(Blocks.salvo, ()->{
            node(ModBlocks.spark);
        });
        node(Blocks.liquidRouter, ()-> {
            node(ModBlocks.gasRouter);
        });
        node(Blocks.laserDrill, ()->{
            node(ModBlocks.hydraulicDrill);
        });
        node(Blocks.cryofluidMixer,()->{
            node( ModBlocks.refrigerantmixer);
        });
        node(Blocks.sorter, ()->{
            node(ModBlocks.smartSorter);
        });
    }

}


