package top.hendrixshen.magiclib.api.malilib.config.option;

import net.minecraft.core.Vec3i;

public interface ConfigVec3iTuple extends ConfigVec3i {
    default Vec3i getFirstVec3i() {
        return this.getVec3i();
    }

    default Vec3i getSecondVec3i() {
        return new Vec3i(this.getSecondX(), this.getSecondY(), this.getSecondZ());
    }

    default void setFirstVec3i(Vec3i value) {
        this.setVec3i(value);
    }

    default void setSecondVec3i(Vec3i value) {
        this.setSecondX(value.getX());
        this.setSecondY(value.getY());
        this.setSecondZ(value.getZ());
    }

    default Vec3i getDefaultFirstVec3iValue() {
        return this.getDefaultVec3iValue();
    }

    Vec3i getDefaultSecondVec3iValue();

    default int getFirstX() {
        return this.getX();
    }

    default int getFirstY() {
        return this.getY();
    }

    default int getFirstZ() {
        return this.getZ();
    }

    int getSecondX();

    int getSecondY();

    int getSecondZ();

    default void setFirstX(int x) {
        this.setX(x);
    }

    default void setFirstY(int y) {
        this.setY(y);
    }

    default void setFirstZ(int z) {
        this.setZ(z);
    }

    void setSecondX(int x);

    void setSecondY(int y);

    void setSecondZ(int z);
}
