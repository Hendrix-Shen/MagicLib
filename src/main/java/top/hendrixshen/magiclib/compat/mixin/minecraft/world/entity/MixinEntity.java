package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.world.entity.EntityCompatApi;

//#if MC <= 11701
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import net.minecraft.world.level.Level;
//#endif
//#if MC <= 11605
//$$ import net.minecraft.core.BlockPos;
//#endif
//#if MC <= 11502
//$$ import net.minecraft.network.chat.Component;
//$$ import java.util.UUID;
//#endif

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityCompatApi {
    //#if MC <= 11701
    //$$ @Shadow
    //$$ public Level level;
    //$$ public Level getLevel() {
    //$$     return this.level;
    //$$ }
    //#endif

    //#if MC <= 11605
    //#if MC > 11502
    //$$ @Shadow
    //$$ public abstract BlockPos blockPosition();
    //#endif
    //$$ @Shadow
    //$$ public float yRot;
    //$$ @Shadow
    //$$ public float xRot;

    //$$ @Override
    //$$ public int getBlockX() {
    //$$     return this.blockPosition().getX();
    //$$ }

    //$$ @Override
    //$$ public int getBlockY() {
    //$$     return this.blockPosition().getY();
    //$$ }

    //$$ @Override
    //$$ public int getBlockZ() {
    //$$     return this.blockPosition().getZ();
    //$$ }

    //$$ @Override
    //$$ public float getYRot() {
    //$$     return this.yRot;
    //$$ }

    //$$ @Override
    //$$ public void setYRot(float f) {
    //$$     this.yRot = f;
    //$$ }

    //$$ @Override
    //$$ public float getXRot() {
    //$$     return this.xRot;
    //$$ }

    //$$ @Override
    //$$ public void setXRot(float f) {
    //$$     this.xRot = f;
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ @Shadow
    //$$ public abstract void sendMessage(Component component);
    //$$ @Shadow
    //$$ public abstract BlockPos getCommandSenderBlockPosition();
    //$$ @Shadow
    //$$ public boolean onGround;

    //$$ @Override
    //$$ public boolean isOnGround() {
    //$$     return this.onGround;
    //$$ }

    //$$ @Override
    //$$ public BlockPos blockPosition() {
    //$$     return this.getCommandSenderBlockPosition();
    //$$ }

    //$$ @Override
    //$$ public void sendMessage(Component component, UUID uuid) {
    //$$     this.sendMessage(component);
    //$$ }

    //$$ @Override
    //$$ public void setOnGround(boolean bl) {
    //$$     this.onGround = bl;
    //$$ }
    //#endif


}