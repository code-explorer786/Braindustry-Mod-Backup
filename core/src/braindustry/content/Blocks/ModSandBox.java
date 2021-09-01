package braindustry.content.Blocks;

import braindustry.world.blocks.sandbox.UnitSpawner;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

import static braindustry.content.Blocks.ModBlocks.unitSpawner;

class ModSandBox implements ContentList {
    public void load() {
        unitSpawner = new UnitSpawner("unit-spawner") {{
            localizedName = "Unit Spawner";
            description = "Powerful sandbox block, can spawn and control any unit from game and mods.";
            size = 2;
            requirements(Category.effect, BuildVisibility.sandboxOnly, ItemStack.empty);
        }};
    }
}
