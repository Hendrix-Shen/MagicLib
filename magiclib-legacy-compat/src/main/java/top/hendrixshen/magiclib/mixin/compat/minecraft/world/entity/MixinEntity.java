package top.hendrixshen.magiclib.mixin.compat.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.world.entity.EntityCompatApi;

//#if MC > 12101
//$$ import net.minecraft.server.level.ServerPlayer;
//$$ import top.hendrixshen.magiclib.util.MiscUtil;
//#endif

//#if MC > 11502
//#if MC < 11900
import top.hendrixshen.magiclib.compat.minecraft.api.UtilCompatApi;
import java.util.UUID;
//#endif
//#else
//$$ import net.minecraft.world.phys.Vec3;
//#endif

@Mixin(Entity.class)
public abstract class MixinEntity implements EntityCompatApi {
    //#if MC > 12004
    //$$ @Shadow
    //$$ public abstract float maxUpStep();
    //#elseif MC > 11903
    //$$ @Shadow
    //$$ private float maxUpStep;
    //$$
    //$$ @Shadow
    //$$ public abstract void setMaxUpStep(float maxUpStep);
    //#else
    @Shadow
    public float maxUpStep;
    //#endif

    //#if MC > 11404
    @Shadow
    public abstract double getX();

    @Shadow
    public abstract double getY();

    @Shadow
    public abstract double getZ();
    //#else
    //$$ @Shadow
    //$$ public double x;
    //$$ @Shadow
    //$$ public double y;
    //$$ @Shadow
    //$$ public double z;
    //#endif

    @Override
    public double getXCompat() {
        //#if MC > 11404
        return this.getX();
        //#else
        //$$ return this.x;
        //#endif
    }

    @Override
    public double getYCompat() {
        //#if MC > 11404
        return this.getY();
        //#else
        //$$ return this.y;
        //#endif
    }

    @Override
    public double getZCompat() {
        //#if MC > 11404
        return this.getZ();
        //#else
        //$$ return this.z;
        //#endif
    }

    //#if MC < 12102
    //#if MC > 11802
    //$$ @Shadow
    //$$ public abstract void sendSystemMessage(Component component);
    //#elseif MC > 11502
    @Shadow
    public abstract void sendMessage(Component component, UUID uuid);

    //#else
    //$$ @Shadow
    //$$ public abstract void sendMessage(Component component);
    //#endif
    //#endif

    @Override
    public void sendSystemMessageCompat(Component component) {
        //#if MC > 12101
        //$$ if (MiscUtil.cast(this) instanceof ServerPlayer) {
        //$$     ((ServerPlayer) MiscUtil.cast(this)).sendSystemMessage(component);
        //$$ }
        //#elseif MC > 11802
        //$$ this.sendSystemMessage(component);
        //#elseif MC > 11502
        this.sendMessage(component, UtilCompatApi.NIL_UUID);
        //#else
        //$$ this.sendMessage(component);
        //#endif
    }

    //#if MC > 11701
    //$$ @Shadow
    //$$ public abstract Level getLevel();
    //#else
    @Shadow
    public Level level;
    //#endif

    @Override
    public Level getLevelCompat() {
        //#if MC > 11701
        //$$ return this.getLevel();
        //#else
        return this.level;
        //#endif
    }

    //#if MC > 11605
    //$$ @Shadow
    //$$ public abstract float getYRot();
    //$$
    //$$ @Shadow
    //$$ public abstract void setYRot(float f);
    //$$
    //$$ @Shadow
    //$$ public abstract float getXRot();
    //$$
    //$$ @Shadow
    //$$ public abstract void setXRot(float f);
    //#else
    @Shadow
    public float yRot;
    @Shadow
    public float xRot;
    //#endif

    @Override
    public float getYRotCompat() {
        //#if MC > 11605
        //$$ return this.getYRot();
        //#else
        return this.yRot;
        //#endif
    }

    @Override
    public void setYRotCompat(float yRot) {
        //#if MC > 11605
        //$$ this.setYRot(yRot);
        //#else
        this.yRot = yRot;
        //#endif
    }

    @Override
    public float getXRotCompat() {
        //#if MC > 11605
        //$$ return this.getXRot();
        //#else
        return this.xRot;
        //#endif
    }

    @Override
    public void setXRotCompat(float xRot) {
        //#if MC > 11605
        //$$ this.setXRot(xRot);
        //#else
        this.xRot = xRot;
        //#endif
    }

    //#if MC >11502
    @Shadow
    public abstract BlockPos blockPosition();

    @Shadow
    public abstract boolean isOnGround();

    @Shadow
    public abstract void setOnGround(boolean onGround);
    //#else
    //$$
    //$$ @Shadow
    //$$ public abstract BlockPos getCommandSenderBlockPosition();
    //$$
    //$$ @Shadow
    //$$ public boolean onGround;
    //$$
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
    public float maxUpStepCompat() {
        //#if MC > 12004
        //$$ return this.maxUpStep();
        //#else
        return this.maxUpStep;
        //#endif
    }

    @Override
    public void setMaxUpStepCompat(float maxUpStep) {
        //#if MC > 12004
        //$$ throw new UnsupportedOperationException();
        //#elseif MC > 11903
        //$$ this.setMaxUpStep(maxUpStep);
        //#else
        this.maxUpStep = maxUpStep;
        //#endif
    }
}