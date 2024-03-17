package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;

import java.util.Locale;

//#if MC > 12001
//$$ import net.minecraft.server.level.ClientInformation;
//#else
import net.minecraft.network.protocol.game.ServerboundClientInformationPacket;
//#endif

//#if MC > 11502 && MC < 11800
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.ServerboundClientInformationPacketAccessor;
//#endif

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin implements ServerPlayerLanguage {
    @Unique
    private String magiclib$language = "en_us";

    @Inject(
            method = "updateOptions",
            at = @At(
                    value = "HEAD"
            )
    )
    private void preUpdateOptions(
            //#if MC > 12001
            //$$ @NotNull ClientInformation clientInformation,
            //#else
            @NotNull ServerboundClientInformationPacket serverboundClientInformationPacket,
            //#endif
            CallbackInfo ci
    ) {
        //#if MC > 12001
        //$$ this.magiclib$language = clientInformation.language().toLowerCase(Locale.ROOT);
        //#elseif MC > 11701
        //$$ this.magiclib$language = serverboundClientInformationPacket.language().toLowerCase(Locale.ROOT);
        //#elseif MC > 11502
        //$$ this.magiclib$language = ((ServerboundClientInformationPacketAccessor) serverboundClientInformationPacket)
        //$$         .magiclib$getLanguage().toLowerCase(Locale.ROOT);
        //#else
        this.magiclib$language = serverboundClientInformationPacket.getLanguage().toLowerCase(Locale.ROOT);
        //#endif
    }

    @Override
    public String magicLib$getLanguage() {
        return this.magiclib$language;
    }
}
