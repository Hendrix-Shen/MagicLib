package top.hendrixshen.magiclib.compat.minecraft.mixin.network.chat;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;

@ApiStatus.Internal
@Mixin(Style.class)
public interface AccessorStyle {
}
