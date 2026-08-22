package top.hendrixshen.magiclib.mixin.minecraft.command.client;

import net.minecraft.client.player.LocalPlayer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.impl.command.client.ClientCommandInternals;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.19
//$$ import net.minecraft.network.chat.Component;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Intercepts the LocalPlayer's chat messages, so that client-side commands
 * are executed on the client instead of being sent to the server.
 *
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19.3+        : subproject 1.19.3 [dummy]</li>
 */
@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin {
    //#if MC >= 1.19
    //$$ @Inject(method = "commandSigned", at = @At("HEAD"), cancellable = true)
    //$$ private void onCommandSigned(String string, Component component, CallbackInfo ci) {
    //$$     if (ClientCommandInternals.executeCommand(string)) {
    //$$         ci.cancel();
    //$$     }
    //$$ }
    //#else
    @Inject(method = "chat", at = @At("HEAD"), cancellable = true)
    private void onChat(String message, CallbackInfo ci) {
        if (ClientCommandInternals.executeChatMessage(message)) {
            ci.cancel();
        }
    }
    //#endif
}
