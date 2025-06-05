package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.Minecraft;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12002
//$$ import net.minecraft.client.multiplayer.ClientCommonPacketListenerImpl;
//#else
import net.minecraft.client.multiplayer.ClientPacketListener;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
        //#if MC >= 12002
        //$$ ClientCommonPacketListenerImpl.class
        //#else
        ClientPacketListener.class
        //#endif
)
public interface ClientPacketListenerAccessor {
    @Accessor("minecraft")
    Minecraft magiclib$getMinecraft();
}
