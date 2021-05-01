package braindustry.gen;

import arc.util.Log;

import arc.func.Prov;
import arc.struct.ObjectMap;
import arc.util.io.Writes;

public class ObjectOperations {
  public static ObjectMap<Integer, Prov> idMap = new ObjectMap<>();

  public static ObjectMap<Class, Integer> nameMap = new ObjectMap<>();

  public static ObjectMap<Class, Prov> classMap = new ObjectMap<>();

  static {
    mapClass(-2,braindustry.gen.UnitEntry.class,braindustry.gen.UnitEntry::new);
    mapClass(-3,braindustry.world.blocks.sandbox.DpsMeter.MeterContainer.class,braindustry.world.blocks.sandbox.DpsMeter.MeterContainer::new);
  }

  public static Prov map(int id) {
    return idMap.get(id);
  }

  public static Prov map(Class c) {
    return classMap.get(c);
  }

  public static void mapClass(int id, Class aClass, Prov prov) {
    idMap.put(id, prov);
    nameMap.put(aClass, id);
    classMap.put(aClass, prov);
  }

  public static WritableInterface getById(int id) {
    try {
      Prov p=map(id);
      return (braindustry.gen.WritableInterface)p.get();
    } catch (Exception e){return null;
    }
  }

  public static boolean contains(Writes write, Object obj) {
    try {
      Class aClass=obj.getClass();
      int id=nameMap.get(aClass,Integer.MIN_VALUE);
      braindustry.gen.WritableInterface w=(braindustry.gen.WritableInterface)obj;
      write.b(id);
      w.write(write);
      return true;
    }
    catch (IllegalArgumentException e){throw e;} catch (Exception e) {
    }
    return false;
  }
}
