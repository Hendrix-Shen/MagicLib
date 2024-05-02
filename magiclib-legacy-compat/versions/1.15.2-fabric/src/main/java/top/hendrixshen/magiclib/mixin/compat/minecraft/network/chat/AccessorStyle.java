package top.hendrixshen.magiclib.mixin.compat.minecraft.network.chat;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.ApiStatus;
import org.spongepowered.asm.mixin.Mixin;

@ApiStatus.Internal
@Mixin(Style.class)
public interface AccessorStyle {
}
