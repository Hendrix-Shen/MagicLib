package top.hendrixshen.magiclib.impl.compat.minecraft.world.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.chat.network.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.EntityCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.LevelCompat;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

//#if MC > 11502 && MC < 11900
import top.hendrixshen.magiclib.api.compat.minecraft.UtilCompat;
//#endif

public class EntityCompatImpl extends AbstractCompat<Entity> implements EntityCompat {
    public EntityCompatImpl(@NotNull Entity type) {
        super(type);
    }

    @Override
    public LevelCompat getLevel() {
        return ValueContainer.ofNullable(
                //#if MC > 11904
                //$$ this.get().level()
                //#else
                this.get().level
                //#endif
        ).map(LevelCompat::of)
                .orElse(null);
    }

    @Override
    public double getX() {
        //#if MC > 11404
        return this.get().getX();
        //#else
        //$$ return this.get().x;
        //#endif
    }

    @Override
    public double getY() {
        //#if MC > 11404
        return this.get().getY();
        //#else
        //$$ return this.get().y;
        //#endif
    }

    @Override
    public double getZ() {
        //#if MC > 11404
        return this.get().getZ();
        //#else
        //$$ return this.get().z;
        //#endif
    }

    @Override
    public int getBlockX() {
        return this.getBlockPosition().getX();
    }

    @Override
    public int getBlockY() {
        return this.getBlockPosition().getY();
    }

    @Override
    public int getBlockZ() {
        return this.getBlockPosition().getZ();
    }

    @Override
    public float getYRot() {
        //#if MC > 11605
        return this.get().getYRot();
        //#else
        //$$ return this.get().yRot;
        //#endif
    }

    @Override
    public void setYRot(float yRot) {
        //#if MC > 11605
        this.get().setYRot(yRot);
        //#else
        //$$ this.get().yRot = yRot;
        //#endif
    }

    @Override
    public float getXRot() {
        //#if MC > 11605
        return this.get().getXRot();
        //#else
        //$$ return this.get().xRot;
        //#endif
    }

    @Override
    public void setXRot(float xRot) {
        //#if MC > 11605
        this.get().setXRot(xRot);
        //#else
        //$$ this.get().xRot = xRot;
        //#endif
    }

    @Override
    public BlockPos getBlockPosition() {
        //#if MC > 11502
        return this.get().blockPosition();
        //#else
        //$$ return this.get().getCommandSenderBlockPosition();
        //#endif
    }

    @Override
    public boolean isOnGround() {
        //#if MC > 11502
        return this.get().isOnGround();
        //#else
        //$$ return this.get().onGround;
        //#endif
    }

    @Override
    public void setOnGround(boolean onGround) {
        //#if MC > 11502
        this.get().setOnGround(onGround);
        //#else
        //$$ this.get().onGround = onGround;
        //#endif
    }

    @Override
    public void sendSystemMessage(@NotNull ComponentCompat component) {
        //#if MC > 11802
        //$$ this.get().sendSystemMessage(component.get());
        //#elseif MC > 11502
        this.get().sendMessage(component.get(), UtilCompat.NIL_UUID);
        //#else
        //$$ this.get().sendMessage(component.get());
        //#endif
    }

    @Override
    public float getMaxUpStep() {
        //#if MC > 11903
        //$$ return this.get().maxUpStep();
        //#else
        return this.get().maxUpStep;
        //#endif
    }

    @Override
    public void setMaxUpStep(float maxUpStep) {
        //#if MC > 12004
        //$$ throw new UnsupportedOperationException();
        //#elseif MC > 11903
        //$$ this.get().setMaxUpStep(maxUpStep);
        //#else
        this.get().maxUpStep = maxUpStep;
        //#endif
    }
}
