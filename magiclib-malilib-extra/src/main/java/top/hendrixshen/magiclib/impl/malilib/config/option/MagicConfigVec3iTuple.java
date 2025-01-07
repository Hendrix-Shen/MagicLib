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
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTuple;

@Getter
@Setter
@ApiStatus.Experimental
public class MagicConfigVec3iTuple extends MagicConfigVec3i implements ConfigVec3iTuple {
    protected final Vec3i defaultSecondVec3iValue;
    protected int secondX;
    protected int secondY;
    protected int secondZ;

    public MagicConfigVec3iTuple(String translationPrefix, String name) {
        this(translationPrefix, name, Vec3i.ZERO, Vec3i.ZERO);
    }

    public MagicConfigVec3iTuple(String translationPrefix, String name, Vec3i defaultFirstValue, Vec3i defaultSecondValue) {
        super(translationPrefix, name, defaultFirstValue);
        this.defaultSecondVec3iValue = defaultSecondValue;
        this.secondX = this.defaultSecondVec3iValue.getX();
        this.secondY = this.defaultSecondVec3iValue.getY();
        this.secondZ = this.defaultSecondVec3iValue.getZ();
    }

    @Override
    public boolean isModified() {
        return super.isModified() ||
                this.secondX != this.defaultSecondVec3iValue.getX() ||
                this.secondY != this.defaultSecondVec3iValue.getY() ||
                this.secondZ != this.defaultSecondVec3iValue.getZ();
    }

    @Override
    public void resetToDefault() {
        super.resetToDefault();
        this.secondX = this.defaultSecondVec3iValue.getX();
        this.secondY = this.defaultSecondVec3iValue.getY();
        this.secondZ = this.defaultSecondVec3iValue.getZ();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                if (JsonUtils.hasObject(obj, "first")) {
                    JsonObject firstObj = obj.getAsJsonObject("first");
                    Vec3i firstVec3i = MagicConfigVec3i.vec3iSerializer.deserialize(firstObj);
                    this.setVec3i(firstVec3i);
                }

                if (JsonUtils.hasObject(obj, "second")) {
                    JsonObject secondObj = obj.getAsJsonObject("second");
                    Vec3i secondVec3i = MagicConfigVec3i.vec3iSerializer.deserialize(secondObj);
                    this.setSecondVec3i(secondVec3i);
                }
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject obj = new JsonObject();
        obj.add("first", MagicConfigVec3i.vec3iSerializer.serialize(this.getFirstVec3i()));
        obj.add("second", MagicConfigVec3i.vec3iSerializer.serialize(this.getSecondVec3i()));
        return obj;
    }
}
