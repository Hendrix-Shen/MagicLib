package top.hendrixshen.magiclib.impl.command.client;

import lombok.AllArgsConstructor;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.15
import net.minecraft.client.multiplayer.ClientLevel;
//#else
//$$ import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.MagicCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;

/**
 * A {@link MagicCommandSource} that delegates to the vanilla client.
 */
@AllArgsConstructor
public class MagicCommandSourceImpl implements MagicCommandSource {
    private final Minecraft client;

    @Override
    public void sendSuccess(Component message) {
        //#if MC >= 26.2
        //$$ this.client.gui.hud.getChat().addClientSystemMessage(message);
        //#elseif MC >= 26.1
        //$$ this.client.gui.getChat().addClientSystemMessage(message);
        //#else
        this.client.gui.getChat().addMessage(message);
        //#endif
    }

    @Override
    public void sendFailure(Component message) {
        //#if MC >= 26.2
        //$$ this.client.gui.hud.getChat().addClientSystemMessage(
        //$$         MutableComponentCompat.of(ComponentCompat.literal("")).append(message).withStyle(ChatFormatting.RED).get());
        //#elseif MC >= 26.1
        //$$ this.client.gui.getChat().addClientSystemMessage(
        //$$         MutableComponentCompat.of(ComponentCompat.literal("")).append(message).withStyle(ChatFormatting.RED).get());
        //#else
        this.client.gui.getChat().addMessage(
                MutableComponentCompat.of(ComponentCompat.literal("")).append(message).withStyle(ChatFormatting.RED).get());
        //#endif
    }

    @Override
    public Minecraft getClient() {
        return this.client;
    }

    @Override
    public LocalPlayer getPlayer() {
        return this.client.player;
    }

    @Override
    //#if MC >= 1.15
    public ClientLevel getLevel() {
        return this.client.level;
    }
    //#else
    //$$ public MultiPlayerLevel getLevel() {
    //$$     return this.client.level;
    //$$ }
    //#endif
}
