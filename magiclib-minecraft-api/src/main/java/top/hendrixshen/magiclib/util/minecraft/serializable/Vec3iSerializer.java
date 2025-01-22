package top.hendrixshen.magiclib.util.minecraft.serializable;

import com.google.gson.JsonObject;
import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Vec3i;

import top.hendrixshen.magiclib.util.serializable.JsonSerializable;

public class Vec3iSerializer implements JsonSerializable<Vec3i, JsonObject> {
    @Override
    public JsonObject serialize(@NotNull Vec3i vec3i) {
        JsonObject obj = new JsonObject();
        obj.addProperty("x", vec3i.getX());
        obj.addProperty("y", vec3i.getY());
        obj.addProperty("z", vec3i.getZ());
        return obj;
    }

    @Override
    public Vec3i deserialize(@NotNull JsonObject jsonObject) {
        int x = jsonObject.get("x").getAsInt();
        int y = jsonObject.get("y").getAsInt();
        int z = jsonObject.get("z").getAsInt();
        return new Vec3i(x, y, z);
    }
}
