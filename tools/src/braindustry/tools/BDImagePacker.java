package braindustry.tools;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Pixmap;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.TextureAtlas;
import arc.graphics.g2d.TextureRegion;
import arc.math.geom.Vec2;
import arc.struct.ObjectMap;
import arc.struct.Seq;
import arc.util.*;
import arc.util.serialization.Json;
import arc.util.serialization.Jval;
import braindustry.BDVars;
import braindustry.gen.BDContentRegions;
import braindustry.gen.BDEntityMapping;
import mindustry.Vars;
import mindustry.ctype.Content;
import mindustry.ctype.MappableContent;
import mindustry.ctype.UnlockableContent;
import mindustry.mod.Mods;
import mindustry.tools.ImagePacker;
import mma.tools.ModImagePacker;

public class BDImagePacker extends ModImagePacker {

    public BDImagePacker() {
    }

    @Override
    protected void init() {
        super.init();
        BDEntityMapping.init();
    }

    @Override
    protected void runGenerators() {
        new BDGenerators();
    }

    @Override
    protected void checkContent(Content content) {
        super.checkContent(content);
        if (content instanceof MappableContent){
            BDContentRegions.loadRegions((MappableContent) content);
        }
    }

    public static void main(String[] args) throws Exception {
        new BDImagePacker();
    }

}
