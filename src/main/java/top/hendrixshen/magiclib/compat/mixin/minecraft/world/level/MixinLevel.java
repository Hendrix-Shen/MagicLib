package top.hendrixshen.magiclib.compat.mixin.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.level.LevelCompatApi;

//#if MC > 11502
import net.minecraft.resources.ResourceKey;
//#else
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import net.minecraft.world.level.dimension.Dimension;
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif
@Mixin(Level.class)
public abstract class MixinLevel implements LevelCompatApi {
    //#if MC > 11502
    @Shadow
    public abstract ResourceKey<Level> dimension();
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ public Dimension dimension;
    //#endif

    @Override
    public ResourceLocation getDimensionLocation() {
        //#if MC > 11502
        return this.dimension().location();
        //#else
        //$$ return DimensionType.getName(this.dimension.getType());
        //#endif
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public int getMinBuildHeightCompat() {
        //#if MC > 11605
        return ((Level) (Object) this).getMinBuildHeight();
        //#else
        //$$ return 0;
        //#endif
    }
}

