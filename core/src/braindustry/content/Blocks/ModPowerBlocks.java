package braindustry.content.Blocks;

import Gas.world.blocks.power.AllBurnerGenerator;
import Gas.world.consumers.ConsumeGasses;
import braindustry.content.Gasses;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.world.blocks.power.BlackHoleReactor;
import braindustry.world.blocks.power.MaterialReactor;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.BuildVisibility;

import static braindustry.content.Blocks.ModBlocks.*;

class ModPowerBlocks implements ContentList {
    @Override
    public void load() {
        methaneBurner = new AllBurnerGenerator("methane-burner") {{
            hasPower = true;
            hasGas = true;
            localizedName = "Methane Burner";
            description = "Burn Methane to produce heat energy.";
            powerProduction = 7f;
            gasCapacity = 11f;
            size = 2;
            consumes.addGas(new ConsumeGasses(Gasses.methane, 0.3f));
            requirements(Category.power, ItemStack.with(Items.silicon, 60, Items.titanium, 50, ModItems.chromium, 90));
        }};
        hyperMethaneBurner = new AllBurnerGenerator("hyper-methane-burner") {{
            hasPower = true;
            hasGas = true;
            localizedName = "Hyper Methane Burner";
            description = "Burn Methane more effective but consumes water.";
            powerProduction = 18f;
            gasCapacity = 25f;
            size = 3;
            consumes.addGas(new ConsumeGasses(Gasses.methane, 0.4f));
            consumes.liquid(Liquids.water, 0.5f);
            requirements(Category.power, ItemStack.with(Items.silicon, 60, Items.plastanium, 60, Items.titanium, 50, ModItems.chromium, 1200));
        }};
        phaseTower = new PowerNode("phase-tower") {{
            localizedName = "Phase Tower";
            health = 240;
            size = 4;
            maxNodes = 12;
            laserRange = 45;
            requirements(Category.power, ItemStack.with(ModItems.phaseAlloy, 15, Items.silicon, 20, Items.titanium, 10, Items.lead, 10));
        }};
        grapheniteSolarCollectorLarge = new SolarGenerator("graphenite-solar-collector-large") {{
            localizedName = "Graphenite Solar Panel";
            description = "Solar panel with Graphenite elements, better than Large Solar Panel.";
            powerProduction = 1.6f;
            size = 3;
            health = 210;
            requirements(Category.power, ItemStack.with(Items.metaglass, 100, Items.silicon, 40, ModItems.graphenite, 70));
        }};
        differentialMagmaGenerator = new SingleTypeGenerator("differential-magma-generator") {{
            localizedName = "Hyper Magma Generator";
            description = "Consumes Magma and Pyratite to produce much heat power.";
            size = 3;
            hasPower = true;
            health = 130;
            hasItems = true;
            itemCapacity = 30;
            hasLiquids = true;
            liquidCapacity = 40;
            itemDuration = 100;
            powerProduction = 20.6f;
            consumes.liquid(ModLiquids.magma, 0.2f);
            consumes.items(ItemStack.with(Items.pyratite, 3));
            requirements(Category.power, ItemStack.with(ModItems.graphenite, 140, ModItems.chromium, 190, Items.plastanium, 120, Items.titanium, 200, ModItems.odinum, 70));
        }};
        magmaGenerator = new BurnerGenerator("magma-generator") {{
            localizedName = "Magma Generator";
            description = "Consumes Magma and Coal to produce a lot of power.";
            size = 2;
            hasPower = true;
            hasItems = true;
            itemCapacity = 10;
            hasLiquids = true;
            liquidCapacity = 15;
            itemDuration = 90;
            powerProduction = 9;
            consumes.liquid(ModLiquids.magma, 0.04f);
            consumes.items(ItemStack.with(Items.coal, 1));
            requirements(Category.power, ItemStack.with(ModItems.graphenite, 50, Items.silicon, 80, Items.plastanium, 120, ModItems.chromium, 160));
        }};
        refrigerantReactor = new NuclearReactor("refrigerant-reactor") {{
            localizedName = "Refrigerant Reactor";
            description = "Upgraded Thorium Reactor. Now it uses the Refrigerant.";
            size = 4;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            itemCapacity = 10;
            liquidCapacity = 10;
            itemDuration = 120;
            powerProduction = 88;
            heating = 0.07f;
            smokeThreshold = 0.48f;
            explosionRadius = 90;
            explosionDamage = 1900;
            flashThreshold = 0.34f;
            coolantPower = 0.9f;
            consumes.power(3f);
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f);
            consumes.items(ItemStack.with(Items.thorium, 3));
            requirements(Category.power, ItemStack.with(Items.metaglass, 300, ModItems.odinum, 100, Items.silicon, 210, ModItems.graphenite, 270, Items.thorium, 200, Items.titanium, 140));
        }};
        odinumReactor = new ImpactReactor("odinum-reactor") {{
            localizedName = "Odinum Reactor";
            description = "Atomic fission and collision reactor.Consumes Odinum and Liquid Graphenite.";
            size = 4;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            itemCapacity = 20;
            liquidCapacity = 20;
            itemDuration = 90;
            powerProduction = 120;
            consumes.power(9f);
            consumes.liquid(ModLiquids.liquidGraphenite, 0.1f);
            consumes.items(ItemStack.with(ModItems.odinum, 1));
            requirements(Category.power, ItemStack.with(Items.metaglass, 500, ModItems.odinum, 300, Items.silicon, 400, ModItems.graphenite, 200, Items.plastanium, 200));
        }};
        materialReactor = new MaterialReactor("materia-reactor") {{
            localizedName = "Material Reactor";
            description = "Produce power from different type of matter, amount of power depends on the characteristics of matter.";
            itemCapacity = 150;
            liquidCapacity = 150;
            hasPower = true;
            hasItems = true;
            hasLiquids = true;
            size = 6;
            health = 8250;
            requirements(Category.power, BuildVisibility.shown, ItemStack.with(
                    Items.silicon, 350,
                    Items.plastanium, 400, Items.surgeAlloy, 300, ModItems.graphenite, 375, ModItems.chloroAlloy, 210
            ));
        }};
        blackHoleReactor = new BlackHoleReactor("black-hole-reactor") {{
            localizedName = "Blackhole Reactor";
            description = "Create power from small Blackhole in center.";
            size = 6;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            hasGas = true;
            gasCapacity = 100;
            itemCapacity = 60;
            liquidCapacity = 100;
            itemDuration = 240;
            powerProduction = 380;
            consumes.addGas(new ConsumeGasses(Gasses.methane, 0.2f));
            consumes.power(42f);
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.2f);
            consumes.item(ModItems.odinum, 7);
            requirements(Category.power, ItemStack.with(ModItems.chloroAlloy, 300, Items.surgeAlloy, 200, Items.graphite, 500, ModItems.odinum, 100));
        }};
        phaseAlloySolarPanel = new SolarGenerator("phase-alloy-solar-panel") {{
            localizedName = "Dense Composite Solar Panel";
            description = "An improved version of the Large Solar Panel. Takes up a lot of space.";
            health = 960;
            size = 5;
            hasPower = true;
            powerProduction = 8.4f;
            requirements(Category.power, ItemStack.with(Items.thorium, 280, Items.silicon, 400, Items.titanium, 90, ModItems.graphenite, 80, ModItems.phaseAlloy, 115));
            buildCostMultiplier = 1.1f;
        }};
    }
}
