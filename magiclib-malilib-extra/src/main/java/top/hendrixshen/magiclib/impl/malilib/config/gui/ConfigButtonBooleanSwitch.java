package top.hendrixshen.magiclib.impl.malilib.config.gui;

import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;
import top.hendrixshen.magiclib.api.malilib.config.option.HotkeyWithSwitch;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public class ConfigButtonBooleanSwitch extends ButtonGeneric {
    private final HotkeyWithSwitch config;

    public ConfigButtonBooleanSwitch(int x, int y, int width, int height, HotkeyWithSwitch config) {
        super(x, y, width, height, "");
        this.config = config;
        this.updateDisplayString();
    }

    @Override
    protected boolean onMouseClickedImpl(int mouseX, int mouseY, int mouseButton) {
        this.config.toggleBooleanValue();
        this.updateDisplayString();
        return super.onMouseClickedImpl(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateDisplayString() {
        if (this.config.getEnableState()) {
            this.displayString = GuiBase.TXT_DARK_GREEN +
                    I18n.tr("magiclib.config.gui.element.config_button_boolean_switch.enabled") + GuiBase.TXT_RST;
        } else {
            this.displayString = GuiBase.TXT_DARK_RED +
                    I18n.tr("magiclib.config.gui.element.config_button_boolean_switch.disabled") + GuiBase.TXT_RST;
        }
    }
}
