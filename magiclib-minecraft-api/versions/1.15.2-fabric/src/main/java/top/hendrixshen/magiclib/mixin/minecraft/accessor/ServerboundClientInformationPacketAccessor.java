package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(ServerboundClientInformationPacket.class)
public interface ServerboundClientInformationPacketAccessor {
}
