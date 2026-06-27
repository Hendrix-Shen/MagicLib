package top.hendrixshen.magiclib.compat.minecraft.api.client.gui;

import net.minecraft.network.chat.Component;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Environment(EnvType.CLIENT)
public interface FontCompatApi {
    default int widthCompat(Component component) {
        throw new UnImplCompatApiException();
    }
}
