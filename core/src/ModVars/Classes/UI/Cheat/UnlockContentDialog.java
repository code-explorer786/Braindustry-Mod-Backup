package ModVars.Classes.UI.Cheat;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.ui.Image;
import arc.scene.ui.ImageButton;
import arc.scene.ui.TextButton;
import arc.scene.ui.layout.Cell;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import braindustry.ui.ModStyles;
import mindustry.Vars;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.Tex;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

public class UnlockContentDialog extends BaseDialog {
    private static int counter = 0;
    private static float allScale = 1f;
    private Table items;

    public UnlockContentDialog() {
        super("Unlock content dialog");
        addCloseButton();
        addCloseListener();
    }

    @Override
    public void addCloseButton() {
        this.setFillParent(true);
        this.keyDown((key) -> {
            if (key == KeyCode.escape || key == KeyCode.back) {
                Core.app.post(this::hide);
            }

        });
        this.cont.pane((t) -> {
            this.items = t.margin(10.0F);
        }).left();
        this.shown(this::setup);
        this.hidden(() -> {
        });
        super.addCloseButton();
    }

    void setup() {
        this.items.clearChildren();
        this.items.left();
        float bsize = 40.0F;
        counter = 0;
        Vars.content.each(c -> {
            if (c instanceof UnlockableContent) {

                UnlockableContent content = (UnlockableContent) c;
                if (content instanceof Block && (((Block) content).buildVisibility != BuildVisibility.shown && ((Block) content).buildVisibility != BuildVisibility.campaignOnly))
                    return;
                this.items.table(Tex.pane, (t) -> {
                    t.margin(4.0F).marginRight(8.0F).left();
                    Cell<Image> imageCell = t.image(content.uiIcon).size(24.0F).padRight(4.0F).padLeft(4.0F);
                    TextButton button = new TextButton("",ModStyles.buttonColor);
                    t.label(() -> "").update(l -> {
                        l.setText(content.localizedName);
                        l.setFontScale(1f);
                        l.invalidate();
                        float freePlace = t.getWidth() - button.getWidth() - imageCell.prefWidth() - Scl.scl(4) * 2f;
                        float freePlaceY = t.getHeight();
                        float scaleX = Math.min(1f, freePlace / l.getPrefWidth()), scaleY = 1f;
                        while (scaleX < 0.60f && l.getText().toString().contains(" ")) {
                            String text = l.getText().toString();
                            int index = text.lastIndexOf(" ");
                            l.setText(text.substring(0, index) + "\n" + text.substring(index + 1).intern());
                            l.setFontScale(1f);
                            l.invalidate();
                            scaleX = Math.min(1f, freePlace / l.getPrefWidth());
                            scaleY = Math.min(1f, freePlaceY / l.getPrefHeight());
                        }
                        allScale = Math.min(allScale, scaleX * scaleY);
                        l.setFontScale(allScale);
                    }).left().width(90.0F * 2f);
                    button.clicked(() -> {
                        if (content.unlocked()) {
                            content.clearUnlock();
                        } else {
                            content.unlock();
                        }
                    });
//                   button.image().size(bsize);
//                    Cell<Image> imageCell = button.add(image).size(bsize);

                    t.add(button).size(bsize).update((b) -> {
//                        b.getImage().fillParent=true;
//                        b.getImage().setDrawable(content.unlocked()?Icon.cancel:Icon.add);
                        b.setColor(content.unlocked() ? Color.lime : Color.scarlet);
//                        b.getImageCell().color(b.color);
                    });
                }).pad(2.0F).height(36.0f).left().fillX();
                counter++;
                int coln = Vars.mobile ? 2 : 3;
                if (counter % coln == 0) {
                    this.items.row();
                }
            }
        });
    }
}
