package top.hendrixshen.magiclib.compat.mixin.minecraft.world.level;

import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.level.LevelCompatApi;

@Mixin(Level.class)
public abstract class MixinLevel implements LevelCompatApi {
    @Shadow
    public abstract ResourceKey<Level> dimension();

    @Override
    public ResourceLocation getDimensionLocation() {
        return this.dimension().location();
    }
}
