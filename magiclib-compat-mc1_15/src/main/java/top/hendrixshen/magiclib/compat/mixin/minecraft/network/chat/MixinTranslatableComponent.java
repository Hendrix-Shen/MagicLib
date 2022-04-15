package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.TranslatableComponent;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.InitMethod;
import top.hendrixshen.magiclib.compat.annotation.ThisInitMethod;

@SuppressWarnings("unused")
@Mixin(TranslatableComponent.class)
public abstract class MixinTranslatableComponent {
    @InitMethod
    public void magicInit(String string) {
        magicThisInit(string);
    }

    @ThisInitMethod
    public void magicThisInit(String string, Object... objects) {
    }
}
