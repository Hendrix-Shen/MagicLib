package top.hendrixshen.magiclib.compat.mixin.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.Dimension;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.level.LevelCompatApi;

@Mixin(Level.class)
public class MixinLevel implements LevelCompatApi {
    @Shadow
    @Final
    public Dimension dimension;

    @Override
    public ResourceLocation getDimensionLocation() {
        return DimensionType.getName(this.dimension.getType());
    }
}
