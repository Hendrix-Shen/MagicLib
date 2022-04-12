package top.hendrixshen.magiclib.impl.util;

import fi.dy.masa.malilib.config.gui.ConfigOptionChangeListenerKeybind;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.gui.button.ButtonBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonKeybind;
import fi.dy.masa.malilib.gui.interfaces.IKeybindConfigGui;

public class HotkeyedBooleanResetListener extends ConfigOptionChangeListenerKeybind {
    private final ConfigBooleanHotkeyed config;
    private final ButtonGeneric booleanButton;

    public HotkeyedBooleanResetListener(ConfigBooleanHotkeyed config, ButtonGeneric booleanButton, ConfigButtonKeybind hotkeyButton, ButtonGeneric resetButton, IKeybindConfigGui host) {
        super(config.getKeybind(), hotkeyButton, resetButton, host);
        this.config = config;
        this.booleanButton = booleanButton;
    }

    @Override
    public void actionPerformedWithButton(ButtonBase button, int mouseButton) {
        this.config.resetToDefault();
        super.actionPerformedWithButton(button, mouseButton);
    }

    @Override
    public void updateButtons() {
        this.booleanButton.updateDisplayString();
        super.updateButtons();
    }
}
