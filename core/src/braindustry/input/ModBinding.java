package braindustry.input;

import braindustry.BDVars;
import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;

public enum ModBinding implements KeyBinds.KeyBind {

    special_key(KeyCode.h),
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
