package braindustry.gen;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import braindustry.world.blocks.TestBlock;
import braindustry.world.blocks.distribution.ArmoredPayloadConveyor;
import braindustry.world.blocks.distribution.PayloadBridge;
import braindustry.world.blocks.distribution.SmartRouter;
import braindustry.world.blocks.power.MaterialReactor;
import braindustry.world.blocks.power.ReceivingPowerNode;
import braindustry.world.blocks.sandbox.DpsMeter;
import braindustry.world.blocks.sandbox.UnitSpawner;
import mindustry.ctype.MappableContent;

public class ModContentRegions {
  public static void loadRegions(MappableContent content) {
    if(content instanceof MaterialReactor) {
      ((MaterialReactor)content).lightsRegion = Core.atlas.find("" + content.name + "-lights");
    }
    if(content instanceof PayloadBridge) {
      ((PayloadBridge)content).endRegion = Core.atlas.find("" + content.name + "-end");
      ((PayloadBridge)content).bridgeRegion = Core.atlas.find("" + content.name + "-bridge");
      ((PayloadBridge)content).arrowRegion = Core.atlas.find("" + content.name + "-arrow");
    }
    if(content instanceof ArmoredPayloadConveyor) {
      ((ArmoredPayloadConveyor)content).up = Core.atlas.find("" + content.name + "-up");
    }
    if(content instanceof DpsMeter) {
      ((DpsMeter)content).teamRegionButton = Core.atlas.find("" + content.name + "-team-region");
    }
    if(content instanceof ReceivingPowerNode) {
      ((ReceivingPowerNode)content).rotatorRegion = Core.atlas.find("" + content.name + "-rotator");
      ((ReceivingPowerNode)content).bottomRegion = Core.atlas.find("" + content.name + "-bottom");
    }
    if(content instanceof UnitSpawner) {
      ((UnitSpawner)content).colorRegion = Core.atlas.find("" + content.name + "-color", "air");
    }
    if(content instanceof SmartRouter) {
      ((SmartRouter)content).cross = Core.atlas.find("" + content.name + "-cross");
      ((SmartRouter)content).arrow = Core.atlas.find("" + content.name + "-arrow");
    }
    if(content instanceof TestBlock) {
      ((TestBlock)content).doubleSize = new TextureRegion[4];
      for(int INDEX0 = 0; INDEX0 < 4; INDEX0 ++) {
        ((TestBlock)content).doubleSize[INDEX0] = Core.atlas.find("" + content.name + "-2-" + INDEX0 + "", "" + content.name + "");
      }
      ((TestBlock)content).triableSize = new TextureRegion[1];
      for(int INDEX0 = 0; INDEX0 < 1; INDEX0 ++) {
        ((TestBlock)content).triableSize[INDEX0] = Core.atlas.find("" + content.name + "-3-" + INDEX0 + "", "" + content.name + "");
      }
      ((TestBlock)content).doubleTop = Core.atlas.find("" + content.name + "-2-top");
    }
  }
}
