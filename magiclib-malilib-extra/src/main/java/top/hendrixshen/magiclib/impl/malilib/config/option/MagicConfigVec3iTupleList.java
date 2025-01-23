package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.options.ConfigBase;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

import net.minecraft.core.Vec3i;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iTupleList;

import java.util.List;

@Getter
@ApiStatus.Experimental
public class MagicConfigVec3iTupleList extends ConfigBase<ConfigVec3iTupleList> implements ConfigVec3iTupleList {
    private final String translationPrefix;
    private final ImmutableList<ConfigVec3iTupleList.Entry> defaultVec3iTupleList;
    private final List<ConfigVec3iTupleList.Entry> vec3iTupleList = Lists.newArrayList();

    public MagicConfigVec3iTupleList(String translationPrefix, String name, ImmutableList<ConfigVec3iTupleList.Entry> defaultVec3iList) {
        super(null, name, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
        this.defaultVec3iTupleList = defaultVec3iList;
        this.vec3iTupleList.addAll(defaultVec3iList);
    }

    @Override
    public String getPrettyName() {
        return ConfigVec3iTupleList.super.getPrettyName();
    }

    @Override
    public String getConfigGuiDisplayName() {
        return ConfigVec3iTupleList.super.getConfigGuiDisplayName();
    }

    @Override
    public void setVec3iTupleList(List<ConfigVec3iTupleList.Entry> vec3iTuples) {
        if (!this.vec3iTupleList.equals(vec3iTuples)) {
            this.vec3iTupleList.clear();
            this.vec3iTupleList.addAll(vec3iTuples);
            this.onValueChanged();
        }
    }

    @Override
    public void resetToDefault() {
        this.setVec3iTupleList(this.defaultVec3iTupleList);
    }

    @Override
    public boolean isModified() {
        return !this.vec3iTupleList.equals(this.defaultVec3iTupleList);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        List<Entry> oldValue = Lists.newArrayList(this.vec3iTupleList);
        this.vec3iTupleList.clear();

        try {
            if (!element.isJsonArray()) {
                MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element);
                return;
            }

            JsonArray array = element.getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                if (!array.get(i).isJsonObject()) {
                    continue;
                }

                JsonObject vec3iTuple = array.get(i).getAsJsonObject();
                Vec3i firstVec3i = vec3iTuple.has("first")
                        ? MagicConfigVec3i.vec3iSerializer.deserializeSafe(vec3iTuple.get("first").getAsJsonObject(), Vec3i.ZERO)
                        : Vec3i.ZERO;
                Vec3i secondVec3i = vec3iTuple.has("second")
                        ? MagicConfigVec3i.vec3iSerializer.deserializeSafe(vec3iTuple.get("second").getAsJsonObject(), Vec3i.ZERO)
                        : Vec3i.ZERO;
                this.vec3iTupleList.add(new ConfigVec3iTupleList.Entry(firstVec3i, secondVec3i));
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }

        if (!oldValue.equals(this.vec3iTupleList)) {
            this.onValueChanged(true);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonArray array = new JsonArray();

        for (ConfigVec3iTupleList.Entry vec3i : this.vec3iTupleList) {
            JsonObject obj = new JsonObject();
            obj.add("first", MagicConfigVec3i.vec3iSerializer.serialize(vec3i.getFirstVec3i()));
            obj.add("second", MagicConfigVec3i.vec3iSerializer.serialize(vec3i.getSecondVec3i()));
            array.add(obj);
        }

        return array;
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
