package Gas.gen;

import Gas.world.blocks.gas.GasConduit;
import Gas.world.blocks.gas.GasGasBlock;
import Gas.world.blocks.power.AllBurnerGenerator;
import Gas.world.blocks.power.AllGenerator;
import Gas.world.blocks.power.GasImpactReactor;
import Gas.world.blocks.production.GasGenericSmelter;
import arc.Core;
import arc.graphics.g2d.TextureRegion;
import mindustry.ctype.MappableContent;

public class GasContentRegions {
  public static void loadRegions(MappableContent content) {
    if(content instanceof AllBurnerGenerator) {
      ((AllBurnerGenerator)content).turbineRegions = new TextureRegion[2];
      for(int INDEX0 = 0; INDEX0 < 2; INDEX0 ++) {
        ((AllBurnerGenerator)content).turbineRegions[INDEX0] = Core.atlas.find("" + content.name + "-turbine" + INDEX0 + "");
      }
      ((AllBurnerGenerator)content).capRegion = Core.atlas.find("" + content.name + "-cap");
    }
    if(content instanceof GasGasBlock) {
      ((GasGasBlock)content).gasRegion = Core.atlas.find("" + content.name + "-gas");
      ((GasGasBlock)content).topRegion = Core.atlas.find("" + content.name + "-top");
      ((GasGasBlock)content).bottomRegion = Core.atlas.find("" + content.name + "-bottom", "" + content.name + "");
    }
    if(content instanceof GasConduit) {
      ((GasConduit)content).topRegions = new TextureRegion[5];
      for(int INDEX0 = 0; INDEX0 < 5; INDEX0 ++) {
        ((GasConduit)content).topRegions[INDEX0] = Core.atlas.find("" + content.name + "-top-" + INDEX0 + "");
      }
      ((GasConduit)content).botRegions = new TextureRegion[5];
      for(int INDEX0 = 0; INDEX0 < 5; INDEX0 ++) {
        ((GasConduit)content).botRegions[INDEX0] = Core.atlas.find("" + content.name + "-bottom-" + INDEX0 + "", "conduit-bottom-" + INDEX0 + "");
      }
    }
    if(content instanceof GasImpactReactor) {
      ((GasImpactReactor)content).bottomRegion = Core.atlas.find("" + content.name + "-bottom");
      ((GasImpactReactor)content).plasmaRegions = new TextureRegion[4];
      for(int INDEX0 = 0; INDEX0 < 4; INDEX0 ++) {
        ((GasImpactReactor)content).plasmaRegions[INDEX0] = Core.atlas.find("" + content.name + "-plasma-" + INDEX0 + "");
      }
    }
    if(content instanceof GasGenericSmelter) {
      ((GasGenericSmelter)content).topRegion = Core.atlas.find("" + content.name + "-top");
    }
    if(content instanceof AllGenerator) {
      ((AllGenerator)content).topRegion = Core.atlas.find("" + content.name + "-top");
      ((AllGenerator)content).liquidRegion = Core.atlas.find("" + content.name + "-liquid");
      ((AllGenerator)content).gasRegion = Core.atlas.find("" + content.name + "-gas");
    }
  }
}
