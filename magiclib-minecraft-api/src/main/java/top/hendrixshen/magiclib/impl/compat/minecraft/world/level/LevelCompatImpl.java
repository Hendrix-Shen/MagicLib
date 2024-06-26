package top.hendrixshen.magiclib.impl.compat.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.LevelCompat;

//#if MC < 11600
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif

public class LevelCompatImpl extends AbstractCompat<Level> implements LevelCompat {
    public LevelCompatImpl(@NotNull Level type) {
        super(type);
    }

    @Override
    public ResourceLocation getDimensionLocation() {
        //#if MC > 11502
        return this.get().dimension().location();
        //#else
        //$$ return DimensionType.getName(this.get().dimension.getType());
        //#endif
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
