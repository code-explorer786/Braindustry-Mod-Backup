package braindustry.audio;

import arc.audio.Music;
import braindustry.gen.ModMusics;
import mindustry.Vars;

public class ModAudio {

    public static void reload() {
        Vars.control.sound.ambientMusic.add(ModMusics.darknessHarmony, ModMusics.stellarSonata);
        Vars.control.sound.bossMusic.add(ModMusics.frozenIslands);
    }

}
