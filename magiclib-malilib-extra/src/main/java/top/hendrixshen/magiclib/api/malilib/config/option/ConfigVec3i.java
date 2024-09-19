package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigResettable;
import net.minecraft.core.Vec3i;

public interface ConfigVec3i extends IConfigResettable, MagicIConfigBase {
    default Vec3i getVec3i() {
        return new Vec3i(getX(), getY(), getZ());
    }

    default void setVec3i(Vec3i value) {
        this.setX(value.getX());
        this.setY(value.getY());
        this.setZ(value.getZ());
    }

    Vec3i getDefaultVec3iValue();

    int getX();

    int getY();

    int getZ();

    void setX(int x);

    void setY(int y);

    void setZ(int z);
}
