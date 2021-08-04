package braindustry.world.blocks;

import braindustry.BDVars;
import arc.Core;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.graphics.g2d.TextureRegion;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.Element;
import arc.scene.ui.layout.Table;
import arc.util.Eachable;
import arc.util.Strings;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.entities.abilities.OrbitalPlatformAbility;
import braindustry.io.ModTypeIO;
import braindustry.ui.ModStyles;
import mindustry.Vars;
import mindustry.entities.units.BuildPlan;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.Tile;
import braindustry.graphics.ModFill;
import braindustry.graphics.ModLines;

import static braindustry.BDVars.fullName;
import static mindustry.Vars.renderer;

public class TestBlock extends Block implements DebugBlock {
    private final static int doubleLength = 4, triableLength = 1;
    public final int timerAny;
    public @ModAnnotations.Load(value = "@-2-#", length = doubleLength, fallback = "@")
    TextureRegion[] doubleSize;
    public @ModAnnotations.Load(value = "@-3-#", length = triableLength, fallback = "@")
    TextureRegion[] triableSize;
    public @ModAnnotations.Load(value = "@-2-top")
    TextureRegion doubleTop;

    public TestBlock(String name) {
        super(name);
        timerAny = timers++;
        this.rotate = true;
        this.destructible = true;
        this.update = true;
        configurable = true;
        saveConfig = true;
        this.<Integer, TestBlockBuild>config(Integer.class, (build, value) -> {
            build.selectedSprite = value % build.getSizeSprites().length;
        });
        this.config(Color.class, (TestBlockBuild build, Color value) -> {
            build.selectedColor = value;
        });
        this.<TestBlockBuild>configClear(build -> {
            build.selectedSprite = 1;
        });
    }

    @Override
    public void load() {
        if (BDVars.packSprites) {
            super.load();
            return;
        }
        Core.atlas.addRegion(this.name, Core.atlas.find(fullName("testBlock")));
        for (int i = 0; i < doubleLength; i++) {
            Core.atlas.addRegion(Strings.format("@-2-@", name, i), Core.atlas.find(fullName(Strings.format("testBlock-2-@", i + 1))));
        }
        for (int i = 0; i < triableLength; i++) {
            Core.atlas.addRegion(Strings.format("@-3-@", name, i), Core.atlas.find(fullName(Strings.format("testBlock-3-@", i + 1))));
        }
        super.load();
    }

    public TextureRegion editorIcon() {
        if (this.editorIcon == null) {
            this.editorIcon = Core.atlas.find(this.name + "-icon-editor");
        }
        return editorIcon;
    }


    public void drawRequestRegion(BuildPlan req, Eachable<BuildPlan> list) {
        TextureRegion reg = this.getRequestRegion(req, list);
        float x = req.drawx();
        float y = req.drawy();
        float halfTile = 4.0F;
        Draw.rect(reg, x, y, (float) (this.size * 8), (float) (this.size * 8), 0.0F);
        if (req.config != null) {
            this.drawRequestConfig(req, list);
        }

        float rotation = (float) req.rotation * 90.0F;
        float radius = halfTile * (float) req.block.size;
        Vec2 trns = (new Vec2()).trns(rotation, -radius, radius);
        Draw.color(Pal.accent);
        Lines.stroke(2.0F);
        ModLines.swirl(trns.x + x, trns.y + y, radius, 0.25F, 180.0F + rotation + 90.0F);
        trns.rotate(180.0F);
        ModLines.swirl(trns.x + x, trns.y + y, radius, 0.25F, rotation + 90.0F);
    }

