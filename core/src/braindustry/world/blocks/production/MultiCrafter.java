package braindustry.world.blocks.production;

import acontent.world.meta.AStats;
import arc.Core;
import arc.func.Func;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.EnumSet;
import arc.struct.Seq;
import arc.util.Structs;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.content.ModFx;
import braindustry.ui.MultiBar;
import braindustry.world.ModBlock;
import braindustry.world.meta.BDStat;
import braindustry.world.meta.RecipeListValue;
import mindustry.Vars;
import mindustry.content.Fx;
import mindustry.content.Liquids;
import mindustry.entities.Effect;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.Liquid;
import mindustry.type.LiquidStack;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.Tile;
import mindustry.world.blocks.ItemSelection;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;

import static braindustry.BDVars.fullName;

public class MultiCrafter extends mma.world.blocks.production.MultiCrafter {
    AStats aStats = new AStats();

    public MultiCrafter(String name) {
        super(name);
        this.stats = aStats.copy(stats);
    }
    @Override
    public void setStats() {
        super.setStats();
        stats.remove(Stat.output);
        aStats.add(BDStat.recipes, new RecipeListValue(recipes));
    }



    public class MultiCrafterBuild extends mma.world.blocks.production.MultiCrafter.MultiCrafterBuild {

    }
}