package top.hendrixshen.magiclib.impl.compat.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.LevelCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.dimension.DimensionWrapper;

public class LevelCompatImpl extends AbstractCompat<Level> implements LevelCompat {
    public LevelCompatImpl(@NotNull Level type) {
        super(type);
    }

    @Override
    public ResourceLocation getDimensionLocation() {
        return DimensionWrapper.of(this.get()).getResourceLocation();
    }

    @Override
    public int getMinBuildHeight() {
        //#if MC > 11605
        //$$ return this.get().getMinBuildHeight();
        //#else
        return 0;
        //#endif
    }
}
