package braindustry.audio;

import arc.audio.Music;
import braindustry.gen.BDMusics;
import mindustry.Vars;

public class ModAudio {

    public static void reload() {
        Vars.control.sound.ambientMusic.add(BDMusics.darknessHarmony, BDMusics.stellarSonata);
        Vars.control.sound.bossMusic.add(BDMusics.frozenIslands);
    }

}
