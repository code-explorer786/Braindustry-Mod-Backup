package braindustry.world.blocks.sandbox;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import braindustry.*;
import braindustry.content.*;
import braindustry.gen.*;
import braindustry.graphics.*;
import braindustry.io.*;
import braindustry.world.*;
import braindustry.world.AlwaysDrawBlock.*;
import mindustry.*;
import mindustry.annotations.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.input.*;
import mindustry.io.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.Vars.world;

public class UnitSpawner extends ModBlock{
    public @Annotations.Load(value = "@-color", fallback = "air")
    TextureRegion colorRegion;
    public UnitSpawnerBuild currentBuilding;
    public Color targetColor = Color.gray;
    public boolean choose = false;

    public UnitSpawner(String name){
        super(name);
        this.update = true;
        sync = true;
        this.configurable = true;
        Events.on(EventType.TapEvent.class, (e) -> {
            if(e.player == Vars.player && choose && currentBuilding != null){
                currentBuilding.tapAt(e.tile);
//                currentBuilding=null;
            }
        });
        config(Vec2.class, (b, vec) -> {
            UnitSpawnerBuild build = b.as();
            build.spawnPos.set(vec);
        });
        config(UnitEntry.class, (b, entry) -> {
            ((UnitSpawnerBuild)b).addEntry(entry);
        });
        AlwaysDrawBlock.register();
    }

    public static boolean local(){
        return !Vars.net.active();
    }

    @Override
    public void init(){
        super.init();
    }

    public class UnitSpawnerBuild extends ModBuilding implements AlwaysDrawBuild{
        public Vec2 spawnPos;
        public Team defaultUnitTeam;
        public Seq<UnitEntry> unitEntries = new Seq<>();
        Runnable rebuild;

        @Override
        public Building init(Tile tile, Team team, boolean shouldAdd, int rotation){
            UnitSpawnerBuild build = (UnitSpawnerBuild)super.init(tile, Team.derelict, shouldAdd, rotation);
            build.defaultUnitTeam = team;
            build.spawnPos = new Vec2(tile.worldx(), tile.worldy());
//            build.
            return build;
        }

        @Override
        public void created(){
            super.created();
        }


        public void remove(UnitEntry unitEntry){
            unitEntries.remove(unitEntry);
            unitEntry.remove();
        }

        protected void addEntry(UnitEntry unitEntry){
            if(!unitEntries.contains(unitEntry)){
                unitEntries.add(unitEntry);
            }
            if(added) unitEntry.add();
        }

        public void add(UnitEntry unitEntry){
            configure(unitEntry);
        }

        public void spawnUnits(){
            unitEntries.each(UnitEntry::spawn);
//            unitEntries.clear();
        }

        public void tapAt(Tile tile){
            currentBuilding = null;
            choose = false;
            spawnPos = new Vec2(tile.worldx(), tile.worldy());
            configure(spawnPos);
//            ModFx.blockSelect.at(tile.worldx(), tile.worldy(), 1, Color.lime, Color.white);
            if(Vars.control.input instanceof DesktopInput){
                ((DesktopInput)Vars.control.input).panning = false;
            }
        }

        public void openCoordsDialog(){
            if(currentBuilding != null && currentBuilding.isValid()) return;
            if(Vars.control.input instanceof DesktopInput){
                ((DesktopInput)Vars.control.input).panning = true;
            }
            Vars.ui.announce("click on need tile");
            currentBuilding = this;
            choose = true;
            Core.camera.position.set(getPos());
//            Core.camera.position.set(this.x,this.y);
        }

