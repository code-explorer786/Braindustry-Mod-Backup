package ModVars.Classes.UI.settings;

import ModVars.modVars;
import arc.Core;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.util.Strings;
import mindustry.gen.Icon;

public class ModOtherSettingsDialog extends Dialog {
    public ModOtherSettingsDialog() {
        super("@dialogs.other-settings");
        setup();
    }

    private void setup() {

        cont.table(t -> {
            t.left().defaults();
            Cell<Label> label = t.label(() -> Strings.format("@: @/@", Core.bundle.get("background.screenshot.scl"), (int) modVars.settings.getFloat("background.screenshot.scl"), 5)).left();
            t.row();
            t.slider(1f, 5f, 1f, modVars.settings.getFloat("background.screenshot.scl"), f -> {
                modVars.settings.setFloat("background.screenshot.scl", f);
            }).left().width(180.0F);
            label.minWidth(label.get().getPrefWidth() / Scl.scl(1.0F) + 50.0F);
            t.row();
        }).padTop(3.0F).left().row();
        cont.table((t) -> {
            t.check("@cheat", modVars.settings.cheating(), (b) -> {
                modVars.settings.cheating(b);
            }).left().row();
            t.check("@debug", modVars.settings.debug(), (b) -> {
                modVars.settings.debug(b);
            }).left().row();

            t.slider(0, 360, 0.1f, modVars.settings.getFloat("angle"), (b) -> {
                modVars.settings.setFloat("angle", b);
            }).left().row();

        }).left().row();

        closeOnBack();
        cont.button("@back", Icon.leftOpen, () -> {
            hide();
        }).size(230.0F, 64.0F).bottom();
    }
}
