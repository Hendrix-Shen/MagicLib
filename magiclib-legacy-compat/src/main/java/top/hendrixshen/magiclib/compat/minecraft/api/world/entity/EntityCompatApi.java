package top.hendrixshen.magiclib.compat.minecraft.api.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface EntityCompatApi {
    default Level getLevelCompat() {
        throw new UnImplCompatApiException();
    }

    default double getXCompat() {
        throw new UnImplCompatApiException();
    }

    default double getYCompat() {
        throw new UnImplCompatApiException();
    }

    default double getZCompat() {
        throw new UnImplCompatApiException();
    }

    default int getBlockXCompat() {
        return this.blockPositionCompat().getX();
    }

    default int getBlockYCompat() {
        return this.blockPositionCompat().getY();
    }

    default int getBlockZCompat() {
        return this.blockPositionCompat().getZ();
    }

    default float getYRotCompat() {
        throw new UnImplCompatApiException();
    }

    default void setYRotCompat(float yRot) {
        throw new UnImplCompatApiException();
    }

    default float getXRotCompat() {
        throw new UnImplCompatApiException();
    }

    default void setXRotCompat(float xRot) {
        throw new UnImplCompatApiException();
    }

    default BlockPos blockPositionCompat() {
        throw new UnImplCompatApiException();
    }

    default boolean isOnGroundCompat() {
        throw new UnImplCompatApiException();
    }

    default void setOnGroundCompat(boolean onGround) {
        throw new UnImplCompatApiException();
    }

    default void sendSystemMessageCompat(Component component) {
        throw new UnImplCompatApiException();
    }

    default float maxUpStepCompat() {
        throw new UnImplCompatApiException();
    }

    default void setMaxUpStepCompat(float maxUpStep) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11904
    //$$ default float maxUpStep() {
    //$$     return this.maxUpStepCompat();
    //$$ }
    //$$
    //$$ default void setMaxUpStep(float maxUpStep) {
    //$$     this.setMaxUpStepCompat(maxUpStep);
    //$$ }
    //#endif

    //#if MC < 11900
    //$$ default void sendSystemMessage(Component component) {
    //$$     this.sendSystemMessageCompat(component);
    //$$ }
    //#endif

    //#if MC <= 11701
    //$$ // fuck remap
    //$$ // It will cause remap fail...
    //$$ ////$$ default Level getLevel() {
    //$$ ////$$     return this.getLevelCompat();
    //$$ ////$$ }
    //#endif

    //#if MC <= 11605
    //$$ default int getBlockX() {
    //$$     return this.getBlockXCompat();
    //$$ }
    //$$
    //$$ default int getBlockY() {
    //$$     return this.getBlockYCompat();
    //$$ }
    //$$
    //$$ default int getBlockZ() {
    //$$     return this.getBlockZCompat();
    //$$ }
    //$$
    //$$ default float getYRot() {
    //$$     return this.getYRotCompat();
    //$$ }
    //$$
    //$$ default void setYRot(float yRot) {
    //$$     this.setYRotCompat(yRot);
    //$$ }
    //$$
    //$$ default float getXRot() {
    //$$     return this.getXRotCompat();
    //$$ }
    //$$
    //$$ default void setXRot(float xRot) {
    //$$     this.setXRotCompat(xRot);
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ default BlockPos blockPosition() {
    //$$     return this.blockPositionCompat();
    //$$ }
    //$$
    //$$ default boolean isOnGround() {
    //$$     return this.isOnGroundCompat();
    //$$ }
    //$$
    //$$ default void setOnGround(boolean onGround) {
    //$$     this.setOnGroundCompat(onGround);
    //$$ }
    //#endif

    //#if MC <= 11404
    //$$ default double getX() {
    //$$     return this.getXCompat();
    //$$ }
    //$$
    //$$ default double getY() {
    //$$     return this.getYCompat();
    //$$ }
    //$$
    //$$ default double getZ() {
    //$$     return this.getZCompat();
    //$$ }
    //#endif
}
