package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.entity.EntityCompatApi;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityCompatApi {

    //#if MC > 11701
    @Shadow
    public abstract Level getLevel();
    //#else
    //$$ @Shadow
    //$$ public Level level;
    //#endif

    @Override
    public Level getLevelCompat() {
        //#if MC > 11701
        return this.getLevel();
        //#else
        //$$ return this.level;
        //#endif
    }

    //#if MC > 11605
    @Shadow
    public abstract float getYRot();

    @Shadow
    public abstract void setYRot(float f);

    @Shadow
    public abstract float getXRot();

    @Shadow
    public abstract void setXRot(float f);
    //#else
    //$$ @Shadow
    //$$ public float yRot;
    //$$ @Shadow
    //$$ public float xRot;
    //#endif

    @Override
    public float getYRotCompat() {
        //#if MC > 11605
        return this.getYRot();
        //#else
        //$$ return this.yRot;
        //#endif
    }

    @Override
    public void setYRotCompat(float yRot) {
        //#if MC > 11605
        this.setYRot(yRot);
        //#else
        //$$ this.yRot = yRot;
        //#endif
    }

    @Override
    public float getXRotCompat() {
        //#if MC > 11605
        return this.getXRot();
        //#else
        //$$ return this.xRot;
        //#endif
    }

    @Override
    public void setXRotCompat(float xRot) {
        //#if MC > 11605
        this.setXRot(xRot);
        //#else
        //$$ this.xRot = xRot;
        //#endif
    }


    //#if MC >11502
    @Shadow
    public abstract BlockPos blockPosition();

    @Shadow
    public abstract boolean isOnGround();

    @Shadow
    public abstract void setOnGround(boolean onGround);

    @Shadow
    public abstract void sendMessage(Component component, UUID uUID);


    //#else

    //$$ @Shadow
    //$$ public abstract BlockPos getCommandSenderBlockPosition();

    //$$ @Shadow
    //$$ public abstract void sendMessage(Component component);

    //$$ @Shadow
    //$$ public boolean onGround;

    //#endif

    @Override
    public BlockPos blockPositionCompat() {
        //#if MC > 11502
        return this.blockPosition();
        //#else
        //$$ return this.getCommandSenderBlockPosition();
        //#endif
    }


    @Override
    public boolean isOnGroundCompat() {
        //#if MC > 11502
        return this.isOnGround();
        //#else
        //$$ return this.onGround;
        //#endif
    }

    @Override
    public void setOnGroundCompat(boolean onGround) {
        //#if MC > 11502
        this.setOnGround(onGround);
        //#else
        //$$ this.onGround = onGround;
        //#endif
    }

    @Override
    public void sendMessageCompat(Component component, UUID uuid) {
        //#if MC > 11502
        this.sendMessage(component, uuid);
        //#else
        //$$ this.sendMessage(component);
        //#endif
    }

}