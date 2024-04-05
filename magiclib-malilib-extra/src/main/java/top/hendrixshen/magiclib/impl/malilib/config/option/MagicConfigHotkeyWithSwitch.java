package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.JsonUtils;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.option.HotkeyWithSwitch;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
public class MagicConfigHotkeyWithSwitch extends MagicConfigHotkey implements HotkeyWithSwitch {
    private final boolean defaultEnableState;
    private boolean enableState;

    public MagicConfigHotkeyWithSwitch(String translationPrefix, String name, boolean defaultEnableState,
                                       String defaultStorageString) {
        super(translationPrefix, name, defaultStorageString);
        this.defaultEnableState = defaultEnableState;
    }

    public MagicConfigHotkeyWithSwitch(String translationPrefix, String name, boolean defaultEnableState,
                                       String defaultStorageString, KeybindSettings settings) {
        super(translationPrefix, name, defaultStorageString, settings);
        this.defaultEnableState = defaultEnableState;
    }

    @Override
    public boolean isModified() {
        return super.isModified() || this.enableState != this.defaultEnableState;
    }

    @Override
    public void resetToDefault() {
        super.resetToDefault();
        this.enableState = this.defaultEnableState;
    }

    @Override
    public boolean getEnableState() {
        return this.enableState;
    }

    @Override
    public boolean getDefaultEnableState() {
        return this.defaultEnableState;
    }

    @Override
    public void setEnableState(boolean value) {
        boolean oldValue = this.enableState;
        this.enableState = value;

        if (this.enableState != oldValue) {
            this.onValueChanged(false);
        }
    }

    @Override
    public boolean isKeybindHeld() {
        return this.getEnableState() && super.isKeybindHeld();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        boolean oldState = this.getEnableState();
        super.setValueFromJsonElement(element);
        this.readExtraDataFromJson(element);

        if (oldState != this.getEnableState()) {
            this.onValueChanged(true);
        }
    }

    private void readExtraDataFromJson(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                if (JsonUtils.hasBoolean(obj, "enabled")) {
                    this.enableState = obj.get("enabled").getAsBoolean();
                }
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonElement jsonElement = super.getAsJsonElement();

        if (!jsonElement.isJsonObject()) {
            throw new RuntimeException("super should return a json object, but " + jsonElement + " found");
        }

        jsonElement.getAsJsonObject().addProperty("enabled", this.enableState);
        return jsonElement;
    }
}
