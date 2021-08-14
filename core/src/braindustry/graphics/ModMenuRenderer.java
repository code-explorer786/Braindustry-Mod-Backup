package braindustry.graphics;

import arc.Core;
import arc.Events;
import arc.files.Fi;
import arc.func.Floatc2;
import arc.graphics.Camera;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.PixmapIO;
import arc.graphics.g2d.*;
import arc.graphics.gl.FrameBuffer;
import arc.graphics.gl.Shader;
import arc.math.Angles;
import arc.math.Mat;
import arc.math.Mathf;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import arc.util.*;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import braindustry.BDVars;
import braindustry.cfunc.BackgroundUnitData;
import braindustry.content.Blocks.ModBlocks;
import braindustry.gen.BackgroundSettings;
import braindustry.tools.BackgroundConfig;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.game.EventType;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.CachedTile;
import mindustry.world.Tile;
import mindustry.world.Tiles;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;

import java.util.Arrays;

import static mindustry.Vars.*;

public class ModMenuRenderer {
    private static final float darkness = 0.3F;
    private final int width = !mobile ? 100 : 60, height = !mobile ? 50 : 40;
    private final Camera camera = new Camera();
    private final Mat mat = new Mat();
    private int[] cacheLayers;
    private int cacheWall;
    private FrameBuffer shadows;
    private CacheBatch batch;
    private float time;
    private float flyerRot = 45f;
    private int units;
    private @Nullable
    UnitType unitType;
    private BackgroundUnitData[] unitsData;

    public ModMenuRenderer() {
        BackgroundUnitData.parent = this;
        buildMain(true);
        Events.on(EventType.StateChangeEvent.class, e -> {
            if (e.to != e.from) {
                resetData();
            }
        });
    }

    public static Shader createShader() {
        return new Shader("attribute vec4 a_position;\nattribute vec4 a_color;\nattribute vec2 a_texCoord0;\nattribute vec4 a_mix_color;\nuniform mat4 u_projTrans;\nvarying vec4 v_color;\nvarying vec4 v_mix_color;\nvarying vec2 v_texCoords;\n\nvoid main(){\n   v_color = a_color;\n   v_color.a = v_color.a * (255.0/254.0);\n   v_mix_color = a_mix_color;\n   v_mix_color.a *= (255.0/254.0);\n   v_texCoords = a_texCoord0;\n   gl_Position = u_projTrans * a_position;\n}",
                "\nvarying lowp vec4 v_color;\nvarying lowp vec4 v_mix_color;\nvarying highp vec2 v_texCoords;\nuniform highp sampler2D u_texture;\n\nvoid main(){\n  vec4 c = texture2D(u_texture, v_texCoords);\n  gl_FragColor = v_color * mix(c, vec4(v_mix_color.rgb, c.a), v_mix_color.a);\n}");
    }

    public static float darkness() {
        return darkness;
    }

    public void takeBackgroundScreenshot() {
        int aFloat = (int) BDVars.settings.getFloat("background.screenshot.scl");
        int w = width * tilesize * aFloat, h = height * tilesize * aFloat;
        int memory = w * h * 4 / 1024 / 1024;

        if (memory >= 65) {
            ui.showInfo("@screenshot.invalid");
            return;
        }
        FrameBuffer buffer = new FrameBuffer(w, h);
        float lastDelta = Time.delta;
        Time.delta = 0;
        render();
        float vpW = camera.width, vpH = camera.height, px = camera.position.x, py = camera.position.y;
        disableUI = true;
        camera.width = w;
        camera.height = h;
        camera.position.x = w / 2f + tilesize / 2f;
        camera.position.y = h / 2f + tilesize / 2f;
        buffer.begin();
        render();
        buffer.end();
        disableUI = false;
        camera.width = vpW;
        camera.height = vpH;
        camera.position.set(px, py);
        buffer.begin();
        byte[] lines = ScreenUtils.getFrameBufferPixels(0, 0, w, h, true);
        for (int i = 0; i < lines.length; i += 4) {
            lines[i + 3] = (byte) 255;
        }
        buffer.end();
        Pixmap fullPixmap = new Pixmap(w, h);
        Buffers.copy(lines, 0, fullPixmap.getPixels(), lines.length);
        Fi file = screenshotDirectory.child("screenshot-background-" + Time.millis() + ".png");
        PixmapIO.writePng(file, fullPixmap);
        fullPixmap.dispose();
        ui.showInfoFade(Core.bundle.format("screenshot", file.toString()));

        buffer.dispose();
        Time.delta = lastDelta;
    }

