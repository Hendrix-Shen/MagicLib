package top.hendrixshen.magiclib.compat.mixin.minecraft.world.entity;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import java.util.UUID;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow
    public abstract void sendMessage(Component component);

    @Remap("method_9203")
    public void sendMessage(Component component, UUID uuid) {
        this.sendMessage(component);
    }
}
