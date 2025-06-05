package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ClientboundCustomPayloadPacket.class)
public interface ClientboundCustomPayloadPacketAccessor {
    @Accessor("identifier")
    ResourceLocation magiclib$getIdentifier();

    @Accessor("data")
    FriendlyByteBuf magiclib$getData();
}
