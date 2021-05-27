package braindustry.content.Blocks;

import Gas.world.blocks.distribution.GasRouter;
import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.type.Rotor;
import braindustry.world.blocks.distribution.CrossItemBridge;
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
import mindustry.world.blocks.distribution.StackConveyor;
import mindustry.world.blocks.liquid.Conduit;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.storage.StorageBlock;

import static braindustry.content.Blocks.ModBlocks.*;

class ModOtherBlocks implements ContentList {
    @Override
    public void load() {
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
                return block.acceptsItems || block instanceof StorageBlock;
            };
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
            requirements(Category.production, ItemStack.with(Items.plastanium, 15, Items.silicon, 40, Items.graphite, 45, ModItems.odinum, 75));
            ambientSound = Sounds.drill;
            ambientSoundVolume = 0.01f;
        }};
        geothermicDrill = new MultiRotorDrill("geothermic-drill") {{
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
            rotors(new Rotor(2, 2), new Rotor(2, 6), new Rotor(6, 2), new Rotor(6, 6));
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
