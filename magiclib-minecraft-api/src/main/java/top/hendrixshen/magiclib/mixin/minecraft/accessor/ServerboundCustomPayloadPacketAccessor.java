package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ServerboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundCustomPayloadPacket.class)
public interface ServerboundCustomPayloadPacketAccessor {
    @Accessor("identifier")
    ResourceLocation magiclib$getIdentifier();

    @Accessor("data")
    FriendlyByteBuf magiclib$getData();
}
