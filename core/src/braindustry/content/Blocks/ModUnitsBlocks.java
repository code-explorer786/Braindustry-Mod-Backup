package braindustry.content.Blocks;

import arc.struct.Seq;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.content.ModUnitTypes;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

import static braindustry.content.Blocks.ModBlocks.*;

class ModUnitsBlocks implements ContentList {

    @Override
    public void load() {
        hyperAirFactory = new UnitFactory("hyper-air-factory") {{
            localizedName = "Hyper Air Factory";
            size = 3;
            consumes.power(2.2f);
            plans = Seq.with(
                    new UnitPlan(ModUnitTypes.armor, 600, ItemStack.with(Items.silicon, 15, ModItems.graphenite, 5, Items.lead, 5))
            );
            requirements(Category.units, ItemStack.with(Items.copper, 50, Items.lead, 150, Items.silicon, 70, Items.plastanium, 30, ModItems.odinum, 60));
        }};
        hyperGroundFactory = new UnitFactory("hyper-ground-factory") {{
            localizedName = "Hyper Ground Factory";
            size = 3;
            consumes.power(2f);
            plans = Seq.with(
                    new UnitPlan(ModUnitTypes.ibis, 550, ItemStack.with(Items.silicon, 10, ModItems.graphenite, 5, ModItems.odinum, 5)),
                    new UnitPlan(ModUnitTypes.tyzen, 510, ItemStack.with(Items.silicon, 10, ModItems.plastic, 2))
            );
            requirements(Category.units, ItemStack.with(Items.copper, 50, Items.lead, 140, Items.silicon, 70, Items.plastanium, 60));
        }};
        hyperNavalFactory = new UnitFactory("hyper-naval-factory") {{
            localizedName = "Hyper Naval Factory";
            size = 3;
            consumes.power(2.2f);
            plans = Seq.with(
                    new UnitPlan(ModUnitTypes.venti, 630, ItemStack.with(Items.silicon, 5, ModItems.graphenite, 10, Items.metaglass, 10))
            );
            requirements(Category.units, ItemStack.with(Items.lead, 150, Items.silicon, 80, Items.plastanium, 25, Items.metaglass, 85, ModItems.odinum, 55));
        }};
        hyperAdditiveReconstructor = new Reconstructor("hyper-additive-reconstructor") {{
            localizedName = "Hyper Additive Reconstructor";
            size = 3;
            consumes.power(3.2f);
            consumes.items(ItemStack.with(Items.silicon, 55, ModItems.graphenite, 25, Items.thorium, 20));
            constructTime = 900;
            requirements(Category.units, ItemStack.with(Items.copper, 300, ModItems.graphenite, 100, Items.titanium, 120, Items.silicon, 190, Items.plastanium, 20));
            upgrades = Seq.with(
                    new UnitType[]{ModUnitTypes.ibis, ModUnitTypes.aries},
                    new UnitType[]{ModUnitTypes.armor, ModUnitTypes.shield},
                    new UnitType[]{ModUnitTypes.venti, ModUnitTypes.lyra},
                    new UnitType[]{ModUnitTypes.tyzen, ModUnitTypes.kryox}
            );
        }};
        hyperExponentialReconstructor = new Reconstructor("hyper-exponential-reconstructor") {{
            localizedName = "Hyper Exponential Reconstructor";
            size = 7;
            consumes.power(7f);
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.3f).optional(false, false);
            consumes.items(ItemStack.with(Items.silicon, 120, ModItems.graphenite, 100, Items.titanium, 125, Items.plastanium, 75));
            constructTime = 2100;
            requirements(Category.units, ItemStack.with(ModItems.graphenite, 1200, Items.titanium, 900, Items.thorium, 500, Items.plastanium, 200, Items.phaseFabric, 300, Items.silicon, 130));
            upgrades = Seq.with(
                    new UnitType[]{ModUnitTypes.capra, ModUnitTypes.lacerta},
                    new UnitType[]{ModUnitTypes.chestplate, ModUnitTypes.chainmail},
                    new UnitType[]{ModUnitTypes.tropsy, ModUnitTypes.cenda},
                    new UnitType[]{ModUnitTypes.intelix, ModUnitTypes.nemesis}
            );
        }};
        hyperMultiplicativeReconstructor = new Reconstructor("hyper-multiplicative-reconstructor") {{
            localizedName = "Hyper Multiplicative Reconstructor";
            size = 5;
            consumes.power(12.2f);
            consumes.items(ItemStack.with(Items.silicon, 600, ModItems.graphenite, 450, Items.metaglass, 130, Items.titanium, 320, ModItems.odinum, 170, ModItems.plastic, 250));
            constructTime = 3020;
            requirements(Category.units, ItemStack.with(ModItems.graphenite, 600, Items.titanium, 300, Items.silicon, 300, ModItems.odinum, 500, ModItems.plastic, 100));
            upgrades = Seq.with(
                    new UnitType[]{ModUnitTypes.aries, ModUnitTypes.capra},
                    new UnitType[]{ModUnitTypes.shield, ModUnitTypes.chestplate},
                    new UnitType[]{ModUnitTypes.lyra, ModUnitTypes.tropsy},
                    new UnitType[]{ModUnitTypes.kryox, ModUnitTypes.intelix}
            );
        }};
        hyperTetrativeReconstructor = new Reconstructor("hyper-tetrative-reconstructor") {{
            localizedName = "Hyper Tetrative Reconstructor";
            size = 9;
            consumes.power(9f);
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.3f).optional(false, false);
            consumes.items(ItemStack.with(ModItems.graphenite, 800, Items.surgeAlloy, 400, ModItems.odinum, 500, ModItems.plastic, 250, ModItems.exoticAlloy, 210));
            constructTime = 3800;
            requirements(Category.units, ItemStack.with(ModItems.graphenite, 3100, Items.surgeAlloy, 500, ModItems.odinum, 500, Items.plastanium, 400, Items.phaseFabric, 600, Items.silicon, 1500, ModItems.exoticAlloy, 1000));
            upgrades = Seq.with(
                    new UnitType[]{ModUnitTypes.lacerta, ModUnitTypes.aquila},
                    new UnitType[]{ModUnitTypes.chainmail, ModUnitTypes.broadsword},
                    new UnitType[]{ModUnitTypes.cenda, ModUnitTypes.vyvna},
                    new UnitType[]{ModUnitTypes.nemesis, ModUnitTypes.maverix}
            );
        }};
        ultraReconstructor = new Reconstructor("ultra-reconstructor") {{
            localizedName = "Ultra Reconstructor";
            description = "Giant reconstructor for production of collosal TX Units, consumes Liquid Methane and endgame rare resources";
            size = 11;
            consumes.power(96f);
            consumes.liquid(ModLiquids.liquidMethane, 1.2f).optional(false, false);
            consumes.items(ItemStack.with(Items.silicon, 1700, ModItems.graphenite, 2200, Items.surgeAlloy, 1300, ModItems.odinum, 2000, ModItems.plastic, 1000, ModItems.chloroAlloy, 300));
            constructTime = 7200;
            requirements(Category.units, ItemStack.with(ModItems.graphenite, 3100, ModItems.chloroAlloy, 400, ModItems.odinum, 700, Items.plastanium, 400, Items.phaseFabric, 600, Items.silicon, 1500, ModItems.exoticAlloy, 1000));
            upgrades = Seq.with(
                    new UnitType[]{ModUnitTypes.aquila, ModUnitTypes.griffon},
                    new UnitType[]{ModUnitTypes.vyvna, ModUnitTypes.moray},
                    new UnitType[]{ModUnitTypes.maverix, ModUnitTypes.litix},
                    new UnitType[]{ModUnitTypes.broadsword, ModUnitTypes.penumbra}
            );
        }};
    }
}
