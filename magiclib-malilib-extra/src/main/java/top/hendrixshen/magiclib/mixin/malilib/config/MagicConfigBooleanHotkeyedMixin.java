package top.hendrixshen.magiclib.mixin.malilib.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import fi.dy.masa.malilib.config.IHotkeyTogglable;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.JsonUtils;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.malilib.MixinPredicates;
import top.hendrixshen.magiclib.impl.malilib.config.option.MagicConfigBooleanHotkeyed;

@Dependencies(require = @Dependency(dependencyType = DependencyType.PREDICATE, predicate = MixinPredicates.Malilib_0_11_4.class))
@Mixin(value = MagicConfigBooleanHotkeyed.class, remap = false)
public abstract class MagicConfigBooleanHotkeyedMixin extends ConfigBoolean implements IHotkeyTogglable {
    public MagicConfigBooleanHotkeyedMixin(String name, boolean defaultValue, String comment) {
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
        this.getKeybind().resetToDefault();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                if (JsonUtils.hasBoolean(obj, "enabled")) {
                    super.setValueFromJsonElement(obj.get("enabled"));
                }

                if (JsonUtils.hasObject(obj, "hotkey")) {
                    JsonObject hotkeyObj = obj.getAsJsonObject("hotkey");

                    if (JsonUtils.hasString(hotkeyObj, "keys")) {
                        this.getKeybind().setValueFromString(hotkeyObj.get("keys").getAsString());
                    }

                    if (JsonUtils.hasObject(hotkeyObj, "settings")) {
                        this.getKeybind().setSettings(KeybindSettings.fromJson(
                                hotkeyObj.getAsJsonObject("settings")));
                    }
                }
            } else {
                // Backwards compatibility with the old bugged serialization that only serialized the boolean value
                super.setValueFromJsonElement(element);
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject obj = new JsonObject();
        obj.add("enabled", super.getAsJsonElement());
        JsonObject keyBindObj = new JsonObject();
        keyBindObj.add("keys", new JsonPrimitive(this.getKeybind().getStringValue()));

        if (this.getKeybind().areSettingsModified()) {
            keyBindObj.add("settings", this.getKeybind().getSettings().toJson());
        }

        obj.add("hotkey", keyBindObj);
        return obj;
    }
}
