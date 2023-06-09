package braindustry.input;

import braindustry.BDVars;
import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;

public enum ModBinding implements KeyBinds.KeyBind {

    show_team_dialog(KeyCode.b),
    show_unit_dialog(KeyCode.u),
    show_rules_edit_dialog(KeyCode.unknown),
    show_item_manager_dialog(KeyCode.unknown),
    show_unlock_dialog(KeyCode.unknown),
    special_key(KeyCode.h),
    menu_background_screenshot(KeyCode.unknown),
    ;

    private final KeyBinds.KeybindValue defaultValue;
    private final String category;

    private ModBinding(KeyBinds.KeybindValue defaultValue, String category) {
        this.defaultValue = defaultValue;
        this.category = category;
    }

    private ModBinding(KeyBinds.KeybindValue defaultValue) {
        this(defaultValue, null);
    }

    public KeyBinds.KeybindValue defaultValue(InputDevice.DeviceType type) {
        return this.defaultValue;
    }

    public String category() {
        return category != null ? category : BDVars.modName();
    }
}
