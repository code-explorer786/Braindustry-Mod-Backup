package braindustry.ui.dialogs;

import arc.func.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import braindustry.gen.*;
import braindustry.world.blocks.sandbox.UnitSpawner.*;
import mindustry.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.ui.dialogs.*;

public class UnitSpawnerDialog extends BaseDialog{
    UnitSpawnerBuild build;
    private UnitEntryDialog entryDialog = new UnitEntryDialog();

    public UnitSpawnerDialog(){
        super("@dialog.unit-block-units.name");
        cont.pane(p->{
            shown(()->rebuild(p));
        });
        hidden(() -> build = null);
        addCloseButton();
    }

    private static int step(int amount){
        if(amount < 10){
            return 1;
        }else if(amount < 100){
            return 10;
        }else{
            return amount / 10;
        }
    }

    public void show(UnitSpawnerBuild build){
        this.build = build;
        show();
    }

    public void text(Table cont, String text){
        cont.table(t -> {
            t.top().margin(6);
            t.add(text).growX().color(Pal.accent);
            t.row();
            t.image().fillX().height(3).pad(4).color(Pal.accent);
        }).fillX().center().row();
    }

    private void rebuild(Table table){
        table.clearChildren();
        ;

        this.text(table, "@text.choose-units");

        table.table(t -> {
            int[] num = {0};
            Seq<UnitType> units = Vars.content.units();

            units.each(u -> {
                if(u.constructor.get() instanceof BlockUnitc) return;
                t.button(b -> {
                    b.left();
                    b.image(u.uiIcon).size(40).padRight(2);
                    b.add(u.localizedName);
                },
                () -> {
                    Team team = Vars.player.team();
                    ;
                    UnitEntry lastEntry = build.unitEntries.find(entry -> {
                        Intf<Float> conv = (c) -> (int)(c / Vars.tilesize);
                        int nx = conv.get(build.spawnPos.x), ny = conv.get(build.spawnPos.y);
                        int ex = conv.get(entry.posX()), ey = conv.get(entry.posY());
                        return entry.unitType() == u && entry.team == team && ex == nx && ey == ny;
                    });
                    if(lastEntry == null){
                        build.add(UnitEntry.create(u, team, 1, build.spawnPos));
                    }else{
                        lastEntry.amount++;
                        build.add(lastEntry);
                    }
//                                    this.addUnit([u.id, this.getTeam(), this.getMultiplier(), this.getSpawnPos()]);
                }
                ).width(188.0f).margin(12).fillX();

                if(++num[0] % 4 == 0) t.row();
            }
            );
        }).width(800).top().center().row();

        this.text(table, "@text.actions-with-all-units");

        table.table(t -> {
            t.button("@button.kill-all-units", BDCall::killAllUnits).growX().height(54).pad(4);

            t.button("@button.heal-all-units", BDCall::healAllUnits).growX().height(54).pad(4).row();

            t.button("@button.tp-all-units", () -> {
                BDCall.tpAllUnits(build.spawnPos);
            }).growX().height(54).pad(4);

            t.button("@button.damage-all-units", BDCall::damageAllUnits).growX().height(54).pad(4).row();
        }).width(300 * 2f).row();


        this.text(table, "@text.spawn");

        table.table(t -> {
            t.button("@button.show-all-units", () -> {
                entryDialog.show();
            }).growX().height(54).pad(4);
            t.button("@button.spawn-units", () -> {
                build.spawnUnits();
            }).growX().height(54).pad(4).row();

            if(Vars.spawner.getSpawns().size > 0){
                t.button("@button.spawn-units-on-spawners", () -> {
//                        this.createUnitsOnSpawners();
                }).growX().height(54).pad(4);

                t.button("@button.spawn-units-on-random-spawner", () -> {
//                        this.createUnitsOnRandomSpawner();
                }).growX().height(54).pad(4).row();
            }
        }).width(300 * 2f).row();

//        addCloseButton();
//        dialog.show();
    }/*
public void showUnitEntryDialog(UnitSpawnerBuild build){
        this.build=build;
        entryDialog.show();
}*/
    private class UnitEntryDialog extends BaseDialog{
        UnitEntryDialog(){
            super("@dialog.all-unit-entry");
            cont.pane(p->{
                shown(()->rebuild(p));
            });
            addCloseButton();
            hidden(() -> {
                if(!UnitSpawnerDialog.this.isShown()){
//                    build = null;
                }
            });
        }

        public void rebuild(Table p){

            p.clearChildren();
            float bsize = 40.0F;
            int countc = 0;
            for(UnitEntry unitEntry : build.unitEntries){
                p.table(Tex.pane, (t) -> {
                    t.margin(4.0F).marginRight(0.0F).left();
                    t.image(unitEntry.unitType().uiIcon).size(24.0F).padRight(4.0F).padLeft(4.0F);
                    t.label(() -> {
                        return unitEntry.amount + "";
                    }).left().width(90.0F);
                    t.button(Tex.whiteui, Styles.clearTransi, 24.0F, () -> {
                        build.remove(unitEntry);
                        rebuild(p);

                    }).size(bsize).get().getStyle().imageUp = Icon.trash;
                    t.button("-", Styles.cleart, () -> {
                        unitEntry.amount = Math.max(unitEntry.amount - step(unitEntry.amount), 0);
                    }).size(bsize);
                    t.button("+", Styles.cleart, () -> {
                        unitEntry.amount += step(unitEntry.amount);
                    }).size(bsize);
                    t.button(Icon.pencil, Styles.cleari, () -> {
                        Vars.ui.showTextInput("@configure", unitEntry.unitType().localizedName, 10, unitEntry.amount + "", true, (str) -> {
                            if(Strings.canParsePositiveInt(str)){
                                int amount = Strings.parseInt(str);
                                if(amount >= 0){
                                    unitEntry.amount = amount;
                                }
                            }
                        });
                    }).size(bsize);
                });
                countc++;
                if(countc % 3 == 0) p.row();
            }
        }
    }

}
