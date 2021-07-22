package braindustry;

import arc.ApplicationCore;
import arc.Core;
import arc.struct.Seq;
import mindustry.ClientLauncher;
import mindustry.Vars;

import static ModVars.modVars.*;

public class ModListener extends ApplicationCore {
    public static Seq<Runnable> updaters=new Seq<>();
    public static void addRun(Runnable runnable){
        updaters.add(runnable);
    }
    public static void load(){
//        Log.info("\n @",ui);
        listener=new ModListener();
        if(Vars.platform instanceof ClientLauncher){
            ((ClientLauncher)Vars.platform).add(listener);
            neededInit =false;
        }else {
            Core.app.addListener(listener);
        }
    }

    @Override
    public void dispose() {
        if (!loaded)return;
        super.dispose();
    }

    @Override
    public void setup() {

    }

    @Override
    public void init() {
        if (!loaded)return;
        super.init();
    }

    public void update() {
        updaters.each(Runnable::run);
        super.update();
    }

}
