package braindustry.content;

import arc.Core;
import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import arc.util.Log;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.ctype.ContentList;
import mindustry.gen.Unit;
import mindustry.type.StatusEffect;

import static mindustry.Vars.renderer;

public class ModStatusEffects implements ContentList {
    public static Seq<StatusEffect> speedMul, darkBurning;

    private static Seq<StatusEffect> StatusEffectSeq(int amount, float value, float startValue, String name) {
        Seq<StatusEffect> seq = new Seq<>();

        for (int i = 0; i < amount; i++) {
            float val = startValue + value * (i + 1.0f);
            String n = name + val;

            try {
                StatusEffect e = new StatusEffect(n) {{
                    localizedName = Strings.capitalize(n);
                    speedMultiplier = name.equals("speedMul") ? val : 1.0f;
                    damageMultiplier = name.equals("damageMul") ? val : 1.0f;
//                        this.armorMultiplier = name == "armorMul" ? val : 1.0f;
                    damage = name.equals("damage") ? val : 0.0f;
                    show=false;
                }};
                seq.add(e);
            } catch (Exception e) {
                Log.err(e);
                seq.add(new StatusEffect(n) {{
                    localizedName = Strings.capitalize(n) + " error";
                }});
            }
        }
        return seq;
    }


    @Override
    public void load() {
        speedMul = StatusEffectSeq(30, 0.1f, 0.0f, "speedMul");
    }
}
