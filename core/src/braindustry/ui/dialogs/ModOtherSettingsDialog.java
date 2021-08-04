package braindustry.ui.dialogs;

import braindustry.BDVars;
import arc.Core;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.util.Strings;
import mindustry.gen.Icon;

import static mindustry.Vars.mobile;

public class ModOtherSettingsDialog extends Dialog {
    public ModOtherSettingsDialog() {
        super("@dialogs.other-settings");
        setup();
    }

    private void setup() {

        cont.table(t -> {
            t.left().defaults();
            Cell<Label> label = t.label(() -> Strings.format("@: @/@", Core.bundle.get("background.screenshot.scl"), (int) BDVars.settings.getFloat("background.screenshot.scl"), 5)).left();
            t.row();
            t.slider(1f, 5f, 1f, BDVars.settings.getFloat("background.screenshot.scl"), f -> {
                BDVars.settings.setFloat("background.screenshot.scl", f);
            }).left().width(180.0F);
            label.minWidth(label.get().getPrefWidth() / Scl.scl(1.0F) + 50.0F);
            t.row();
        }).padTop(3.0F).left().row();
        cont.table((t) -> {
            t.check("@cheat", BDVars.settings.cheating(), (b) -> {
                BDVars.settings.cheating(b);
            }).left().row();
            t.check("@debug", BDVars.settings.debug(), (b) -> {
                BDVars.settings.debug(b);
            }).left().row();

            t.slider(0, 360, 0.1f, BDVars.settings.getFloat("angle"), (b) -> {
                BDVars.settings.setFloat("angle", b);
            }).left().row();
            if (!mobile)return;
            t.check("@background.style.title", BDVars.settings.debug(), (b) -> {
                BDVars.settings.debug(b);
            }).left().row();

        }).left().row();

        closeOnBack();
        cont.button("@back", Icon.leftOpen, () -> {
            hide();
        }).size(230.0F, 64.0F).bottom();
    }
}
