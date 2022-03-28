package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.HoverEvent;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.InnerClass;

//@InnerClass()
@Mixin(HoverEvent.class)
public class MixinHoverEvent {
}
