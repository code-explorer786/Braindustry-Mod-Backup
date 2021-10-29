package braindustry.entities.comp;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.annotations.Annotations;

import braindustry.content.Blocks.ModBlocks;
import braindustry.entities.abilities.ModAbility;
import braindustry.entities.abilities.PowerGeneratorAbility;
import braindustry.gen.ModEntityc;
import braindustry.gen.PowerGeneratorc;
import braindustry.graphics.BlackHoleDrawer;
import braindustry.world.blocks.Unit.power.UnitPowerGenerator;
import braindustry.world.blocks.Unit.power.UnitPowerNode;
import mindustry.core.Renderer;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Entityc;
import mindustry.gen.Unitc;
import mindustry.graphics.Layer;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;

@Annotations.Component
abstract class PowerGeneratorComp implements Entityc, Unitc, ModEntityc, PowerGeneratorc {
    public transient UnitPowerGenerator.UnitPowerGeneratorBuild generatorBuilding;
    public transient UnitPowerNode.UnitPowerNodeBuild nodeBuild;
    public transient Seq<Building> links = new Seq<>();
    @Annotations.Import
    float x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, ammo, minFormationSpeed, dragMultiplier;
    @Annotations.Import
    Team team;
    @Annotations.Import
    UnitType type;
    transient Seq<Building> oldLinks = new Seq<>();
    private transient boolean initStats = false;
    private transient BlackHoleDrawer blackHoleDrawer;

    public PowerGeneratorAbility powerAbility() {
        if (modUnitType() == null) {
            return null;
        }
        for (ModAbility modAbility : modUnitType().getModAbilities()) {
            if (modAbility instanceof PowerGeneratorAbility) {
                return (PowerGeneratorAbility) modAbility;
            }
        }
        return null;
    }

    public BlackHoleDrawer blackHoleDrawer() {
        if (blackHoleDrawer == null) blackHoleDrawer = new BlackHoleDrawer(self());
        return blackHoleDrawer;
    }

    @Override
    public void update() {
        PowerGeneratorAbility powerAbility = powerAbility();
//        Log.info("unit: @,ability: @",toString(),powerAbility);
        if (powerAbility != null) {
            if (blackHoleDrawer == null) blackHoleDrawer = new BlackHoleDrawer(self());
            blackHoleDrawer.update();
            loadStats();
            reloadLinks();
            for (Building building : links) {
                if (building == null) return;
                PowerGraph graph = building.power.graph;
                graph.reflow(generatorBuilding);
            }

        }
    }

    private <T extends Building> T createBuild(Block block) {
        Building building = block.buildType.get().create(block, team);
        return (T) building;
    }

    public void resetLinks() {
        if (links == null) links = new Seq<>();
        for (Building b : links) {
            if (b == null) return;
            nodeBuild.setConnect(b, false);
//            b.power.graph.remove(unit.generatorBuilding);
        }
        links.clear();
        if (generatorBuilding != null) generatorBuilding.kill();
        if (nodeBuild != null) nodeBuild.kill();
    }

    private void sortLinks() {

        links = links.sort(b -> {
            if (b == null) return Float.MAX_VALUE;
            return b.dst(this);
        });
        for (Building link : links.copy()) {
            if (!oldLinks.contains(link)) {
                nodeBuild.setConnect(link, true);
            }
        }
        ;

        for (Building oldLink : oldLinks.copy()) {
            if (!links.contains(oldLink)) {
                nodeBuild.setConnect(oldLink, false);
            }
        }
    }

    public void draw() {
//        System.out.println("Connect buildings:");
      /*  UnitPowerNode.getPotentialLinks(tileOn(),nodeBuild, self(), (link) -> {
            boolean canConnect = link.power != null && !(link.block instanceof PowerNode && ((PowerNode) link.block).maxNodes <= link.power.links.size);
            Draw.color(Mathf.num(canConnect),Mathf.num(oldLinks.contains(link)),Mathf.num(goodLink(link)),1);
            Lines.line(x,y,link.x,link.y);
//            System.out.print(""+link+" ");
        });*/
//        System.out.println();
        float z = elevation > 0.5F ? (type.lowAltitude ? 90.0F : 115.0F) : type.groundLayer + Mathf.clamp(type.hitSize / 4000.0F, 0.0F, 0.01F);
        drawLasers(Math.max(z + 0.1f, Layer.power));
    }

    public void drawLasers(float z) {
        if (!Mathf.zero(Renderer.laserOpacity) && nodeBuild != null) {
            Draw.z(z);
            nodeBuild.setupColor(nodeBuild.power.graph.getSatisfaction());
            for (Building link : links.copy()) {
                modUnitType().drawLaser(team, x, y, link.x, link.y, 1, link.block.size);
            }

            Draw.reset();
        }
    }

    public void loadStats() {
        if (initStats) return;
        initStats = true;

        generatorBuilding = createBuild((UnitPowerGenerator) ModBlocks.unitGenerator);
        nodeBuild = createBuild((UnitPowerNode) ModBlocks.unitNode);
        generatorBuilding.setParent(self());
        nodeBuild.setParent(self());
        nodeBuild.setConnect(generatorBuilding, true);
    }

    public void reloadLinks() {
        oldLinks = links.copy();
        links.clear();
//        oldLinks.each(l->nodeBuild.setConnect(l,false));
        Tile tile = tileOn();
        nodeBlock().getPotentialLinks(tile, oldLinks, self(), (link) -> {
            boolean canConnect = link.power != null && !(link.block instanceof PowerNode && ((PowerNode) link.block).maxNodes <= link.power.links.size);
            if (goodLink(link) && !links.contains(link) && links.size < maxNodes() && (canConnect || oldLinks.contains(link))) {
                links.add(link);
            }
        });
//        links.each(l->nodeBuild.setConnect(l,true));
        sortLinks();
    }

    public boolean goodLink(Building link) {
        return link != null && link.isValid() && powerAbility().good.get(link, self());
    }

    public int maxNodes() {
        return powerAbility().maxNodes;
    }

    public float laserRange() {
        return powerAbility().laserRange;
    }

    private UnitPowerNode nodeBlock() {
        return (UnitPowerNode) ModBlocks.unitNode;
    }

    public void remove() {
        resetLinks();
    }
}