    public void rebuild() {
        batch.dispose();
        shadows.dispose();
        shadows = null;
        batch = null;
        buildMain(false);
    }

    protected void buildMain(boolean timeMark) {
        if (cacheLayers == null || cacheLayers.length != CacheLayer.all.length) {
            cacheLayers = null;
            cacheLayers = new int[CacheLayer.all.length];
        }
        mat.idt();
        Mathf.rand.setSeed(BackgroundSettings.useWorldSeed() ? BackgroundSettings.worldSeed() : System.nanoTime());
        time = 0.0F;
        units = Mathf.chance(0.2D) ? Mathf.random(35) : Mathf.random(15);
        unitType = content.units().select(u -> u.hitSize <= 20f && u.flying && u.region.found()).random();

        if (BackgroundSettings.units().custom()) {
            UnitType unit = BackgroundSettings.unit();
            if (unit != null) unitType = unit;

        }
        createUnitsData();
        if (timeMark) Time.mark();
        generate();
        cache();
        if (timeMark) Log.info("Time to generate menu: @", Time.elapsed());
    }

    private void createUnitsData() {
        if (unitsData != null) {
            BackgroundUnitData[] unitData = new BackgroundUnitData[units];
            for (int i = 0; i < unitData.length; i++) {
                if (i < unitsData.length) {
                    unitData[i] = unitsData[i];
                    unitData[i].reset();
                } else {

                    unitData[i] = new BackgroundUnitData();
                }
            }
            for (int i = 0; i < unitsData.length; i++) {
                if (i > unitData.length) unitsData[i].clear();
                unitsData[i] = null;
            }
            unitsData = null;
            unitsData = unitData;
        } else {
            unitsData = new BackgroundUnitData[units];
            for (int i = 0; i < unitsData.length; i++) {
                unitsData[i] = new BackgroundUnitData();
            }
        }
    }

