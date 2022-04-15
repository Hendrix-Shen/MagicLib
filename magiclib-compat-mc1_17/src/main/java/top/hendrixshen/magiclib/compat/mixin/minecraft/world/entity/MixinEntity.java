package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Entity.class)
public class MixinEntity {
    @Shadow
    public Level level;

    @Remap("method_37908")
    public Level getLevel() {
        return this.level;
    }
}