    public class TestBlockBuild extends Building implements BuildingLabel {
        public Color selectedColor = Color.white.cpy();
        private float time = 0;
        private int spriteIndex = 1;
        private int selectedSprite;
        private float someVariable = 0.5f;

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);
            TestBlockBuild me = this;
            table.table(Tex.buttonTrans, (t) -> {
                t.label(() -> "displayScale: " + renderer.getDisplayScale());
                t.label(() -> "scale: " + renderer.getScale());
//                t.button(Icon.up,ModStyles.alphai, () -> {
//                });
                t.button(Icon.pick, ModStyles.alphai, () -> {
                    BDVars.modUI.colorPicker.show(selectedColor, this::configure);
//                    ModFx.Spirals.at(x, y, size, Pal.lancerLaser);
                });
                t.button(Icon.edit, ModStyles.alphai, () -> {
                    BaseDialog dialog = new BaseDialog("") {{
                        String[] textureName = {Core.settings.getString("TestBlock.TextureName", "error")};
                        cont.table(t -> {
                            t.defaults().width(120).height(60).padLeft(4);
                            t.label(() -> "textureName: ");
                            t.field(textureName[0], name -> {
                                textureName[0] = name;
                                Core.settings.put("TestBlock.TextureName", name);
                            }).row();

                            t.add(new Element() {
                                @Override
                                public void draw() {
                                    TextureRegion region = false ? Core.atlas.find(textureName[0]) : OrbitalPlatformAbility.region;
                                    Draw.rect(region, x, y, region.width / region.height * height, height);
                                }
                            }).colspan(2).height(240).width(240);
                        });

                    }};
                    dialog.addCloseListener();
                    dialog.show();
                });
            }).row();
            table.table(Tex.buttonTrans, t -> {
                t.slider(0, 360, .001f, BDVars.settings.getFloat("angle"), (f) -> {
                    BDVars.settings.setFloat("angle", Mathf.round(f, 0.001f));
                }).row();
                t.label(() -> {
                    String angle = Mathf.round(BDVars.settings.getFloat("angle"), 1f) + "";
                    StringBuilder builder = new StringBuilder(angle);
                    while (builder.length() < 3) {
                        builder.insert(0, "0");
                    }
                    return builder.toString();
                });
            }).row();
            table.table(Tex.buttonTrans, t -> {
                TextureRegion[] sizeSprites = getSizeSprites();
                int max = sizeSprites.length;
                t.slider(1, 10, 1, someVariable, (f) -> {
                    someVariable = f;
                }).row();
                t.label(() -> {
                    return Strings.format("@", someVariable);
                }).right();
            }).row();
            table.table(Tex.buttonTrans, t -> {
                TextureRegion[] sizeSprites = getSizeSprites();
                int max = sizeSprites.length;
                t.slider(1, max, 1, selectedSprite + 1, (f) -> {
                    configure(Mathf.mod(Mathf.round(f, 1) - 1, max));
                }).row();/*
                t.label(() -> {
                    return Strings.format("@/@",selectedSprite+1,max);
                });*/
            }).row();
            table.table(Tex.buttonTrans, t -> {
                t.slider(0.1f, 20f, 0.1f, BDVars.settings.getFloat("stroke"), f -> {
                    BDVars.settings.setFloat("stroke", f);
                }).row();
                ;
                t.label(() -> {
                    return Strings.format("@", BDVars.settings.getFloat("stroke"));
                }).right();
            });
        }

        public TextureRegion[] getSizeSprites() {
            return size == 2 ? doubleSize : size == 3 ? triableSize : new TextureRegion[0];
        }

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation) {

            Building building = super.init(tile, team, shouldAdd, rotation);
            return building;
        }

        @Override
        public void playerPlaced(Object config) {
            configure(config);
        }

        public Building nearby(int rotation) {
            if (rotation == 0) {
                return Vars.world.build(this.tile.x + (int) (this.block.size / 2 + 1), this.tile.y);
            } else if (rotation == 1) {
                return Vars.world.build(this.tile.x, this.tile.y + (int) (this.block.size / 2 + 1));
            } else if (rotation == 2) {
                return Vars.world.build(this.tile.x - Mathf.ceil(this.block.size / 2f), this.tile.y);
            } else {
                return rotation == 3 ? Vars.world.build(this.tile.x, this.tile.y - Mathf.ceil(this.block.size / 2f)) : null;
            }
        }

        public Tile nearbyTile(int rotation) {
            if (rotation == 0) {
                return Vars.world.tile(this.tile.x + (int) (this.block.size / 2 + 1), this.tile.y);
            } else if (rotation == 1) {
                return Vars.world.tile(this.tile.x, this.tile.y + (int) (this.block.size / 2 + 1));
            } else if (rotation == 2) {
                return Vars.world.tile(this.tile.x - Mathf.ceil(this.block.size / 2f), this.tile.y);
            } else {
                return rotation == 3 ? Vars.world.tile(this.tile.x, this.tile.y - Mathf.ceil(this.block.size / 2f)) : null;
            }
        }


        public Building front() {
            return this.nearby(this.rotation);
        }

        @Override
        public void updateTile() {
            super.updateTile();
            time += this.delta() / 60f;
        }

        public void draw() {
            float settingsRot = BDVars.settings.getFloat("angle");
            float settingsStroke = BDVars.settings.getFloat("stroke", 1f);
            TextureRegion region = getRegion();
            Draw.rect(region, x, y, size * 8, size * 8, 0.0F);
            float epsilon = 2f - (renderer.getDisplayScale() - renderer.minScale()) / (renderer.maxScale() - renderer.minScale());
            Draw.color(Color.red);
            Draw.rect(region, x, y, size * 8 * epsilon, size * 8 * epsilon, 0.0F);
//            Draw.rect(editorIcon(), x, y + size * 8, size * 8, size * 8, 0f);
//            Draw.alpha(0.5f);

            Draw.reset();
            Lines.stroke(settingsStroke);
//            ModLines.crystal(x, y,8f, (size) * 8f, rotdeg(),(int) someVariable);
//            ModFill.spikesSwirl(x, y, (size) * 8, 8, modVars.settings.getFloat("angle") / 360f, rotdeg(), someVariable);
            Vars.renderer.lights.add(() -> {
                Draw.color(selectedColor.toFloatBits());
                ModFill.crystal(x, y, 8f, (size) * 8f, rotdeg(), (int) someVariable);
                ModFill.doubleSwirl(x, y, (size) * 8f, 8f * (size + 1f), BDVars.settings.getFloat("angle") / 360f, rotdeg());
            });
//            ModLines.crystal(x, y,8f, (size) * 8f, rotdeg(),(int) someVariable);
//            ModFill.spikesSwirl(x, y, (size) * 8, 8, modVars.settings.getFloat("angle") / 360f, rotdeg(), someVariable);
            Draw.reset();
            Building front = front();
//            if (true)return;
            if (front != null && front.block != null) {
                float size = front.block.size * 8f;
                Draw.z(Layer.blockBuilding + 1f);
                Draw.color(Color.green);
                Draw.alpha(0.3f);
                float offset = Mathf.ceil(size / 2f);
                ModLines.rect(front.x - offset, front.y - offset, size, size, settingsRot);
            }
            Draw.reset();
        }

        protected TextureRegion getRegion() {
            TextureRegion[] sizeSprites = getSizeSprites();
            TextureRegion region = this.block.region;
            if (sizeSprites.length != 0) {
                region = sizeSprites[selectedSprite];
            }
            return region;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(selectedSprite);
            ModTypeIO.writeColor(write, selectedColor);
            write.f(someVariable);
        }

        @Override
        public byte version() {
            return 2;
        }

        @Override
        public void read(Reads read, byte revision) {
            if (revision >= 1) {
                selectedSprite = read.i() % getSizeSprites().length;
            }
            if (revision >= 2) {
                selectedColor = ModTypeIO.readColor(read);
                someVariable = read.f();
            }
        }
    }
}
