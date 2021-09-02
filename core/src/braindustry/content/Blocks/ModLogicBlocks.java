package braindustry.content.Blocks;

import braindustry.world.blocks.logic.LaserRuler;
import mindustry.content.Items;
import mindustry.ctype.ContentList;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.meta.BuildVisibility;

import static braindustry.content.Blocks.ModBlocks.*;

class ModLogicBlocks implements ContentList {
    public void load() {

        laserRuler = new LaserRuler("laser-ruler") {{
            localizedName = "Laser ruler";
            description = "Click on a tile and it will calculate the distance to it.";
            details = "For processor command \"sensor\":"+
                      "The variable range contains the distance to between the block and the selected tile." +
                      " The shootX variable contains the x coordinate of the selected tile." +
                      " The shootY variable contains the y coordinate of the selected tile.";
            size = 1;
            requirements(Category.logic, BuildVisibility.shown, ItemStack.with(
                    Items.copper, 25,
                    Items.metaglass, 5,
                    Items.silicon, 30,
                    Items.titanium, 10
            ));
        }};
    }
}
