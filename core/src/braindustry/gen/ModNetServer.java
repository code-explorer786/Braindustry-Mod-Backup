package braindustry.gen;

import arc.util.Structs;
import braindustry.BDVars;

import arc.ApplicationListener;
import arc.Events;
import arc.func.Cons;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.CommandHandler;
import arc.util.Log;
import braindustry.core.CheatLevel;
import mindustry.annotations.Annotations;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Groups;
import mindustry.gen.Player;
import mindustry.gen.Unit;
import mindustry.type.UnitType;

import static mindustry.Vars.net;

public class ModNetServer implements ApplicationListener {

    private static boolean cheating(Player player) {
        if (!net.active()) return BDVars.settings.cheating();
        return BDVars.settings.cheatLevel().cheating(player);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void setTeam(Player player, Team team) {
        if (!cheating(player)) return;
        player.team(team);
    }

    private static void stealthAction(Cons<Unit> cons, boolean inStealth) {
        Groups.all.each(entityc -> {
            if (entityc instanceof Stealthc && (!inStealth || ((Stealthc) entityc).inStealth())) {
                cons.get((Unit) entityc);
            }
        });
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void damageAllUnits(Player player) {
        Groups.unit.each(unit -> unit.damage(unit.health - 1));
        stealthAction((unit) -> {
            unit.damage(unit.health - 1);
        }, true);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void killAllUnits(Player player) {
        Groups.unit.each(unit -> unit.kill());
        stealthAction((unit) -> {
            unit.kill();
        }, true);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void healAllUnits(Player player) {
        Groups.unit.each(unit -> unit.heal());
        stealthAction((unit) -> {
            unit.heal();
        }, true);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void tpAllUnits(Player player, Vec2 pos) {
        Groups.unit.each(unit -> unit.set(pos));
        stealthAction((unit) -> {
            unit.set(pos);
        }, true);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void setUnit(Player player, Unit unit) {
        if (!cheating(player)) return;
        player.unit(unit);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void setNewUnit(Player player, UnitType type) {
        if (!cheating(player)) return;
        Unit unit = type.spawn(player.team(), player.x, player.y);
        unit.spawnedByCore = true;
        player.unit().spawnedByCore = true;
        player.unit(unit);
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void spawnUnits(Player player, UnitType type, float x, float y, int amount, boolean spawnerByCore, Team team) {
        for (int i = 0; i < amount; i++) {
            Unit unit = type.spawn(team == null ? Team.derelict : team, x, y);
            unit.spawnedByCore(spawnerByCore);

        }
    }

    @Annotations.Remote(targets = Annotations.Loc.client, called = Annotations.Loc.server)
    public static void setStealthStatus(Player player, Unit unit, boolean inStealth, float value) {
        if (unit instanceof Stealthc) {
            Stealthc stealthUnit = (Stealthc) unit;
            if (inStealth) {
                if (value == -1) {
                    stealthUnit.setStealth();
                } else {
                    stealthUnit.setStealth(value);
                }
            } else {
                if (value == -1) {
                    stealthUnit.removeStealth();
                } else {
                    stealthUnit.removeStealth(value);
                }
            }
        }
    }

    @Annotations.Remote(targets = Annotations.Loc.both, called = Annotations.Loc.both, forward = true)
    public static void checkStealthStatus(Player player, Unit unit, boolean inStealth) {
        if (unit instanceof Stealthc) {
            if (inStealth) {
                while (Groups.unit.contains(u -> u == unit)) {
                    Groups.unit.remove(unit);
                }
            } else if (!Groups.unit.contains(u -> u == unit)) {
                Groups.unit.add(unit);
            }
            if (((Stealthc) unit).inStealth() != inStealth) ((Stealthc) unit).inStealth(inStealth);
//            stealthUnit.inStealth(inStealth);
        }
    }

    public void registerCommands(CommandHandler handler) {
        Events.on(EventType.PlayerConnect.class, e -> {
            BDCall.setServerCheatLevel(BDVars.settings.cheatLevel().ordinal());
        });
        handler.register("cheats", "[value]", "Set cheat level or return it.", (args) -> {
            if (args.length == 0) {
                Log.info("Cheat level: @", BDVars.settings.cheatLevel());
                return;
            }
            String value = args[0];
            ;
            if (!Structs.contains(CheatLevel.values(),c->c.name().equals(value))) {
                Log.info("Cheat levels: @", Seq.with(CheatLevel.values()).toString(", "));
                return;
            }
            BDVars.settings.cheatLevel(CheatLevel.valueOf(value));
            Log.info("Cheat level updated to @", BDVars.settings.cheatLevel());
            BDCall.setServerCheatLevel(BDVars.settings.cheatLevel().ordinal());
        });
    }
}
