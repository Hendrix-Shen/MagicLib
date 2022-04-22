package top.hendrixshen.magiclib.compat.minecraft.world.entity;

//#if MC <= 11701
//$$ import net.minecraft.world.level.Level;
//#endif
//#if MC <= 11605
//$$ import net.minecraft.core.BlockPos;
//#endif
//#if MC <= 11502
//$$ import net.minecraft.network.chat.Component;
//$$ import java.util.UUID;
//#endif

public interface EntityCompatApi {

    // It will cause remap fail...
    //#if MC <= 11701
    ////$$ default Level getLevel() {
    ////$$     throw new UnsupportedOperationException();
    ////$$ }
    //#endif

    //#if MC <= 11605
    //$$ default int getBlockX() {
    //$$    throw new UnsupportedOperationException();
    //$$ }

    //$$ default int getBlockY() {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default int getBlockZ() {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default float getYRot() {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default void setYRot(float f) {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default float getXRot() {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default void setXRot(float f) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ default BlockPos blockPosition() {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default boolean isOnGround() {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default void sendMessage(Component component, UUID uuid) {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default void setOnGround(boolean bl) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif

}
