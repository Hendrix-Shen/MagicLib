package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.server.MinecraftServer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12002
//$$ import net.minecraft.server.network.ServerCommonPacketListenerImpl;
//#else
import net.minecraft.server.network.ServerGamePacketListenerImpl;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(
        //#if MC >= 12002
        //$$ ServerCommonPacketListenerImpl.class
        //#else
        ServerGamePacketListenerImpl.class
        //#endif
)
public interface ServerGamePacketListenerImplAccessor {
    @Accessor("server")
    MinecraftServer magiclib$getServer();
}
