package top.hendrixshen.magiclib.compat.mixin.minecraft.client.gui.components;

import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.InitMethod;
import top.hendrixshen.magiclib.compat.annotation.ThisInitMethod;

@SuppressWarnings("unused")
@Mixin(AbstractButton.class)
public class MixinAbstractButton {

    @InitMethod
    public void magicInit(int i, int j, int k, int l, Component component) {
        magicThisInit(i, j, k, l, component.getColoredString());
    }

    @ThisInitMethod
    public void magicThisInit(int i, int j, int k, int l, String string) {
    }
}
