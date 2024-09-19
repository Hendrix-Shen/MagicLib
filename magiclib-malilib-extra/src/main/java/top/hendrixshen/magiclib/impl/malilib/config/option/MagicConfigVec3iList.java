package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.config.options.ConfigBase;
import lombok.Getter;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.option.ConfigVec3iList;

import java.util.List;

@Getter
@ApiStatus.Experimental
public class MagicConfigVec3iList extends ConfigBase<MagicConfigVec3iList> implements ConfigVec3iList {
    private final String translationPrefix;
    private final ImmutableList<Vec3i> defaultVec3iList;
    private final List<Vec3i> vec3iList = Lists.newArrayList();

    public MagicConfigVec3iList(String translationPrefix, String name, ImmutableList<Vec3i> defaultVec3iList) {
        super(null, name, String.format("%s.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
        this.defaultVec3iList = defaultVec3iList;
        this.vec3iList.addAll(defaultVec3iList);
    }

    @Override
    public String getPrettyName() {
        return ConfigVec3iList.super.getPrettyName();
    }

    @Override
    public String getConfigGuiDisplayName() {
        return ConfigVec3iList.super.getConfigGuiDisplayName();
    }

    @Override
    public void setVec3iList(List<Vec3i> vec3is) {
        if (!this.vec3iList.equals(vec3is)) {
            this.vec3iList.clear();
            this.vec3iList.addAll(vec3is);
            this.onValueChanged();
        }
    }

    @Override
    public void resetToDefault() {
        this.setVec3iList(this.defaultVec3iList);
    }

    @Override
    public boolean isModified() {
        return !this.vec3iList.equals(this.defaultVec3iList);
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        this.vec3iList.clear();

        try {
            if (element.isJsonArray()) {
                JsonArray array = element.getAsJsonArray();

                for (int i = 0; i < array.size(); i++) {
                    if (!array.get(i).isJsonObject()) {
                        continue;
                    }

                    JsonObject obj = array.get(i).getAsJsonObject();
                    int x = obj.has("x") ? obj.get("x").getAsInt() : 0;
                    int y = obj.has("y") ? obj.get("y").getAsInt() : 0;
                    int z = obj.has("z") ? obj.get("z").getAsInt() : 0;
                    this.vec3iList.add(new Vec3i(x, y, z));
                }
            } else {
                MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element);
            }
        } catch (Exception e) {
            MagicLib.getLogger().warn("Failed to set config value for '{}' from the JSON element '{}'", this.getName(), element, e);
        }
    }

    @Override
    public JsonElement getAsJsonElement() {
        JsonArray array = new JsonArray();

        for (Vec3i vec3i : this.vec3iList) {
            JsonObject obj = new JsonObject();
            obj.addProperty("x", vec3i.getX());
            obj.addProperty("y", vec3i.getY());
            obj.addProperty("z", vec3i.getZ());
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
