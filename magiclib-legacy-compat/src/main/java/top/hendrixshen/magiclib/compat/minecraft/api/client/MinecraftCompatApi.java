package top.hendrixshen.magiclib.compat.minecraft.api.client;

import com.mojang.blaze3d.platform.Window;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface MinecraftCompatApi {
    default Window getWindowCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11500
    //$$ default Window getWindow() {
    //$$     return this.getWindowCompat();
    //$$ }
    //#endif
}
