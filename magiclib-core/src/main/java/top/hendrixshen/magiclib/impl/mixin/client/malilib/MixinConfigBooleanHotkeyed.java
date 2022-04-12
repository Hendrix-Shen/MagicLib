package top.hendrixshen.magiclib.impl.mixin.client.malilib;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.MaLiLib;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.JsonUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

// backport new ConfigBooleanHotkeyed
@Dependencies(and = @Dependency(value = "malilib", versionPredicate = "<0.11.4"))
@Mixin(value = ConfigBooleanHotkeyed.class, remap = false)
public abstract class MixinConfigBooleanHotkeyed extends ConfigBoolean implements IHotkeyTogglable {


    @Final
    @Shadow
    protected IKeybind keybind;

    public MixinConfigBooleanHotkeyed(String name, boolean defaultValue, String comment) {
        super(name, defaultValue, comment);
    }

    @Override
    public boolean isModified() {
        // Note: calling isModified() for the IHotkey here directly would not work
        // with multi-type configs like the FeatureToggle in Tweakeroo!
        // Thus we need to get the IKeybind and call it for that specifically.
        return super.isModified() || this.getKeybind().isModified();
    }

    @Override
    public void resetToDefault() {
        super.resetToDefault();
        this.keybind.resetToDefault();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                if (JsonUtils.hasBoolean(obj, "enabled")) {
                    super.setValueFromJsonElement(obj.get("enabled"));
                }

                if (this.keybind instanceof IHotkey && JsonUtils.hasObject(obj, "hotkey")) {
                    JsonObject hotkeyObj = obj.getAsJsonObject("hotkey");

                    if (JsonUtils.hasString(hotkeyObj, "keys")) {
                        this.setValueFromString(hotkeyObj.get("keys").getAsString());
                    }

                    if (JsonUtils.hasObject(hotkeyObj, "settings")) {
                        this.keybind.setSettings(KeybindSettings.fromJson(hotkeyObj.getAsJsonObject("settings")));
                    }
                }
            }
            // Backwards compatibility with the old bugged serialization that only serialized the boolean value
            else {
                super.setValueFromJsonElement(element);
            }
        } catch (Exception e) {
            MaLiLib.logger.warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject obj = new JsonObject();
        obj.add("enabled", super.getAsJsonElement());

        JsonObject keyBindObj = new JsonObject();
        keyBindObj.add("keys", new JsonPrimitive(this.getStringValue()));
        if (this.keybind.areSettingsModified()) {
            keyBindObj.add("settings", this.keybind.getSettings().toJson());
        }
        obj.add("hotkey", keyBindObj);
        return obj;
    }
}
