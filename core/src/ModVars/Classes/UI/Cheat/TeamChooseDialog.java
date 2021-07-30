package ModVars.Classes.UI.Cheat;

import arc.Core;
import arc.func.Cons;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Cell;
import arc.struct.Seq;
import mindustry.game.Team;
import mindustry.gen.Tex;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.mobile;

public class TeamChooseDialog extends BaseDialog {
    private Cons<Team> confirm;

    public TeamChooseDialog(Cons<Team> confirm) {
        super("Choose team:");
        this.confirm = confirm;
        setup();
        if (mobile) onResize(this::setup);
        this.addCloseButton();
    }

    private void setup() {
        cont.clear();
        this.cont.table(i -> {
            i.table(t -> {
                final int buttonSize = 20;
                int pad = 6;
                int coln = !mobile ? 20 : (Core.graphics.getWidth()) /(buttonSize+pad) - 2;
                for (Team team : Team.all) {
                    if (Seq.with(Team.all).indexOf(team) % coln == 0) t.row();
                    ImageButton button = new ImageButton(Tex.whitePane, Styles.clearToggleTransi);
                    button.clearChildren();
                    Image image = new Image();
                    button.background(image.getDrawable()).setColor(team.color);
                    Cell<Image> imageCell = button.add(image).color(team.color).size(buttonSize);
                    button.clicked(() -> {
                        confirm.get(team);
                        this.hide();
                    });
                    t.add(button).color(team.color).width(buttonSize).height(buttonSize).pad(pad);
                }
            });
        }).growX().bottom().center();
    }
}
