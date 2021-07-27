package braindustry.gen;

import ModVars.modFunc;
import arc.struct.ObjectMap;
import mindustry.gen.EntityMapping;

import java.lang.reflect.Constructor;

public class ModEntityMapping {
    private static final int offset = 60;
    private static final ObjectMap<Class<?>, Integer> classIdMap = new ObjectMap<>();
    private static int lastClass;
    private static boolean inited = false;

    static {
        init();
    }

    public static void init() {
        if (inited) return;
        inited = true;
        lastClass = 0;
        mapClass(null);
        mapClass(null);//old Stealth Units
        mapClass(null);//Old Clouds
        ModEntityMappingGenerated.init();
        mapClass(null);
    }

    public static int getId(Class<?> name) {
        int id = classIdMap.get(name, offset);
//        Log.info("getId(class: @,id: @)",name.getName(),id);
        return id;
    }

    //
    public static <T> void mapClasses(Class<?>... objClasses) {
        for (Class<?> objClass : objClasses) {
            mapClass(objClass);
        }
    }

    public static <T> void mapClass(Class<?> objClass) {
        try {
            final int id = lastClass + offset;
            lastClass++;
            if (objClass == null) return;
            Constructor<?> cons = objClass.getDeclaredConstructor();
//            objClass.getField("classId").setInt(objClass, id);
//            Log.info("@ @",lastClass,objClass.getName());
            classIdMap.put(objClass, id);
            EntityMapping.idMap[id] = () -> {
                try {
                    return cons.newInstance();
                } catch (Exception var3) {
                    throw new RuntimeException(var3);
                }
            };
            EntityMapping.nameMap.put(objClass.getName(), () -> EntityMapping.idMap[getId(objClass)]);
//            Log.info("class: @, id: @, getId: @",objClass.getName(),id,getId(objClass));
        } catch (Exception e) {
            modFunc.showException(e);
        }
    }
}
