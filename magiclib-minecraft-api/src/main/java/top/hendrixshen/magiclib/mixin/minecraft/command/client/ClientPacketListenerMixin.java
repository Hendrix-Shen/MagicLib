package top.hendrixshen.magiclib.mixin.minecraft.command.client;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.21.6
//$$ import net.minecraft.client.multiplayer.ClientSuggestionProvider;
//#else
import net.minecraft.commands.SharedSuggestionProvider;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.impl.command.client.ClientCommandInternals;

/**
 * Merges the registered client commands into the vanilla command tree, so that
 * the vanilla suggestion system can complete them. Also intercepts the packet
 * listener's chat sending methods on MC 1.19.3+, where chat messages no longer
 * pass through {@code LocalPlayer}.
 */
@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {
    //#if MC > 1.21.5
    //$$ @Shadow
    //$$ private CommandDispatcher<ClientSuggestionProvider> commands;
    //#else
    @Shadow
    private CommandDispatcher<SharedSuggestionProvider> commands;
    //#endif

    //#if MC >= 1.19.3
    //$$ @Inject(method = "sendCommand", at = @At("HEAD"), cancellable = true)
    //$$ private void onSendCommand(String string, CallbackInfo ci) {
    //$$     if (ClientCommandInternals.executeCommand(string)) {
    //$$         ci.cancel();
    //$$     }
    //$$ }
    //$$
    //$$ @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    //$$ private void onSendChat(String string, CallbackInfo ci) {
    //$$     if (ClientCommandInternals.executeChatMessage(string)) {
    //$$         ci.cancel();
    //$$     }
    //$$ }
    //#endif

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(method = "handleCommands", at = @At("RETURN"))
    private void onHandleCommands(ClientboundCommandsPacket packet, CallbackInfo ci) {
        ClientCommandInternals.addCommands((CommandDispatcher) this.commands);
    }
}
