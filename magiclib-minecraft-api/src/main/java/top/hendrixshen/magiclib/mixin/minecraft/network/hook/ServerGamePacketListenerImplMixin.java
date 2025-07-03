package top.hendrixshen.magiclib.mixin.minecraft.network.hook;

import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12002
//$$ import net.minecraft.server.network.ServerCommonPacketListenerImpl;
//#else
import net.minecraft.network.FriendlyByteBuf;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.api.network.packet.PacketType;
import top.hendrixshen.magiclib.api.network.packet.ServerboundPacketHandler;
import top.hendrixshen.magiclib.impl.network.packet.MagicCustomPayload;
import top.hendrixshen.magiclib.impl.network.packet.MagicPacketRegistry;
import top.hendrixshen.magiclib.impl.network.packet.PacketHandlerContextImpl;
import top.hendrixshen.magiclib.impl.network.packet.RegistryEntry;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12002
import top.hendrixshen.magiclib.mixin.minecraft.accessor.ServerboundCustomPayloadPacketAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;
//#endif
// CHECKSTYLE.ON: ImportOrder

@Mixin(
        //#if MC >= 12002
        //$$ ServerCommonPacketListenerImpl.class
        //#else
        ServerGamePacketListenerImpl.class
        //#endif
)
public abstract class ServerGamePacketListenerImplMixin {
    @SuppressWarnings({
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            "rawtypes",
            "unchecked"
            //#if MC >= 12002
            //$$ , "ConstantConditions"
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    })
    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void handleServerboundCustomPayload(ServerboundCustomPayloadPacket packet, CallbackInfo ci) {
        //#if MC >= 12005
        //$$ ResourceLocation identifier = packet.payload().type().id();
        //#elseif MC >= 12002
        //$$ ResourceLocation identifier = packet.payload().id();
        //#else
        ResourceLocation identifier = ((ServerboundCustomPayloadPacketAccessor) packet).magiclib$getIdentifier();
        //#endif
        PacketType type = PacketType.of(identifier);
        RegistryEntry<ServerboundPacketHandler<?>, ?> entry = MagicPacketRegistry.SERVERBOUND_GAME.getEntry(type);

        if (entry == null) {
            return;
        }

        //#if MC >= 12002
        //$$ if (packet.payload() instanceof MagicCustomPayload mcp && (Object) this instanceof ServerGamePacketListenerImpl self) {
        //$$     ServerGamePacketListenerImplMixin.magiclib$handleCustomPayload(mcp, (ServerboundPacketHandler) entry.getHandler(), self);
        //$$     ci.cancel();
        //$$ }
        //#else
        FriendlyByteBuf buf = ((ServerboundCustomPayloadPacketAccessor) packet).magiclib$getData();
        MagicCustomPayload<?> payload = new MagicCustomPayload<>(type, entry.getCodec(), buf);
        ServerGamePacketListenerImplMixin.magiclib$handleCustomPayload(payload, (ServerboundPacketHandler) entry.getHandler(), MiscUtil.cast(this));
        ci.cancel();
        //#endif
    }

    @Unique
    private static <P> void magiclib$handleCustomPayload(MagicCustomPayload<P> payload, ServerboundPacketHandler<P> handler, ServerGamePacketListenerImpl self) {
        handler.handle(payload.getPacket(), new PacketHandlerContextImpl.Serverbound(self));
    }
}
