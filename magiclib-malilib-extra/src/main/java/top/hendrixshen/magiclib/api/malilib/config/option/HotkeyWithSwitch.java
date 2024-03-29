package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigBoolean;
import fi.dy.masa.malilib.hotkeys.IHotkey;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public interface HotkeyWithSwitch extends IConfigBoolean, IHotkey {
    /**
     * If the hotkey is enabled
     */
    boolean getEnableState();

    boolean getDefaultEnableState();

    void setEnableState(boolean value);

    boolean isKeybindHeld();

    @Override
    default boolean getBooleanValue() {
        return this.getEnableState();
    }

    @Override
    default boolean getDefaultBooleanValue() {
        return this.getDefaultEnableState();
    }

    @Override
    default void setBooleanValue(boolean value) {
        this.setEnableState(value);
    }
}
