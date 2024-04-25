package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ServerboundClientInformationPacket.class)
public interface ServerboundClientInformationPacketAccessor {
    @Accessor("language")
    String magiclib$getLanguage();
}
