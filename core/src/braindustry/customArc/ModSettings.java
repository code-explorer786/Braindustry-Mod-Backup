package braindustry.customArc;

import arc.assets.Loadable;
import braindustry.BDVars;

import arc.Core;
import arc.Events;
import arc.math.Mathf;
import arc.util.Strings;
import braindustry.BraindustryMod;
import braindustry.core.CheatLevel;
import braindustry.ui.ModStyles;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Styles;

import javax.swing.text.Style;

import static braindustry.BDVars.*;

public class ModSettings {
    public ModSettings(){
        addEvent();
    }
    private String full(String name){
        return fullName(name);
    }
    private void put(String name,Object value){
        if (Core.settings.has(full(name))){
            Core.settings.remove(full(name));
        }
        Core.settings.put(full(name),value);
    };
    private Object get(String name,Object def){
        return Core.settings.get(full(name),def);
    }
    public boolean getBool(String name, boolean def){
        return (boolean)get(name,def);
    }
    public boolean getBool(String name){
        return getBool(name,false);
    }
    public void setBool(String name, boolean def){
        put(name,def);
    }
    public float getFloat(String name, float def){
        return (float)get(name,def);
    }
    public float getFloat(String name){
        return getFloat(name,0.0F);
    }
    public void setFloat(String name, float def){
        put(name,def);
    }
    public int getInt(String name,int def) {
        Object o = get(name, def);
        if (o instanceof Boolean){
            put(name,o==(Object) true?0:1);
            return getInt(name,def);
        }
        return (int) o;
    }
    public int getInt(String name) {
        return getInt(name,0);
    }
    public void setInt(String name,int value){
        put(name,value);
    }
    public boolean cheating(){
        return getBool("cheat",false);
    }
    public void cheating(boolean cheating){
        put("cheat",cheating);
    }
    public boolean debug(){
        return getBool("debug",false);
    }
    public void debug(boolean debug){
        put("debug",debug);
    }
    public CheatLevel cheatLevel(){
        CheatLevel[] values = CheatLevel.values();
        return values[Mathf.mod(getInt("cheatLevel"), values.length)];
    }
    public void cheatLevel(CheatLevel value){
        setInt("cheatLevel",value.ordinal());
    }
    private void addEvent() {


        Events.on(EventType.ClientLoadEvent.class,(e)->{
//            modLog("settings.ClientLoadEvent start");
            Vars.ui.settings.row();
            Vars.ui.settings.button((button) -> {
                        button.image(BraindustryMod.getIcon()).size(64, 64);
//                        button.setStyle(Styles.);
//                        button.setSize(80f,80f);
                        button.label(() -> Strings.format("@", modInfo==null?"":modInfo.meta.displayName));
                    },
                    () -> {
                        BDVars.otherSettingsDialog.show();
                    }).height(84).right().row();
//            modLog("settings.ClientLoadEvent end");

        });
    }


    public void cheatLevelServer( CheatLevel level) {
        setInt("server-var-cheatLevel",level.ordinal());
    }

    public CheatLevel cheatLevelServer() {
        CheatLevel[] values = CheatLevel.values();
        return values[Mathf.mod(getInt("server-var-cheatLevel"), values.length)];
    }
}
