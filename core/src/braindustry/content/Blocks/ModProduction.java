package braindustry.content.Blocks;

import braindustry.world.blocks.production.*;
import gas.GasStack;
import gas.world.blocks.production.GasGenericCrafter;
import gas.world.consumers.ConsumeGas;
import arc.math.geom.Vec3;
import braindustry.content.ModGasses;
import braindustry.content.ModFx;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import gas.world.draw.GasDrawSmelter;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.type.LiquidStack;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.draw.DrawSmelter;
import mma.type.ModLiquidStack;
import mma.type.Recipe;
import mma.type.Rotator;
import mma.world.blocks.production.MultiCrafter;
import mma.world.blocks.production.MultiRotatorDrill;

import static braindustry.content.Blocks.ModBlocks.*;

class ModProduction implements ContentList {
    @Override
    public void load() {
        multiCrafter = new MultiCrafter("multi-crafter") {{
            size = 3;
            localizedName = "Multi Smelter";
            description = "A furnace that can melt several resources depending on your choice";
            update = true;
            destructible = true;
            hasLiquids = true;
            hasShadow = true;

            health = 360;
            category = Category.crafting;
            dynamicItem = true;
            dynamicLiquid = true;
            extraStorageLiquid = 3;
            extraStorageItem = 3;
            recipes(Recipe.with(new ItemStack(ModItems.graphenite, 1), ItemStack.with(Items.graphite, 2, Items.silicon, 3, Items.titanium, 2, Items.lead, 2), ModLiquidStack.with(Liquids.slag, 1), 150),
                    Recipe.with(new ItemStack(ModItems.exoticAlloy, 1), ItemStack.with(Items.sporePod, 3, Items.titanium, 4, Items.thorium, 3), ModLiquidStack.with(Liquids.water, 2), 140)
            );
            requirements(category, ItemStack.with(Items.thorium, 200, ModItems.graphenite, 230, Items.lead, 500, Items.plastanium, 120));
        }};
        largeMultiCrafter = new MultiCrafter("multi-kiln") {{
            size = 6;
            localizedName = "Multi Kiln";
            description = "A bigger furnace that can melt several resources depending on your choice";
            update = true;
            destructible = true;
            hasLiquids = true;
            hasShadow = true;

            health = 980;
            category = Category.crafting;
            dynamicItem = true;
            dynamicLiquid = true;
            recipes(Recipe.with(new ItemStack(ModItems.graphenite, 2), ItemStack.with(Items.graphite, 2, Items.silicon, 3, Items.titanium, 2, Items.lead, 2), ModLiquidStack.with(Liquids.slag, 1), 150),
                    Recipe.with(new ItemStack(ModItems.exoticAlloy, 2), ItemStack.with(Items.sporePod, 3, Items.titanium, 4, Items.thorium, 3), ModLiquidStack.with(Liquids.water, 2), 140),
                    Recipe.with(new ItemStack(ModItems.chromium, 1), ItemStack.with(Items.titanium, 3, Items.metaglass, 4), ModLiquidStack.with(Liquids.oil, 2), 125),
                    Recipe.with(new ItemStack(ModItems.odinum, 2), ItemStack.with(Items.thorium, 3, Items.titanium, 1, Items.plastanium, 3), ModLiquidStack.with(Liquids.water, 2), 155)
            );
            requirements(category, ItemStack.with(ModItems.odinum, 200, ModItems.graphenite, 230, ModItems.phaseAlloy, 500, ModItems.plastic, 220));
        }};

        chromiumForge = new GenericCrafter("chromium-forge") {{
            localizedName = "Chromium Forge";
            description = "This forge can smelt a Chromium from Metaglass and Titanium.";
            health = 220;
            liquidCapacity = 0;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            hasItems = true;
            craftTime = 90;
            updateEffect = Fx.plasticburn;
            consumes.power(2.5f);
            consumes.items(ItemStack.with(Items.metaglass, 2, Items.titanium, 1));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 80, Items.titanium, 100, Items.metaglass, 120, Items.silicon, 200, ModItems.graphenite, 200));
            outputItem = new ItemStack(ModItems.chromium, 2);
        }};
        exoticAlloySmelter = new GenericCrafter("exotic-alloy-smelter") {{
            localizedName = "Exotic Alloy Smelter";
            description = "Cultivates Exometal from Thorium, Titanium and Spore Pods.";
            health = 140;
            liquidCapacity = 10;
            size = 3;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            craftTime = 80;
            updateEffect = Fx.plasticburn;
            consumes.power(1.2f);
            drawer = new DrawSmelter();
            consumes.items(ItemStack.with(ModItems.graphenite, 1, Items.thorium, 3, Items.titanium, 1));
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 40, Items.lead, 100, Items.graphite, 80, ModItems.graphenite, 100, Items.metaglass, 80));
            outputItem = new ItemStack(ModItems.exoticAlloy, 2);
        }};
        grapheniteFluidizer = new GenericCrafter("graphenite-fluidizer") {{
            localizedName = "Graphenite Fluidizer";
            description = "A fluidizer that turns graphenite into slurry using huge pressure. Consumes water and graphenite.";
            size = 2;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            craftTime = 60;
            updateEffect = Fx.steam;
            consumes.power(3f);
            consumes.liquid(Liquids.water, 0.1f);
            consumes.items(ItemStack.with(ModItems.graphenite, 1));
            requirements(Category.crafting, ItemStack.with(Items.lead, 50, Items.thorium, 80, Items.silicon, 70, Items.titanium, 50, ModItems.graphenite, 85));
            outputLiquid = new LiquidStack(ModLiquids.liquidGraphenite, 12f);
        }};
        grapheniteForge = new GenericCrafter("graphenite-forge") {{
            localizedName = "Graphenite Forge";
            description = "Combines Graphite, Titanium and Silicon to produce Graphenite.";
            health = 80;
            liquidCapacity = 0;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            hasItems = true;
            craftTime = 70;
            updateEffect = Fx.plasticburn;
            consumes.power(1.2f);
            consumes.items(ItemStack.with(Items.graphite, 2, Items.silicon, 1, Items.titanium, 1));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.lead, 100, Items.titanium, 30, Items.thorium, 40, Items.silicon, 70, Items.graphite, 80));
            outputItem = new ItemStack(ModItems.graphenite, 2);
        }};
        hyperAlloySmelter = new GenericCrafter("hyper-alloy-smelter") {{
            localizedName = "Hyper Alloy Smelter";
            description = "Bigger Surge Kiln, uses more materials and energy as well as oil for speed and higher amounts of alloy produced.";
            health = 300;
            liquidCapacity = 80;
            size = 5;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            craftTime = 90;
            updateEffect = Fx.plasticburn;
            consumes.power(9f);
            consumes.liquid(Liquids.oil, 0.2f).optional(false, false);
            consumes.items(ItemStack.with(Items.copper, 4, Items.titanium, 4, Items.lead, 5, Items.silicon, 5));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 120, Items.titanium, 150, Items.metaglass, 100, Items.silicon, 300, ModItems.graphenite, 170, Items.surgeAlloy, 100));
            outputItem = new ItemStack(Items.surgeAlloy, 5);
        }};
        hyperPhaseWeaver = new GenericCrafter("hyper-phase-weaver") {{
            localizedName = "Hyper Phase Weaver";
            description = "Bigger Phase Weaver, uses more materials and Silicon to produce more Phase Fabric.";
            health = 100;
            liquidCapacity = 0;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            hasItems = true;
            itemCapacity = 30;
            craftTime = 120;
            updateEffect = Fx.plasticburn;
            consumes.power(6f);
            consumes.items(ItemStack.with(Items.thorium, 6, Items.silicon, 3, Items.sand, 6));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.metaglass, 200, Items.titanium, 90, Items.phaseFabric, 80, Items.silicon, 200, ModItems.graphenite, 180));
            outputItem = new ItemStack(Items.phaseFabric, 4);
        }};
        magmaMixer = new GenericCrafter("magma-mixer") {{
            localizedName = "Magma Mixer";
            description = "Makes hot Magma liquid from Copper and Slag.";
            size = 2;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            craftTime = 70;
            updateEffect = Fx.steam;
            consumes.power(6f);
            consumes.liquid(Liquids.slag, 0.2f);
            consumes.items(ItemStack.with(Items.copper, 8));
            requirements(Category.crafting, ItemStack.with(Items.lead, 150, ModItems.chromium, 200, Items.silicon, 90, Items.metaglass, 100, ModItems.graphenite, 85));
            outputLiquid = new LiquidStack(ModLiquids.magma, 26f);
        }};
        grapheniteKiln = new MultiGenericSmelter("graphenite-kiln") {{
            topPoints(
                    new Vec3(1.25f, 1.25f, 0.9f),
                    new Vec3(1.25f, 4.75f, 0.9f),
                    new Vec3(3.f, 3.f, 1.1f),
                    new Vec3(4.75f, 1.25f, 0.9f),
                    new Vec3(4.75f, 4.75f, 0.9f)
            );
            localizedName = "Graphenite Kiln";
            description = "Big version of legacy Graphenite forge, consumes more resources to produce more Graphenite.";
            health = 240;
            liquidCapacity = 20;
            size = 6;
            hasPower = true;
            hasLiquids = false;
            hasItems = true;
            craftTime = 90;
            updateEffect = Fx.plasticburn;
            consumes.power(2f);
            consumes.items(ItemStack.with(
                    Items.graphite, 3,
                    Items.silicon, 2,
                    Items.titanium, 1,
                    Items.pyratite, 1
            ));
            consumes.liquid(Liquids.water,
                    0.1f);
            requirements(category, ItemStack.with(
                    Items.lead, 240,
                    ModItems.graphenite, 120,
                    Items.titanium, 50,
                    ModItems.odinum, 70,
                    Items.silicon, 130,
                    Items.graphite, 100
            ));
            category = Category.crafting;
            outputItem = new ItemStack(ModItems.graphenite, 4);
        }};
        odinumExtractor = new GenericCrafter("odinum-extractor") {{
            localizedName = "Odinum Extractor";
            description = "This forge extracts Odinum from Plastanium, Titanium and Thorium.";
            health = 250;
            liquidCapacity = 0;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            hasItems = true;
            craftTime = 100;
            updateEffect = Fx.plasticburn;
            consumes.power(3f);
            consumes.items(ItemStack.with(Items.plastanium, 1, Items.thorium, 3, Items.titanium, 1));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 60, Items.titanium, 100, Items.metaglass, 50, Items.silicon, 150, Items.graphite, 80));
            outputItem = new ItemStack(ModItems.odinum, 2);
        }};
        phaseAlloySmelter = new GenericCrafter("phase-alloy-smelter") {{
            localizedName = "Dense Composite Smelter";
            description = "Produces universal Dense Composite from Plastanium, Surge alloy and Phase fabric.";
            health = 310;
            liquidCapacity = 0;
            size = 3;
            hasPower = true;
            hasLiquids = false;
            hasItems = true;
            craftTime = 90;
            updateEffect = Fx.plasticburn;
            consumes.power(3f);
            consumes.items(ItemStack.with(Items.plastanium, 1, Items.surgeAlloy, 1, Items.phaseFabric, 1));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.phaseFabric, 100, Items.plastanium, 100, Items.thorium, 400, ModItems.exoticAlloy, 270, ModItems.graphenite, 380));
            outputItem = new ItemStack(ModItems.phaseAlloy, 2);
        }};
        plasticForge = new GenericCrafter("plastic-forge") {{
            localizedName = "Plastic Forge";
            description = "Consumes Plastanium, Oil and Thorium to produce ultra-light Plastic.";
            health = 330;
            liquidCapacity = 15;
            size = 4;
            hasPower = true;
            hasLiquids = true;
            hasItems = true;
            craftTime = 100;
            updateEffect = Fx.plasticburn;
            consumes.power(2.4f);
            consumes.liquid(Liquids.oil, 0.1f);
            consumes.items(ItemStack.with(Items.plastanium, 1, Items.thorium, 3));
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.phaseFabric, 100, Items.plastanium, 200, Items.thorium, 300, Items.silicon, 470, ModItems.exoticAlloy, 130, ModItems.graphenite, 380));
            outputItem = new ItemStack(ModItems.plastic, 2);
        }};
        refrigerantmixer = new GenericCrafter("refrigerantmixer") {{
            localizedName = "Thorium Refrigerant Mixer";
            description = "Makes cool Refrigirant from Cryofluid and crushed Thorium.";
            health = 130;
            size = 2;
            consumes.power(3f);
            consumes.liquid(Liquids.cryofluid, 0.1f);
            consumes.items(ItemStack.with(Items.thorium, 2));
            outputLiquid = new LiquidStack(ModLiquids.thoriumRefrigerant, 18f);
            drawer = new DrawSmelter();
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 200, Items.thorium, 200, Items.titanium, 100, Items.metaglass, 130, ModItems.graphenite, 190));
            updateEffect = Fx.purify;
            updateEffectChance = 0.02f;
        }};
        methaneGasifier = new GenericCrafterWithGas("methane-gasifier") {
            {
                localizedName = "Methane Extractor";
                description = "Extract Methane from Oil";
                health = 140;
                size = 2;
                hasPower = true;
                hasLiquids = true;
                hasGasses = true;
                hasItems = true;
                consumes.power(2.5f);
                consumes.liquid(Liquids.oil, 0.16f);
                outputGas = new GasStack(ModGasses.methane, 8);
                requirements(Category.crafting, ItemStack.with(ModItems.odinum, 100, Items.titanium, 100, Items.metaglass, 130, ModItems.chromium, 190));
                updateEffect = Fx.purify;
                updateEffectChance = 0.02f;
            }
        };
        hyperDenseCompositeSmelter = new GasGenericCrafter("hyper-dense-composite-smelter") {{
            drawer=new GasDrawSmelter();
            localizedName = "Hyper Dense Composite Smelter";
            description = "Upgraded version of Dense Composite smelter but consumes gas for more effective production.";
            health = 510;
            liquidCapacity = 20;
            size = 6;
            hasPower = true;
            hasLiquids = false;
            hasGasses = true;
            hasItems = true;
            craftTime = 100;
            updateEffect = Fx.fuelburn;
            consumes.power(5f);
            consumes.items(ItemStack.with(Items.plastanium, 3, Items.surgeAlloy, 2, Items.phaseFabric, 2));
            consumes.addGas(new ConsumeGas(ModGasses.methane, 1));
            requirements(Category.crafting, ItemStack.with(Items.phaseFabric, 100, Items.plastanium, 110, Items.thorium, 400, ModItems.exoticAlloy, 270, ModItems.graphenite, 360));
            outputItem = new ItemStack(ModItems.phaseAlloy, 5);
        }};
        methaneLiquifier = new GasGenericCrafter("methane-liquifier") {{
            localizedName = "Methane Liquifier";
            description = "Turn Methane into Liquid.";
            health = 150;
            liquidCapacity = 20;
            itemCapacity = 25;
            gasCapacity = 15;
            size = 2;
            hasPower = true;
            hasLiquids = true;
            hasGasses = true;
            hasItems = true;
            craftTime = 100;
            updateEffect = ModFx.contritumUpdate;
            consumes.power(3f);
            consumes.liquid(Liquids.water, 0.1f);
            consumes.addGas(new ConsumeGas(ModGasses.methane, 0.5f));
            requirements(Category.crafting, ItemStack.with(ModItems.chromium, 130, Items.plastanium, 90, Items.metaglass, 160, ModItems.graphenite, 200));
            outputLiquid = new LiquidStack(ModLiquids.liquidMethane, 1f);
        }};
        hyperExoAlloySmelter = new GasGenericCrafter("hyper-exotic-alloy-smelter") {{
            drawer=new GasDrawSmelter();
            localizedName = "Hyper Exotic Alloy Smelter";
            description = "More powerful and effective smelter of Exotic Alloy, consumes Methane.";
            health = 420;
            liquidCapacity = 16;
            size = 6;
            hasPower = true;
            hasLiquids = false;
            hasGasses = true;
            hasItems = true;
            craftTime = 120;
            updateEffect = Fx.impactsmoke;
            consumes.power(5f);
            consumes.items(ItemStack.with(Items.titanium, 5, Items.thorium, 4, ModItems.graphenite, 4));
            consumes.addGas(new ConsumeGas(ModGasses.methane, 0.5f));
            requirements(Category.crafting, ItemStack.with(Items.plastanium, 120, Items.thorium, 420, ModItems.exoticAlloy, 120, ModItems.graphenite, 210, Items.titanium, 300));
            outputItem = new ItemStack(ModItems.exoticAlloy, 5);
        }};


        hydraulicDrill = new Drill("hydraulic-drill") {{
            localizedName = "Hydraulic Drill";
            description = "This drill uses hydraulics for more effective work. Consumes water.";
            size = 3;
            tier = 4;
            consumes.liquid(Liquids.water, 0.1f).optional(false, false);
            hasLiquids = true;
            rotateSpeed = 2;
            drillTime = 245;
            requirements(Category.production, ItemStack.with(Items.plastanium, 15, Items.silicon, 45, Items.graphite, 45, ModItems.odinum, 65));
            ambientSound = Sounds.drill;
            ambientSoundVolume = 0.01f;
        }};
        geothermicDrill = new MultiRotatorDrill("geothermic-drill") {{
            localizedName = "Geothermic Drill";
            description = "4 rotators, more efficiency, consumes Magma";
            requirements(Category.production, ItemStack.with(
                    ModItems.chromium, 290, Items.titanium, 400,
                    Items.silicon, 300, ModItems.graphenite, 575,
                    ModItems.odinum, 190, Items.plastanium, 120));
            size = 9;
            health = 1460;
            hasLiquids = true;
            liquidCapacity = 20;
            drawMineItem = false;
            tier = 8;
            rotators(new Rotator(2, 2), new Rotator(2, 6), new Rotator(6, 2), new Rotator(6, 6));
            rotateSpeed = 3.2f;
            consumes.power(6.8F);
            consumes.liquid(ModLiquids.magma, 0.4F);
        }};
        quarryDrill = new Drill("quarry-drill") {{
            localizedName = "Quarry Drill";
            description = "A fastest, most efficient gigantic drill.";
            size = 5;
            health = 260;
            drillTime = 210;
            tier = 5;
            drawRim = true;
            consumes.power(2f);
            consumes.liquid(ModLiquids.thoriumRefrigerant, 0.1f).optional(false, false);
            hasLiquids = true;
            liquidCapacity = 60;
            rotateSpeed = 5;
            requirements(Category.production, ItemStack.with(ModItems.plastic, 150, ModItems.graphenite, 120, ModItems.odinum, 140, Items.plastanium, 100));
        }};
    }
}
