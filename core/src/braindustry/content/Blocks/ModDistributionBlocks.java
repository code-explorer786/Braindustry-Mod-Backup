package braindustry.content.Blocks;

import Gas.world.blocks.distribution.GasRouter;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.type.Rotor;
import braindustry.world.blocks.distribution.CrossItemBridge;
import braindustry.world.blocks.distribution.SideJunction;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.distribution.SmartSorter;
import braindustry.world.blocks.production.MultiRotorDrill;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.ctype.ContentList;
import mindustry.gen.Sounds;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.ArmoredConveyor;
import mindustry.world.blocks.distribution.Conveyor;
import mindustry.world.blocks.distribution.ItemBridge;
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.StorageBlock;

import static braindustry.content.Blocks.ModBlocks.*;

class ModDistributionBlocks implements ContentList {
    @Override
    public void load() {
        smartRouter = new SmartRouter("smart-router") {{
            localizedName = "Smart Router";
            description = "Regular router with a choice of input / output paths";
            size = 1;
            requirements(Category.distribution, ItemStack.with(Items.copper, 3, Items.silicon, 10));
            buildCostMultiplier = 4.0F;
        }};
        smartSorter = new SmartSorter("smart-sorter") {{
            localizedName = "Smart Sorter";
            description = "Sorts items not only by quantity, but also by the item itself.";
            requirements(Category.distribution, ItemStack.with(Items.titanium, 2, Items.copper, 3));
        }};
        sideJunction = new SideJunction("side-junction") {{
            localizedName = "Side Junction";
            description = "Transports objects sideways.";
            health = 40;
            buildCostMultiplier = 6;
            requirements(Category.distribution, ItemStack.with(Items.titanium, 2));
        }};
        gasRouter = new GasRouter("gas-router") {{
            localizedName = "Gas Router";
            description = "Simple router for gasses.";
            health = 50;
            hasGas = true;
            gasCapacity = 20;
            requirements(Category.liquid, ItemStack.with(Items.titanium, 1, ModItems.graphenite, 1, Items.metaglass, 3));
        }};
        gasTank = new GasRouter("gas-tank") {{
            localizedName = "Gas Tank";
            description = "Storage gas";
            size = 3;
            gasCapacity = 1500f;
            health = 500;
            requirements(Category.liquid, ItemStack.with(Items.titanium, 25, Items.metaglass, 25));
        }};
        armoredPlastaniumConveyor = new ArmoredConveyor("armored-plastanium-conveyor") {{
            localizedName = "Armored Plastanium Conveyor";
            description = "Wonderful and strong Plastanium Conveyor.";
            health = 240;
            hasItems = true;
            itemCapacity = 6;
            requirements(Category.distribution, ItemStack.with(Items.silicon, 2, ModItems.graphenite, 1, Items.metaglass, 1, Items.plastanium, 2));
            speed = 0.3f;
        }};
        chromiumConduit = new Conduit("chromium-conduit") {{
            localizedName = "Chromium Conduit";
            size = 1;
            description = "Heat resistant and fast conduit.";
            health = 190;
            liquidCapacity = 20;
            rotate = true;
            solid = false;
            floating = true;
            requirements(Category.liquid, ItemStack.with(ModItems.chromium, 2, Items.metaglass, 2, Items.thorium, 1));
        }};
        plasticConveyor = new Conveyor("plastic-conveyor") {{
            localizedName = "Hermetic Plastic Conveyor";
            description = "The most fast and durable conveyor.";
            health = 168;
            requirements(Category.distribution, ItemStack.with(Items.silicon, 1, ModItems.graphenite, 1, ModItems.plastic, 1));
            speed = 0.4f;
        }};
        surgeConveyor = new StackConveyor("surge-conveyor") {{
            localizedName = "Surge Conveyor";
            requirements(Category.distribution, ItemStack.with(Items.surgeAlloy, 1, Items.silicon, 1, ModItems.graphenite, 1));
            health = 100;
            speed = 0.12f;
            itemCapacity = 12;
        }};
        phaseAlloyConveyor = new CrossItemBridge("phase-alloy-conveyor") {{
            localizedName = "Dense Composite Conveyor";
            requirements(Category.distribution, ItemStack.with(ModItems.phaseAlloy, 5, Items.silicon, 7, Items.lead, 10, Items.graphite, 10));
            range = 12;
            canOverdrive = false;
            hasPower = true;
            consumes.power(0.3F);
            /** custom connect filter*/
            connectFilter = (build) -> {
                Block block = build.block;
//                return block.acceptsItems || block instanceof StorageBlock;
                return block instanceof ItemBridge;
            };
        }};
    }


}