        @Override
        public void buildConfiguration(Table table){
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, this::spawnUnits).size(40).get().getStyle().imageUp = Icon.commandAttack;
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, () -> {
                BDVars.modUI.unitSpawnerDialog.show(this);
            }).size(40).fill().grow().get().getStyle().imageUp = Icon.play;
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, this::openCoordsDialog).size(40).fill().grow().get().getStyle().imageUp = Icon.grid;
            table.button(Tex.whiteui, Styles.clearTransi, 24.0F, this::openEditDialog).size(40).fill().grow().get().getStyle().imageUp = Icon.wrench;
        }

        public void showOffsetInput(String titleText, Cons2<String, String> confirmed){
            new Dialog(titleText){
                {
                    TextField fieldX = addInput("x: ", getPos().x / Vars.tilesize, true);
                    TextField fieldY = addInput("y: ", getPos().y / Vars.tilesize, false);
                    buttons.defaults().size(120, 54).pad(4);
                    buttons.button("@cancel", this::hide);
                    buttons.button("@ok", () -> {
                        confirmed.get(fieldX.getText(), fieldY.getText());
                        hide();
                    }).disabled(b -> fieldX.getText().isEmpty() || fieldY.getText().isEmpty());
                    keyDown(KeyCode.enter, () -> {
                        String textX = fieldX.getText();
                        String textY = fieldX.getText();
                        if(!textX.isEmpty() && !textY.isEmpty()){
                            confirmed.get(textX, textY);
                            hide();
                        }
                    });
                    keyDown(KeyCode.escape, this::hide);
                    keyDown(KeyCode.back, this::hide);
                    show();
                    Core.scene.setKeyboardFocus(fieldX);
                    fieldX.setCursorPosition((spawnPos.x + "").length());
                }

                protected TextField addInput(String dtext, float def, boolean x){

                    cont.margin(30).add(dtext).padRight(6f);
                    TextField.TextFieldFilter filter = TextField.TextFieldFilter.floatsOnly;
                    TextField field = cont.field(def + "", t -> {
                    }).height(50f).get();
                    field.setFilter((f, c) -> {

                        String text = f.getText();
                        if(text.isEmpty()) return true;
                        StringBuilder b = new StringBuilder(text);
                        String selection = f.getSelection();
//                        text.substring(0,f.getSelectionStart());
                        b.insert(f.getCursorPosition(), c);
                        if(!selection.isEmpty()){
                            text = b.toString();
                        }else{
                            text = b.toString();
                        }
                        float num = Strings.parseFloat("0" + text, -1f);
                        return num >= 0f && num <= (x ? world.width() : world.height());
                    });
                    return field;
                }
            };

        }

        protected void openEditDialog(){
            BaseDialog dialog = new BaseDialog("@unit-spawner.edit");
            dialog.cont.table((t) -> {
                t.defaults().size(280.0F, 60.0F);
                t.button("@unit-spawner.edit-offset", () -> {
                    showOffsetInput("@unit-spawner.edit-offset", (sx, sy) -> {
                        float x = Strings.parseFloat(sx, -1f);
                        float y = Strings.parseFloat(sy, -1f);
                        if(x >= 0f && x <= world.width() && y >= 0f && y <= world.height()){
                            configure(new Vec2(x, y).scl(Vars.tilesize));
                        }
                    });
                }).growX().row();
            });
            dialog.addCloseListener();
            dialog.addCloseButton();
            dialog.show();
        }

        public boolean selected(){
            return Vars.control.input.frag.config.getSelectedTile() == this;
        }

        @Override
        public void drawTeam(){
        }

        @Override
        public void drawTeamTop(){
        }

        @Override
        public void draw(){
            Draw.rect(this.block.region, this.x, this.y, this.block.rotate ? this.rotdeg() : 0.0F);
            Draw.draw(Draw.z(), () -> {
                Vec2 vec = Core.input.mouseWorld(Vars.control.input.getMouseX(), Vars.control.input.getMouseY());
                Building build = world.buildWorld(vec.x, vec.y);
                if(build == this){
                    drawCursor();
                }else{
                    Draw.color(Color.gray);
                    Draw.rect(colorRegion, this.x, this.y);
                }
            });
        }

        public void drawCursor(){
            ModShaders.rainbow.set(id);
            Draw.rect(colorRegion, this.x, this.y);
            Draw.shader();
        }

        @Override
        public void damage(float damage){
            ModFx.shieldWave.at(this.x, this.y, this.block.size + 1, Pal.shield, this.block.size * 8f);
        }

        public void damage(float amount, boolean withEffect){
            this.damage(amount);
        }

        public boolean collision(Bullet other){
            this.damage(other.damage() * other.type().buildingDamageMultiplier);
            return true;
        }

        @Override
        public void damageContinuousPierce(float amount){
            super.damageContinuousPierce(amount);
        }


        @Override
        public void add(){
            if(!this.added){
                Groups.all.add(this);
                Groups.build.add(this);
                this.added = true;
                unitEntries.each(UnitEntry::add);
            }
        }

        @Override
        public void remove(){
            if(this.added){
                Groups.all.remove(this);
                Groups.build.remove(this);
                if(this.sound != null){
                    this.sound.stop();
                }

                unitEntries.each(UnitEntry::remove);
                this.added = false;
            }
        }

        @Override
        public void updateTile(){
            super.updateTile();
            this.team = Team.derelict;
            Seq<UnitEntry> select = unitEntries.select(u -> !u.isAdded());
            for(UnitEntry unitEntry : select){
                remove(unitEntry);
            }
        }

        @Override
        public void display(Table table){
            table.table((t) -> {
                t.left();
                t.add(new Image(this.block.getDisplayIcon(this.tile))).size(32.0F);
                t.labelWrap(this.block.getDisplayName(this.tile)).left().width(190.0F).padLeft(5.0F);
            }).growX().left();
            table.row();

            table.table((bars) -> {
                bars.defaults().growX().height(18.0F).pad(4.0F);
                this.displayBars(bars);
            }).growX();
            table.row();
            table.table(this::displayConsumption).growX();
            boolean displayFlow = (this.block.category == Category.distribution || this.block.category == Category.liquid) && Core.settings.getBool("flow") && this.block.displayFlow;
            if(displayFlow){
                String ps = " " + StatUnit.perSecond.localized();
                if(this.items != null){
                    table.row();
                    table.left();
                    table.table((l) -> {
                        Bits current = new Bits();
                        Runnable rebuild = () -> {
                            l.clearChildren();
                            l.left();

                            for(Item item : Vars.content.items()){
                                if(this.items.hasFlowItem(item)){
                                    l.image(item.uiIcon).padRight(3.0F);
                                    l.label(() -> {
                                        return this.items.getFlowRate(item) < 0.0F ? "..." : Strings.fixed(this.items.getFlowRate(item), 1) + ps;
                                    }).color(Color.lightGray);
                                    l.row();
                                }
                            }

                        };
                        rebuild.run();
                        l.update(() -> {

                            for(Item item : Vars.content.items()){
                                if(this.items.hasFlowItem(item) && !current.get(item.id)){
                                    current.set(item.id);
                                    rebuild.run();
                                }
                            }

                        });
                    }).left();
                }

                if(this.liquids != null){
                    table.row();
                    table.table((l) -> {
                        boolean[] had = new boolean[]{false};
                        Runnable rebuild = () -> {
                            l.clearChildren();
                            l.left();
                            l.image(() -> {
                                return this.liquids.current().uiIcon;
                            }).padRight(3.0F);
                            l.label(() -> {
                                return this.liquids.getFlowRate() < 0.0F ? "..." : Strings.fixed(this.liquids.getFlowRate(), 2) + ps;
                            }).color(Color.lightGray);
                        };
                        l.update(() -> {
                            if(!had[0] && this.liquids.hadFlow()){
                                had[0] = true;
                                rebuild.run();
                            }

                        });
                    }).left();
                }
            }

            if(Vars.net.active() && this.lastAccessed != null){
                table.row();
                table.add(Core.bundle.format("lastaccessed", this.lastAccessed)).growX().wrap().left();
            }

            table.marginBottom(-5.0F);

        }

        @Override
        public boolean interactable(Team team){
            return true;
        }

        @Override
        public void write(Writes write){
            super.write(write);
            TypeIO.writeVec2(write, spawnPos);
            TypeIO.writeTeam(write, defaultUnitTeam);
            write.i(unitEntries.size);
            unitEntries.each(entry -> {
                entry.write(write);
            });
        }

        @Override
        public byte version(){
            return 1;
        }

        @Override
        public void read(Reads read, byte revision){
            super.read(read, revision);
            spawnPos = TypeIO.readVec2(read);
            defaultUnitTeam = TypeIO.readTeam(read);
            float amount = read.i();
//            Log.info("unitEntries.size(A): @",unitEntries==null?null:unitEntries.size);
            if(unitEntries != null){
                unitEntries.each(UnitEntry::remove);
                unitEntries.clear();
            }
            for(int i = 0; i < amount; i++){
//                UnitEntry entry = new UnitEntry();
//                entry.read(read, revision);
                if(revision == 0){
                    UnitType unitType = BdTypeIO.readUnitType(read);
//                    if (unitType==null)throw new IllegalArgumentException("UnitType cannot be null");
                    int count = read.i();
                    Vec2 pos = TypeIO.readVec2(read);
                    Team team = TypeIO.readTeam(read);
                    addEntry(UnitEntry.create(unitType, team, count, pos));
                }else{
                    addEntry(UnitEntry.readEntry(read, revision));
                }
//                add();
            }
//            Log.info("unitEntries.size(B): @",unitEntries.size);
        }

        public Vec2 getPos(){
            return new Vec2().set(spawnPos.cpy());
        }

        @Override
        public void alwaysDraw(){
            Draw.draw(Layer.blockBuilding + 5f, () -> {
                Draw.mixcol();
//                ModShaders.rainbow.set(this);
                Draw.color(targetColor);
                Draw.rect(colorRegion, getPos().x, getPos().y);
                Draw.shader();
                Draw.reset();
//                unitEntries.each(UnitEntry::draw);
            });
        }
    }
}
