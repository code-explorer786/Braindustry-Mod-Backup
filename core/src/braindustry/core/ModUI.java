package braindustry.core;

import braindustry.ui.dialogs.ModOtherSettingsDialog;
import arc.ApplicationListener;
import arc.Core;
import arc.Input;
import arc.KeyBinds;
import arc.func.Cons;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.ui.Dialog;
import arc.scene.ui.Label;
import arc.scene.ui.TextButton;
import arc.scene.ui.TextField;
import arc.scene.ui.layout.Collapser;
import arc.scene.ui.layout.Table;
import arc.util.Disposable;
import arc.util.Log;
import arc.util.Strings;
import arc.util.Time;
import braindustry.gen.ModTex;
import braindustry.gen.Stealthc;
import braindustry.input.ModBinding;
import braindustry.ui.ModStyles;
import braindustry.ui.dialogs.ModColorPicker;
import braindustry.ui.fragments.ModHudFragment;
import braindustry.ui.fragments.ModMenuFragment;
import mindustry.Vars;
import mindustry.ui.Styles;

import java.util.Objects;

import static braindustry.BDVars.*;
import static arc.Core.settings;
import static mindustry.Vars.headless;
import static mindustry.Vars.ui;

public class ModUI implements Disposable, ApplicationListener {
    static {
        //x axis or not
        ModMenuFragment.xAxis(true);
        //wave size in pixels
        ModMenuFragment.pixels(3f);
        //wave force
        ModMenuFragment.otherAxisMul(50);
        //waves speed
        ModMenuFragment.timeScl(0.1f);

    }

    public ModColorPicker colorPicker;
    private boolean inited=false;

    public ModUI() {
        Time.mark();
        KeyBinds.KeyBind[] keyBinds = Core.keybinds.getKeybinds();
        KeyBinds.KeyBind[] modBindings = ModBinding.values();
        KeyBinds.KeyBind[] defs = new KeyBinds.KeyBind[keyBinds.length + modBindings.length];
        for (int i = 0; i < defs.length; i++) {
            if (i<keyBinds.length){
                defs[i]=keyBinds[i];
            } else {
                defs[i]=modBindings[i-keyBinds.length];
            }
        }
        Log.info("[Braindustry]Time to combine arrays: @ms",Time.elapsed());
        Core.keybinds.setDefaults(defs);
        settings.load();
    }

    @Override
    public void init() {
        if (headless) return;
        inited=true;
        inTry(ModTex::load);
        inTry(ModStyles::load);
        inTry(ModMenuFragment::init);
        inTry(ModHudFragment::init);

        colorPicker = new ModColorPicker();
    }

    @Override
    public void dispose() {
    }
    @Override
    public void update() {
        if (!inited)return;
        boolean noDialog = !Core.scene.hasDialog();
        boolean inGame = Vars.state.isGame();

        boolean inMenu = Vars.state.isMenu() || !ui.planet.isShown();
        if (inGame && Vars.state.isPaused() && Vars.player.unit() instanceof Stealthc) {
            Stealthc unit = (Stealthc) Vars.player.unit();
            unit.updateStealthStatus();
        }
    }
    public static void showExceptionDialog(Throwable t) {
        showExceptionDialog("", t);
    }
    public static Dialog getInfoDialog(String title, String subTitle, String message, Color lineColor) {
        return new Dialog(title) {
            {
                this.setFillParent(true);
                this.cont.margin(15.0F);
                this.cont.add(subTitle);
                this.cont.row();
                this.cont.image().width(300.0F).pad(2.0F).height(4.0F).color(lineColor);
                this.cont.row();
                (this.cont.add(message).pad(2.0F).growX().wrap().get()).setAlignment(1);
                this.cont.row();
                this.cont.button("@ok", this::hide).size(120.0F, 50.0F).pad(4.0F);
                this.closeOnBack();
            }
        };
    }
    public static void showExceptionDialog(final String text, final Throwable exc) {
        (new Dialog("") {
            {
                String message = Strings.getFinalMessage(exc);
                this.setFillParent(true);
                this.cont.margin(15.0F);
                this.cont.add("@error.title").colspan(2);
                this.cont.row();
                this.cont.image().width(300.0F).pad(2.0F).colspan(2).height(4.0F).color(Color.scarlet);
                this.cont.row();
                ((Label) this.cont.add((text.startsWith("@") ? Core.bundle.get(text.substring(1)) : text) + (message == null ? "" : "\n[lightgray](" + message + ")")).colspan(2).wrap().growX().center().get()).setAlignment(1);
                this.cont.row();
                Collapser col = new Collapser((base) -> {
                    base.pane((t) -> {
                        t.margin(14.0F).add(Strings.neatError(exc)).color(Color.lightGray).left();
                    });
                }, true);
                Table var10000 = this.cont;
                TextButton.TextButtonStyle var10002 = Styles.togglet;
                Objects.requireNonNull(col);
                var10000.button("@details", var10002, col::toggle).size(180.0F, 50.0F).checked((b) -> {
                    return !col.isCollapsed();
                }).fillX().right();
                this.cont.button("@ok", this::hide).size(110.0F, 50.0F).fillX().left();
                this.cont.row();
                this.cont.add(col).colspan(2).pad(2.0F);
                this.closeOnBack();
            }
        }).show();
    }
    public static void showTextInput(String title, String text, String def, Cons<String> confirmed) {
        showTextInput(title, text, 32, def, confirmed);
    }

    public static void showTextInput(String titleText, String text, int textLength, String def, Cons<String> confirmed) {
        showTextInput(titleText, text, textLength, def, (t,c)->{return true;}, confirmed);
    }
    public static void showTextInput(final String titleText, final String dtext, final int textLength, final String def, final TextField.TextFieldFilter filter, final Cons<String> confirmed) {
        if (Vars.mobile) {
            Core.input.getTextInput(new Input.TextInput() {
                {
                    this.title = titleText.startsWith("@") ? Core.bundle.get(titleText.substring(1)) : titleText;
                    this.text = def;
                    this.numeric=filter== TextField.TextFieldFilter.digitsOnly;
//                    this.numeric = inumeric;
                    this.maxLength = textLength;
                    this.accepted = confirmed;
                }
            });
        } else {
            Dialog var10001 = new Dialog(titleText) {
                {
                    this.cont.margin(30.0F).add(dtext).padRight(6.0F);
                    TextField field = (TextField)this.cont.field(def, (t) -> {
                    }).size(330.0F, 50.0F).get();
                    field.setFilter((f, c) -> {
                        return field.getText().length() < textLength && filter.acceptChar(f, c);
                    });
                    this.buttons.defaults().size(120.0F, 54.0F).pad(4.0F);
                    this.buttons.button("@cancel", this::hide);
                    this.buttons.button("@ok", () -> {
                        confirmed.get(field.getText());
                        this.hide();
                    }).disabled((b) -> {
                        return field.getText().isEmpty();
                    });
                    this.keyDown(KeyCode.enter, () -> {
                        String text = field.getText();
                        if (!text.isEmpty()) {
                            confirmed.get(text);
                            this.hide();
                        }

                    });
                    this.keyDown(KeyCode.escape, this::hide);
                    this.keyDown(KeyCode.back, this::hide);
                    this.show();
                    Core.scene.setKeyboardFocus(field);
                    field.setCursorPosition(def.length());
                }
            };
        }

    }

}
