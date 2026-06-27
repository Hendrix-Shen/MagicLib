package top.hendrixshen.magiclib.mixin.compat.minecraft.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.compat.minecraft.api.client.gui.FontCompatApi;
import top.hendrixshen.magiclib.util.MiscUtil;

@Environment(EnvType.CLIENT)
@Mixin(Font.class)
public abstract class MixinFont implements FontCompatApi {
    @Override
    public int widthCompat(Component component) {
        //#if MC > 11502
        return ((Font) MiscUtil.cast(this)).width(component);
        //#else
        //$$ return this.width(component.getString());
        //#endif
    }
}
