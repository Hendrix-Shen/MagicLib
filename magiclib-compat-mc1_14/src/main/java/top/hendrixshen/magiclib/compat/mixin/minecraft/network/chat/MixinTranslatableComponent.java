package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.network.chat.*;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.InitMethod;
import top.hendrixshen.magiclib.compat.annotation.Remap;
import top.hendrixshen.magiclib.compat.annotation.ThisInitMethod;

@SuppressWarnings("unused")
@Mixin(TranslatableComponent.class)
public abstract class MixinTranslatableComponent extends BaseComponent implements ContextAwareComponent {
    @InitMethod
    public void magicInit(String string) {
        magicThisInit(string);
    }

    @ThisInitMethod
    public void magicThisInit(String string, Object... objects) {
    }

}
