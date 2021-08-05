package braindustry.io;

import arc.math.Mathf;
import braindustry.tools.BackgroundConfig;
import gas.io.GasTypeIO;
import gas.type.Gas;
import arc.math.geom.Point2;
import arc.math.geom.Vec2;
import arc.struct.IntSeq;
import arc.util.Nullable;
import arc.util.io.Reads;
import arc.util.io.Writes;
import braindustry.annotations.ModAnnotations;
import braindustry.entities.BuilderDrawer;
import braindustry.gen.ObjectOperations;
import braindustry.gen.Stealthc;
import braindustry.gen.WritableInterface;
import mindustry.Vars;
import mindustry.content.TechTree;
import mindustry.ctype.ContentType;
import mindustry.ctype.UnlockableContent;
import mindustry.entities.EntityGroup;
import mindustry.entities.units.UnitCommand;
import mindustry.entities.units.UnitController;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Nulls;
import mindustry.gen.Unit;
import mindustry.io.TypeIO;
import mindustry.logic.LAccess;
import mindustry.type.UnitType;
import mindustry.world.Block;
import mindustry.world.blocks.ControlBlock;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.StaticWall;

@ModAnnotations.TypeIOHandler
public class ModTypeIO extends TypeIO {
    public static void writeUnitMovingType(Writes writes, BackgroundConfig.UnitMovingType viewType) {
        writes.s(viewType.ordinal());
    }
    public static BackgroundConfig.UnitMovingType readUnitMovingType(Reads read){
        return BackgroundConfig.UnitMovingType.values()[Mathf.mod(read.s(), BackgroundConfig.UnitMovingType.values().length)];
    }
    public static void writeState(Writes writes, BackgroundConfig.State viewType) {
        writes.s(viewType.ordinal());
    }
    public static BackgroundConfig.State readState(Reads read){
        return BackgroundConfig.State.values()[Mathf.mod(read.s(), BackgroundConfig.State.values().length)];
    }
    public static void writeViewType(Writes writes, BackgroundConfig.ViewType viewType) {
        writes.s(viewType.ordinal());
    }
    public static BackgroundConfig.ViewType readViewType(Reads read){
        return BackgroundConfig.ViewType.values()[Mathf.mod(read.s(), BackgroundConfig.ViewType.values().length)];
    }
    public static void writeStaticWall(Writes writes, StaticWall staticWall) {
        writes.s(staticWall==null?-1:staticWall.id);
    }
    public static StaticWall readStaticWall(Reads read){
        short s = read.s();
        Block staticWall=s==-1?null:Vars.content.block(s);
        return staticWall instanceof StaticWall? (StaticWall) staticWall :null;
    }
    public static void writeFloor(Writes writes, Floor floor) {
        writes.s(floor==null?-1:floor.id);
    }
    public static Floor readFloor(Reads read){
        short s = read.s();
        Block floor=s==-1?null:Vars.content.block(s);
        return floor instanceof Floor? (Floor) floor :null;
    }
    public static void writeBuilderDrawer(Writes writes,BuilderDrawer builderDrawer) {
        writeBuilding(writes, (Building) builderDrawer);
    }
    public static BuilderDrawer readBuilderDrawer(Reads read){
        return (BuilderDrawer) readBuilding(read);
    }
    public static void writeGas(Writes writes, Gas obj) {
        GasTypeIO.writeGas(writes,obj);
    }

    public static Gas readGas(Reads reads) {
        return GasTypeIO.readGas(reads);
    }

    public static Vec2 readVec2(Reads read) {
        return new Vec2(read.f(), read.f());
    }

    public static void writeTeam(Writes write, Team team) {
        if (team == null) {
            write.b(-1);
        } else {
            TypeIO.writeTeam(write, team);
        }
    }

    public static Team readTeam(Reads reads) {
        int id = reads.b();
        if (id == -1) return null;
        return Team.get(id);
    }

    public static void writeUnitType(Writes write, UnitType unitType) {
        if (unitType == null) {
            write.s(-1);
        } else {
            TypeIO.writeUnitType(write, unitType);
        }
    }

    public static UnitController readController(Reads read) {
        return readController(read, null);
    }

    @ModAnnotations.WritableObjectsConfig(value = {Vec2.class}, offset = 1)
    public static void writeObject(Writes write, Object object) {
        if (object != null && ObjectOperations.contains(write, object)) {
        } else if (object instanceof Vec2) {
            write.b(-1);
            TypeIO.writeVec2(write, (Vec2) object);
        } else {
            TypeIO.writeObject(write, object);
        }
    }

    @Nullable
    public static Object readObject(Reads read) {
        byte type = read.b();
        int i;
        WritableInterface writable = ObjectOperations.getById(type);
//        Log.info("type=@,obj=@", type, writable);
        if (writable != null) {
            writable.read(read);
            return writable;
        }
        switch (type) {
            case -1:
                return TypeIO.readVec2(read);
            case 0:
                return null;
            case 1:
                return read.i();
            case 2:
                return read.l();
            case 3:
                return read.f();
            case 4:
                return readString(read);
            case 5:
                return Vars.content.getByID(ContentType.all[read.b()], read.s());
            case 6:
                short length = read.s();
                IntSeq arr = new IntSeq();

                for (i = 0; i < length; ++i) {
                    arr.add(read.i());
                }

                return arr;
            case 7:
                return new Point2(read.i(), read.i());
            case 8:
                byte len = read.b();
                Point2[] out = new Point2[len];

                for (i = 0; i < len; ++i) {
                    out[i] = Point2.unpack(read.i());
                }

                return out;
            case 9:
                return TechTree.getNotNull((UnlockableContent) Vars.content.getByID(ContentType.all[read.b()], read.s()));
            case 10:
                return read.bool();
            case 11:
                return read.d();
            case 12:
                return Vars.world.build(read.i());
            case 13:
                return LAccess.all[read.s()];
            case 14:
                i = read.i();
                byte[] bytes = new byte[i];
                read.b(bytes);
                return bytes;
            case 15:
                return UnitCommand.all[read.b()];
            default:
                throw new IllegalArgumentException("Unknown object type: " + type);
        }
    }

    public static Unit readUnit(Reads read) {
        byte type = read.b();
        int id = read.i();
        if (type == 0) {
            return Nulls.unit;
        } else if (type == 2) {
            Unit unit = Groups.unit.getByID(id);
            if (unit == null) {
                EntityGroup<Unit> stealthUnits = new EntityGroup<>(Unit.class, true, true);
                Groups.all.each((entityc -> {
                    if (entityc instanceof Stealthc && ((Stealthc) entityc).inStealth()) {
                        stealthUnits.add((Unit) entityc);
                    }
                }));
                unit = stealthUnits.getByID(id);
            }
            return unit == null ? Nulls.unit : unit;
        } else if (type != 1) {
            return Nulls.unit;
        } else {
            Building tile = Vars.world.build(id);
            ControlBlock cont;
            return tile instanceof ControlBlock && (cont = (ControlBlock) tile) == (ControlBlock) tile ? cont.unit() : Nulls.unit;
        }
    }
}

