package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCustomPayloadPacket;
import net.minecraft.resources.ResourceLocation;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.20.1: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.20.2+        : subproject 1.20.2 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(ClientboundCustomPayloadPacket.class)
public interface ClientboundCustomPayloadPacketAccessor {
    @Accessor("identifier")
    ResourceLocation magiclib$getIdentifier();

    @Accessor("data")
    FriendlyByteBuf magiclib$getData();
}
