package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;

import org.spongepowered.asm.mixin.Mixin;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15  : subproject 1.15.2 [dummy]        &lt;--------</li>
 * <li>mc1.15 ~ mc1.20.1: subproject 1.16.5 (main project)</li>
 * <li>mc1.20.2+        : subproject 1.20.2 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(ServerboundClientInformationPacket.class)
public interface ServerboundClientInformationPacketAccessor {
}
