package braindustry.entities.comp;

import arc.graphics.g2d.Draw;
import arc.math.Mathf;
import arc.struct.IntSeq;
import arc.struct.Seq;
import arc.util.Log;
import braindustry.annotations.ModAnnotations;
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
import mindustry.gen.Unit;
import mindustry.gen.Unitc;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.blocks.power.PowerGraph;
import mindustry.world.blocks.power.PowerNode;

import static mindustry.Vars.world;

@ModAnnotations.Component
abstract class PowerGeneratorComp implements Entityc, Unitc, ModEntityc, PowerGeneratorc {
    public transient UnitPowerGenerator.UnitPowerGeneratorBuild generatorBuilding;
    public transient UnitPowerNode.UnitPowerNodeBuild nodeBuild;
    public Seq<Integer> links = new Seq<Integer>();
    @ModAnnotations.Import
    float x, y, rotation, elevation, maxHealth, drag, armor, hitSize, health, ammo, minFormationSpeed, dragMultiplier;
    @ModAnnotations.Import
    Team team;
    @ModAnnotations.Import
    UnitType type;
    Seq<Integer> oldLinks = new Seq<Integer>();
    private transient boolean initStats = false;
    @ModAnnotations.ReadOnly
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
        if (blackHoleDrawer==null)blackHoleDrawer=new BlackHoleDrawer(self());
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
            for (int pos : links.copy()) {
                Building building = world.build(pos);
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
        for (int pos : links) {
            Building b = world.build(pos);
            if (b == null) return;
            nodeBuild.setConnect(b, false);
//            b.power.graph.remove(unit.generatorBuilding);
        }
        if (generatorBuilding != null) generatorBuilding.kill();
        if (nodeBuild != null) nodeBuild.kill();
    }

    private void sortLinks() {

        links = links.sort(pos -> {
            Building b = world.build(pos);
            if (b == null) return Float.MAX_VALUE;
            return b.dst(this);
        });
        for (int pos : links.copy()) {
            Building link = world.build(pos);
            if (link == null) {
                links.remove(Integer.valueOf(pos));
                continue;
            } else if (link.pos()!=pos){
//                links.remove(Integer.valueOf(pos));
//             if (oldLinks.contains(pos))   links.add(link.pos());
            }
            if (!oldLinks.contains(link.pos())) {
                nodeBuild.setConnect(link, true);
            }
        }
        ;

        for (int pos : oldLinks.copy()) {
            Building oldLink = world.build(pos);
            if (oldLink == null) continue;
            if (!links.contains(oldLink.pos())) {
                nodeBuild.setConnect(oldLink, false);
            }
        }
    }

    public void draw() {

        nodeBlock().getPotentialLinks(tileOn(), self(), (link) -> {
            boolean canConnect = link.power != null && !(link.block instanceof PowerNode && ((PowerNode) link.block).maxNodes <= link.power.links.size);
            if (goodLink(link) && (canConnect || oldLinks.contains(link.pos()))) {

            }
        });
        float z = elevation > 0.5F ? (type.lowAltitude ? 90.0F : 115.0F) : type.groundLayer + Mathf.clamp(type.hitSize / 4000.0F, 0.0F, 0.01F);
        drawLasers(z + 0.1f);
    }

    public void drawLasers(float z) {
        if (!Mathf.zero(Renderer.laserOpacity) && nodeBuild != null) {
            Draw.z(z);
            nodeBuild.setupColor(nodeBuild.power.graph.getSatisfaction());
            for (int pos : links.copy()) {
                Building link = world.build(pos);
                if (link == null) continue;
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
        Tile tile = tileOn();
        nodeBlock().getPotentialLinks(tile, self(), (link) -> {
            boolean canConnect = link.power != null && !(link.block instanceof PowerNode && ((PowerNode) link.block).maxNodes <= link.power.links.size);
            if (goodLink(link) && !links.contains(link.pos()) && links.size < maxNodes() && (canConnect || oldLinks.contains(link.pos()))) {
                links.add(link.pos());
            }
        });
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
