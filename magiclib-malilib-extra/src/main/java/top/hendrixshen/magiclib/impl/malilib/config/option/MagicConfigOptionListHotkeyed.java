package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindMulti;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.malilib.config.option.OptionListHotkeyed;
import top.hendrixshen.magiclib.util.minecraft.InfoUtil;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 *
 * <p>
 * OptionList with a hotkey to cycle the value
 */
public class MagicConfigOptionListHotkeyed extends MagicConfigOptionList implements OptionListHotkeyed {
    protected final IKeybind keybind;

    public MagicConfigOptionListHotkeyed(String translationPrefix, String name, IConfigOptionListEntry defaultValue,
                                         String defaultHotkey) {
        this(translationPrefix, name, defaultValue, defaultHotkey, KeybindSettings.DEFAULT);
    }

    public MagicConfigOptionListHotkeyed(String translationPrefix, String name, IConfigOptionListEntry defaultValue,
                                         String defaultHotkey, KeybindSettings settings) {
        super(translationPrefix, name, defaultValue);
        this.keybind = KeybindMulti.fromStorageString(defaultHotkey, settings);
        this.keybind.setCallback(this::onHotkey);
    }

    @Override
    public IKeybind getKeybind() {
        return this.keybind;
    }

    @Override
    public void resetToDefault() {
        super.resetToDefault();
        this.keybind.resetToDefault();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        IConfigOptionListEntry oldValue = this.getOptionListValue();

        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();
                this.setValueFromString(obj.get("value").getAsString());
                this.getKeybind().setValueFromString(obj.get("keybind").getAsString());
            } else if (element.isJsonPrimitive()) {
                // a malilib OptionList
                this.setValueFromString(element.getAsString());
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }

        if (oldValue != this.getOptionListValue()) {
            this.onValueChanged(true);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject obj = new JsonObject();
        obj.addProperty("value", this.getOptionListValue().getStringValue());
        obj.addProperty("keybind", this.getKeybind().getStringValue());
        return obj;
    }

    private boolean onHotkey(KeyAction keyAction, IKeybind iKeybind) {
        IConfigOptionListEntry newValue = this.getOptionListValue().cycle(true);
        this.setOptionListValue(newValue);
        InfoUtil.displayActionBarMessage(
                ComponentCompat.translatable("magiclib.config.message.option_list_hotkeyed.cycled_message",
                        this.getName(), GuiBase.TXT_GOLD + newValue.getDisplayName() + GuiBase.TXT_RST).get());
        return true;
    }
}
