package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3i;

@Getter
@Setter
@ApiStatus.Experimental
public class MagicConfigVec3i extends ConfigBase<MagicConfigVec3i> implements ConfigVec3i {
    private final String translationPrefix;
    protected final Vec3i defaultVec3iValue;
    protected int x;
    protected int y;
    protected int z;

    public MagicConfigVec3i(String translationPrefix, String name) {
        this(translationPrefix, name, Vec3i.ZERO);
    }

    public MagicConfigVec3i(String translationPrefix, String name, Vec3i defaultValue) {
        super(null, name, String.format("%s.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
        this.defaultVec3iValue = defaultValue;
        this.resetToDefault();
    }

    @Override
    public String getPrettyName() {
        return ConfigVec3i.super.getPrettyName();
    }

    @Override
    public String getConfigGuiDisplayName() {
        return ConfigVec3i.super.getConfigGuiDisplayName();
    }

    @Override
    public boolean isModified() {
        return this.x != this.defaultVec3iValue.getX() ||
                this.y != this.defaultVec3iValue.getY() ||
                this.z != this.defaultVec3iValue.getZ();
    }

    @Override
    public void resetToDefault() {
        this.x = this.defaultVec3iValue.getX();
        this.y = this.defaultVec3iValue.getY();
        this.z = this.defaultVec3iValue.getZ();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                if (JsonUtils.hasInteger(obj, "x")) {
                    this.x = obj.get("x").getAsInt();
                }

                if (JsonUtils.hasInteger(obj, "y")) {
                    this.y = obj.get("y").getAsInt();
                }

                if (JsonUtils.hasInteger(obj, "z")) {
                    this.z = obj.get("z").getAsInt();
                }
            } else {
                MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                        this.getName(), element);
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", this.x);
        obj.addProperty("y", this.y);
        obj.addProperty("z", this.z);
        return obj;
    }

    @Override
    public void onValueChanged() {
        this.onValueChanged(false);
    }

    @Override
    public void onValueChanged(boolean fromFile) {
        super.onValueChanged();

        if (!fromFile && this.getMagicContainer().shouldStatisticValueChange()) {
            this.updateStatisticOnUse();
        }
    }
}
