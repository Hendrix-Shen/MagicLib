package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Vec3i;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTuple;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTupleList.Entry;

@Getter
@Setter
@ApiStatus.Experimental
public class MagicConfigVec3iTuple extends MagicConfigVec3i implements ConfigVec3iTuple {
    private final Vec3i defaultSecondVec3iValue;
    private int secondX;
    private int secondY;
    private int secondZ;

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
        return super.isModified()
                || this.secondX != this.defaultSecondVec3iValue.getX()
                || this.secondY != this.defaultSecondVec3iValue.getY()
                || this.secondZ != this.defaultSecondVec3iValue.getZ();
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
        Entry oldValue = new Entry(this.getFirstVec3i(), this.getSecondVec3i());

        try {
            if (element.isJsonObject()) {
                JsonObject obj = element.getAsJsonObject();

                if (JsonUtils.hasObject(obj, "first")) {
                    JsonObject firstObj = obj.getAsJsonObject("first");
                    Vec3i firstVec3i = MagicConfigVec3i.vec3iSerializer.deserializeSafe(firstObj, Vec3i.ZERO);
                    this.setVec3i(firstVec3i);
                }

                if (JsonUtils.hasObject(obj, "second")) {
                    JsonObject secondObj = obj.getAsJsonObject("second");
                    Vec3i secondVec3i = MagicConfigVec3i.vec3iSerializer.deserializeSafe(secondObj, Vec3i.ZERO);
                    this.setSecondVec3i(secondVec3i);
                }
            } else {
                MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                        this.getName(), element);
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'",
                    this.getName(), element, e);
        }

        if (!oldValue.equals(new Entry(this.getFirstVec3i(), this.getSecondVec3i()))) {
            this.onValueChanged(true);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonObject obj = new JsonObject();
        obj.add("first", MagicConfigVec3i.vec3iSerializer.serialize(this.getFirstVec3i()));
        obj.add("second", MagicConfigVec3i.vec3iSerializer.serialize(this.getSecondVec3i()));
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
