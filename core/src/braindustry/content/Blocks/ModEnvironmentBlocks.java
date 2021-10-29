package braindustry.content.Blocks;

import braindustry.content.ModItems;
import braindustry.content.ModLiquids;
import braindustry.graphics.ModPal;
import mindustry.content.*;
import mindustry.ctype.ContentList;
import mindustry.graphics.CacheLayer;
import mindustry.world.blocks.environment.*;

import static braindustry.content.Blocks.ModBlocks.*;

class ModEnvironmentBlocks implements ContentList {

    public void load() {
        magmaFloor = new Floor("magma-floor") {{
            localizedName = "Magma";
            isLiquid = true;
            variants = 1;
            //blendGroup = ModBlocks.;
            cacheLayer = CacheLayer.tar;
            speedMultiplier = 0.17f;
            status = StatusEffects.blasted;
            liquidDrop = ModLiquids.magma;
            statusDuration = 240;
            drownTime = 90;
            walkEffect = Fx.melting;
            drownUpdateEffect = Fx.burning;
        }};
        obsidianBlock = new StaticWall("obsidian-wall") {{
            localizedName = "Obsidian Block";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        obsidianFloor = new Floor("obsidian-floor") {{
            localizedName = "Obsidian Floor";
            variants = 3;
        }};
        oreChromium = new OreBlock("ore-chromium") {{
            itemDrop = ModItems.chromium;
        }};
        oreOdinum = new OreBlock("ore-odinum") {{
            itemDrop = ModItems.odinum;
        }};

        invisibleWall = new StaticWall("invisible-wall") {{
            localizedName = "Invisible Wall";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 1;

        }};

        crimzesWall = new StaticWall("crimzes-wall") {{
            localizedName = "Crimson Wall";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        jungleWall = new StaticWall("jungle-shrubs") {{
            localizedName = "Jungle Shrubs";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        dirtRocksWall = new StaticWall("dirt-rocks") {{
            localizedName = "Hard Dirt Wall";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        jungleFloor = new Floor("jungle-grass") {{
            localizedName = "Jungle Grass";
            variants = 3;
        }};
        crimzesFloor = new Floor("crimzes-floor") {{
            localizedName = "Crimzes Floor";
            variants = 3;
        }};
        graysand = new Floor("graysand") {{
            localizedName = "Gray Sand";
            itemDrop = Items.sand;
            playerUnmineable = true;
        }};
        liquidMethaneFloor = new Floor("liquid-methane-floor") {{
            localizedName = "Liquid Methane";
            isLiquid = true;
            variants = 1;
            blendGroup = Blocks.water;
            cacheLayer = CacheLayer.tar;
            speedMultiplier = -0.15f;
            status = StatusEffects.freezing;
            liquidDrop = ModLiquids.liquidMethane;
            statusDuration = 200;
            drownTime = 90;
            walkEffect = Fx.freezing;
            drownUpdateEffect = Fx.freezing;
        }};
        blackIceWall = new StaticWall("black-ice-wall") {{
            localizedName = "Black Ice Wall";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        blackSnowWall = new StaticWall("black-snow-wall") {{
            localizedName = "Black Snow Wall";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        darkShrubs = new StaticWall("dark-shrubs") {{
            localizedName = "Dark Shrubs";
            breakable = false;
            alwaysReplace = false;
            solid = true;
            variants = 2;
        }};
        blackIce = new Floor("black-ice") {{
            localizedName = "Black Ice";
            variants = 3;
        }};
        blackSnow = new Floor("black-snow") {{
            localizedName = "Black Snow";
            variants = 3;
        }};
        swampSandWater = new ShallowLiquid("swamp-sand-water-floor") {{
            localizedName = "Swamp Sand Water";
            speedMultiplier = 0.7f;
            statusDuration = 60f;
            variants = 1;
            albedo = 0.3f;
        }};
        swampWater = new Floor("swamp-water-floor") {{
            localizedName = "Swamp Water";
            isLiquid = true;
            variants = 1;
            blendGroup = Blocks.water;
            cacheLayer = CacheLayer.water;
            speedMultiplier = -0.5f;
            status = StatusEffects.corroded;
            liquidDrop = Liquids.water;
            statusDuration = 120;
            drownTime = 60;
            walkEffect = Fx.muddy;
            drownUpdateEffect = Fx.muddy;
        }};
        greenTree = new TreeBlock("green-tree") {{
            localizedName = "Green Tree";
        }};

        fluorescentTree = new TreeBlock("fluorescent-tree") {{
            localizedName = "Fluorescent Tree";
            emitLight = true;
            lightRadius = 85f;
            lightColor = ModPal.fluorescentColor.cpy().a(0.63f);
        }};

        blackTree = new TreeBlock("black-tree") {{
            localizedName = "Black Tree";
        }};

        darkPine = new StaticTree("dark-pine") {{
            localizedName = "Dark Pine";
            variants = 0;
        }};

        metallicPine = new StaticTree("metallic-pine") {{
            localizedName = "Metallic Pine";
            variants = 0;
        }};

        fluorescentPine = new StaticTree("fluorescent-pine") {{
            localizedName = "Fluorescent Pine";
            variants = 0;
            emitLight = true;
            lightRadius = 55f;
            lightColor = ModPal.fluorescentColor.cpy().a(0.63f);
        }};

        darkShrubsFloor = new Floor("black-grass") {{
            localizedName = "Dark Grass";
            variants = 3;
        }};
    }
}
