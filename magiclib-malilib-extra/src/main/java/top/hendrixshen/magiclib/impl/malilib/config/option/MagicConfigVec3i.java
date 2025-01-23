package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Vec3i;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3i;
import top.hendrixshen.magiclib.util.minecraft.serializable.Vec3iSerializer;

@Getter
@Setter
@ApiStatus.Experimental
public class MagicConfigVec3i extends ConfigBase<MagicConfigVec3i> implements ConfigVec3i {
    public static final Vec3iSerializer vec3iSerializer = new Vec3iSerializer();
    private final String translationPrefix;
    private final Vec3i defaultVec3iValue;
    private int x;
    private int y;
    private int z;

    public MagicConfigVec3i(String translationPrefix, String name) {
        this(translationPrefix, name, Vec3i.ZERO);
    }

    public MagicConfigVec3i(String translationPrefix, String name, Vec3i defaultValue) {
        super(null, name, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
        this.defaultVec3iValue = defaultValue;
        this.x = this.defaultVec3iValue.getX();
        this.y = this.defaultVec3iValue.getY();
        this.z = this.defaultVec3iValue.getZ();
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
        return this.x != this.defaultVec3iValue.getX()
                || this.y != this.defaultVec3iValue.getY()
                || this.z != this.defaultVec3iValue.getZ();
    }

    @Override
    public void resetToDefault() {
        this.x = this.defaultVec3iValue.getX();
        this.y = this.defaultVec3iValue.getY();
        this.z = this.defaultVec3iValue.getZ();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        Vec3i oldValue = this.getVec3i();

        try {
            Vec3i vec3i = MagicConfigVec3i.vec3iSerializer.deserialize(element.getAsJsonObject());
            this.setVec3i(vec3i);
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }

        if (!oldValue.equals(this.getVec3i())) {
            this.onValueChanged(true);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        return MagicConfigVec3i.vec3iSerializer.serialize(this.getVec3i());
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