    private void generate() {
        Vars.world.beginMapLoad();
        Tiles tiles = Vars.world.resize(this.width, this.height);
        Seq<Block> ores = Vars.content.blocks().select((b) -> {
            return b instanceof OreBlock;
        });
        this.shadows = new FrameBuffer(this.width, this.height);
        int offset = Mathf.random(100000);
        int s1 = offset, s2 = offset + 1, s3 = offset + 2;
        Block[] selected = Structs.select(new Block[][]{
                {Blocks.sand, Blocks.sandWall},
                {Blocks.shale, Blocks.shaleWall},
                {Blocks.ice, Blocks.iceWall},
                {ModBlocks.obsidianFloor, ModBlocks.obsidianBlock},
                {Blocks.sand, Blocks.sandWall},
                {Blocks.shale, Blocks.shaleWall},
                {Blocks.ice, Blocks.iceWall},
                {ModBlocks.obsidianFloor, ModBlocks.obsidianBlock},
                {Blocks.moss, Blocks.sporePine},
        });
        Block[] selected2 = Structs.select(new Block[][]{
                {Blocks.basalt, Blocks.duneWall},
                {Blocks.basalt, Blocks.duneWall},
                {Blocks.stone, Blocks.stoneWall},
                {Blocks.stone, Blocks.stoneWall},
                {Blocks.moss, Blocks.sporeWall},
                {Blocks.salt, Blocks.saltWall},
                {ModBlocks.jungleFloor, ModBlocks.jungleWall},
                {ModBlocks.jungleFloor, ModBlocks.jungleWall},
                {ModBlocks.crimzesFloor, ModBlocks.crimzesWall},
                {Blocks.water, Blocks.sandWall},

        });
        Block floord3 = null, floord4 = null;
        Block walld3 = null, walld4 = null;
        if (BackgroundSettings.useStyles()) {
            if (BackgroundSettings.hasFloor3()) {
                floord3 = BackgroundSettings.floor3();
            }
            if (BackgroundSettings.hasFloor4()) {
                floord4 = BackgroundSettings.floor4();
            }
            if (BackgroundSettings.hasWall3()) {
                walld3 = BackgroundSettings.wall3();
            }
            if (BackgroundSettings.hasWall4()) {
                walld4 = BackgroundSettings.wall4();
            }
            if (BackgroundSettings.floor1() != null) selected[0] = BackgroundSettings.floor1();
            if (BackgroundSettings.wall1() != null) selected[1] = BackgroundSettings.wall1();
            if (BackgroundSettings.floor2() != null) selected2[0] = BackgroundSettings.floor2();
            if (BackgroundSettings.wall2() != null) selected2[1] = BackgroundSettings.wall2();

        }
        Block ore1 = ores.random();
        ores.remove(ore1);
        Block ore2 = ores.random();
        if (BackgroundSettings.ore().custom()) {
            if (BackgroundSettings.ore1() != null) ore1 = BackgroundSettings.ore1();
            if (BackgroundSettings.ore2() != null) ore2 = BackgroundSettings.ore2();
        }
        double tr1 = Mathf.random(0.65f, 0.85f);
        double tr2 = Mathf.random(0.65f, 0.85f);
        if (BackgroundSettings.useOreSeed()) {
            tr1 = Mathf.randomSeed(BackgroundSettings.oreSeed(), 0.65f, 0.85f);
            tr2 = Mathf.randomSeed(BackgroundSettings.oreSeed(), 0.65f, 0.85f);
        }
        boolean doheat = Mathf.chance(0.25);
        boolean tendrils = Mathf.chance(0.25);
        boolean tech = Mathf.chance(0.25);
        int secSize = 10;
        Block floord = selected[0];
        Block walld = selected[1];
        Block floord2 = selected2[0];
        Block walld2 = selected2[1];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Block floor = null;
                Block ore = Blocks.air;
                Block wallDef = walld3 == null ? Blocks.air : null;
                Block wall = wallDef;

                if (Simplex.noise2d(s1, 3, 0.5, 1 / 20.0, x, y) > 0.5) {
                    if (floord3 == null) {
                        wall = walld;
                    } else {
                        floor = floord3;
                        wall = Blocks.air;
                    }
                }

                if (Simplex.noise2d(s3, 3, 0.5, 1 / 20.0, x, y) > 0.5) {
                    boolean check = floord3 == null ? wall != wallDef : floor != null;
                    floor = floord2;
                    wall = Blocks.air;
                    if (walld4 != null) wall = walld4;
                    if (check) {
                        if (floord4 != null) {
                            floor = floord4;
                            wall = Blocks.air;
                        } else {
                            wall = walld2;
                        }
                    }
                }

                int oreSeed = BackgroundSettings.useOreSeed() ? BackgroundSettings.oreSeed() : s2;
                if (Simplex.noise2d(oreSeed, 3, 0.3, 1 / 30.0, x, y) > tr1 && BackgroundSettings.ore().enabled()) {
                    ore = ore1;
                }

                if (Simplex.noise2d(oreSeed, 2, 0.2, 1 / 15.0, x, y + 99999) > tr2 && BackgroundSettings.ore().enabled()) {
                    ore = ore2;
                }

                if (doheat && BackgroundSettings.heat().enabled() || BackgroundSettings.heat().custom()) {
                    double heat = Simplex.noise2d(BackgroundSettings.useHeatSeed() ? BackgroundSettings.heatSeed() : s3, 4, 0.6, 1 / 50.0, x, y + 9999);
                    double base = 0.65;
                    if (BackgroundSettings.heat().custom()) {
                        float offsetHeat = BackgroundSettings.heatValue() * (1f - 0.60f);
                        heat += offsetHeat;
                    }
                    if (heat > base) {
                        ore = Blocks.air;
                        wall = Blocks.air;
                        floor = Blocks.basalt;

                        if (heat > base + 0.1) {
                            floor = Blocks.hotrock;

                            if (heat > base + 0.15) {
                                floor = Blocks.magmarock;
                            }
                        }
                    }
                }
                tech &= !BackgroundSettings.tech().disabled();
                if (tech || BackgroundSettings.tech().enable()) {
                    int mx = x % secSize, my = y % secSize;
                    int sclx = x / secSize, scly = y / secSize;
                    if (Simplex.noise2d(s1, 2, 1f / 10f, 0.5f, sclx, scly) > 0.4f && (mx == 0 || my == 0 || mx == secSize - 1 || my == secSize - 1)) {
                        floor = Blocks.darkPanel3;
                        if (Mathf.dst(mx, my, secSize / 2, secSize / 2) > secSize / 2f + 1) {
                            floor = Blocks.darkPanel4;
                        }


                        if (wall != Blocks.air && Mathf.chance(0.7)) {
                            wall = Blocks.darkMetal;
                        }
                    }
                }

                tendrils &= !BackgroundSettings.tendrils().disabled();
                if (tendrils || BackgroundSettings.tendrils().enable()) {
                    if (Ridged.noise2d(1 + offset, x, y, 1f / 17f) > 0f) {
                        floor = Mathf.chance(0.2) ? Blocks.sporeMoss : Blocks.moss;

                        if (wall != Blocks.air) {
                            wall = Blocks.sporeWall;
                        }
                    }
                }
                floor = floor == null ? floord : floor;
                wall = wall == null ? walld3 : wall;
                Tile tile;
                tiles.set(x, y, (tile = new CachedTile()));
                tile.x = (short) x;
                tile.y = (short) y;
                tile.setFloor(floor.asFloor());
                tile.setBlock(wall);
                tile.setOverlay(ore);
            }
        }

        Vars.world.endMapLoad();
    }

    private void cache() {
        Arrays.fill(cacheLayers, -1);
        //draw shadows
        Draw.proj().setOrtho(0, 0, shadows.getWidth(), shadows.getHeight());
        shadows.begin(Color.clear);
        Draw.color(Color.black);

        for (Tile tile : world.tiles) {
            if (tile.block() != Blocks.air) {
                Fill.rect(tile.x + 0.5f, tile.y + 0.5f, 1, 1);
            }
        }

        Draw.color();
        shadows.end();

        Batch prev = Core.batch;
        if (batch != null) {
            batch.dispose();
        }
        Core.batch = batch = new CacheBatch(new SpriteCache(width * height * 6, false));
        /*ObjectSet<CacheLayer> used = new ObjectSet<>();
        for (int tilex = 0; tilex < world.width(); tilex++) {
            for (int tiley = 0; tiley < world.height(); tiley++) {
                Tile tile = world.rawTile(tilex, tiley);
                boolean wall = tile.block().cacheLayer != CacheLayer.normal;

                if (wall) {
                    used.add(tile.block().cacheLayer);
                }

                if (!wall || world.isAccessible(tilex, tiley)) {
                    used.add(tile.floor().cacheLayer);
                }
            }
        }*/
        for (CacheLayer layer : CacheLayer.all) {
            batch.beginCache();
            for (int tilex = 0; tilex < world.width(); tilex++) {
                for (int tiley = 0; tiley < world.height(); tiley++) {
                    Tile tile = world.tile(tilex, tiley);
                    Floor floor;

                    if (tile == null) {
                        continue;
                    } else {
                        floor = tile.floor();
                    }

                    if (tile.block().cacheLayer == layer && layer == CacheLayer.walls && !(tile.isDarkened() && tile.data >= 5)) {
                        tile.block().drawBase(tile);
                    } else if (floor.cacheLayer == layer && (world.isAccessible(tile.x, tile.y) || tile.block().cacheLayer != CacheLayer.walls || !tile.block().fillsTile)) {
                        floor.drawBase(tile);
                    } else if (floor.cacheLayer != layer && layer != CacheLayer.walls) {
                        floor.drawNonLayer(tile, layer);
                    }
                }
            }
            cacheLayers[layer.id] = batch.endCache();
        }

        batch.beginCache();

        for (Tile tile : world.tiles) {
            tile.block().drawBase(tile);
        }

        cacheWall = batch.endCache();

        Core.batch = prev;
    }

    public void render() {
        time += Time.delta;
        float scaling = Math.max(Scl.scl(4f), Math.max(Core.graphics.getWidth() / ((width - 1f) * tilesize), Core.graphics.getHeight() / ((height - 1f) * tilesize)));
        camera.position.set(width * tilesize / 2f, height * tilesize / 2f);
        camera.resize(Core.graphics.getWidth() / scaling,
                Core.graphics.getHeight() / scaling);

        mat.set(Draw.proj());
        Draw.flush();
        Draw.proj(camera);
        batch.setProjection(camera.mat);
        float prev = Time.time;
        Time.time = time;
        Draw.z(Layer.floor);
        renderer.effectBuffer.resize(Core.graphics.getWidth(), Core.graphics.getHeight());
        for (int i = 0; i < cacheLayers.length; i++) {
            if (cacheLayers[i] == -1) continue;
            CacheLayer.all[i].begin();
            batch.beginDraw();
            batch.drawCache(cacheLayers[i]);
            batch.endDraw();
            CacheLayer.all[i].end();
        }
        Time.time = prev;
        Draw.color();
        Draw.z(Layer.block - 1);
        Draw.rect(Draw.wrap(shadows.getTexture()),
                width * tilesize / 2f - 4f, height * tilesize / 2f - 4f,
                width * tilesize, -height * tilesize);
        Draw.flush();
        Draw.z(Layer.block);
        batch.beginDraw();
        batch.drawCache(cacheWall);
        batch.endDraw();
        Draw.z(Layer.flyingUnitLow);
        drawFlyers();

        Draw.z(Layer.darkness);
        Draw.proj(mat);
        Draw.color(0f, 0f, 0f, darkness);
        Fill.crect(0, 0, Core.graphics.getWidth(), Core.graphics.getHeight());
        Draw.color();
    }

    private void drawFlyers() {
        if (unitType == null) return;

        TextureRegion icon = unitType.fullIcon;

        float size = Math.max(icon.width, icon.height) * Draw.scl * 1.6f;
//        TextureRegion outline = this.flyerType.outlineRegion;
        int[] i = {0};
        BackgroundConfig.UnitMovingType movingType = BackgroundSettings.unitMovingType();
        flyers((x, y) -> {
            if (movingType.naval()){
                unitsData[i[0]].drawTrail(x, y);
            } else if (movingType.legs()) {
                unitsData[i[0]].drawLegs(x, y);
            } else if(movingType.flying()){
                Draw.color(0.0F, 0.0F, 0.0F, 0.4F);
//                Draw.rect(outline, x - 12.0F, y - 13.0F, this.flyerRot - 90.0F);
                Draw.rect(icon, x +UnitType.shadowTX, y +UnitType.shadowTY, this.flyerRot - 90.0F);
            }
            i[0]++;
        });
        this.flyers((x, y) -> {
            Draw.color(0.0F, 0.0F, 0.0F, 0.4F);
            Draw.rect("circle-shadow", x, y, size, size);
        });
        Draw.color();
        this.flyers((x, y) -> {
            float engineOffset = this.unitType.engineOffset;
            float engineSize = this.unitType.engineSize;
            float rotation = this.flyerRot;
            if (movingType.flying()) {
                Draw.color(Pal.engine);
                Fill.circle(x + Angles.trnsx(rotation + 180.0F, engineOffset), y + Angles.trnsy(rotation + 180.0F, engineOffset), engineSize + Mathf.absin(Time.time, 2.0F, engineSize / 4.0F));
                Draw.color(Color.white);
                Fill.circle(x + Angles.trnsx(rotation + 180.0F, engineOffset - 1.0F), y + Angles.trnsy(rotation + 180.0F, engineOffset - 1.0F), (engineSize + Mathf.absin(Time.time, 2.0F, engineSize / 4.0F)) / 2.0F);
            }
            Draw.color();
//            Draw.rect(outline, x, y, this.flyerRot - 90.0F);
            Draw.rect(icon, x, y, this.flyerRot - 90.0F);
//            Draw.color(Team.sharded.color);
//            Draw.rect(cellRegion, x, y, this.flyerRot - 90.0F);
        });
    }

    private void flyers(Floatc2 cons) {
        if (BackgroundSettings.units().disabled()) return;
        float tw = (float) (width * 8) * 1.0F + 8.0F;
        float th = (float) (height * 8) * 1.0F + 8.0F;
        float range = 500.0F;
        float offset = -100.0F;
        for (int i = 0; i < units; ++i) {
            Tmp.v1.trns(flyerRot, time * (unitType.speed));
            float absinX = Mathf.absin(time + Mathf.randomSeedRange((long) (i + 2), 500.0F), 10.0F, 3.4F);
            float absinY = Mathf.absin(time + Mathf.randomSeedRange((long) (i + 3), 500.0F), 10.0F, 3.4F);
            if (BackgroundSettings.unitMovingType() != BackgroundConfig.UnitMovingType.flying) {
                absinX = absinY = 0;
            }
            float x = (Mathf.randomSeedRange((long) i, range) + Tmp.v1.x + absinX + offset) % (tw + (float) Mathf.randomSeed((long) (i + 5), 0, 500));
            float y = (Mathf.randomSeedRange((long) (i + 1), range) + Tmp.v1.y + absinY + offset) % th;

            cons.get(x, y);
        }

    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public float time() {
        return time;
    }

    public float unitRot() {
        return flyerRot;
    }

    public int flyers() {
        return units;
    }

    public UnitType unitType() {
        return unitType;
    }

    public Object[] flyersData() {
        return unitsData;
    }

    public void dispose() {
        this.batch.dispose();
        this.shadows.dispose();
    }

    public void resetData() {
        for (BackgroundUnitData flyersDatum : unitsData) {
            flyersDatum.reset();
        }
    }
}
