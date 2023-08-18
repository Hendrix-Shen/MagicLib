package top.hendrixshen.magiclib.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;

import java.util.List;
import java.util.Optional;

//#if MC > 11802
import net.minecraft.network.chat.MutableComponent;
//#else
//$$ import net.minecraft.network.chat.BaseComponent;
//#endif

//#if MC > 11502
import net.minecraft.world.level.Level;
//#else
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif

public class MessageUtil {
    public static void sendMessage(CommandSourceStack source, String message) {
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(message));
    }

    public static void sendMessage(CommandSourceStack source, Component messages) {
        //#if MC > 11904
        Optional.ofNullable(source).ifPresent(sourceStack -> sourceStack.sendSuccess(() -> messages, source.getServer().getLevel(Level.OVERWORLD) != null));
        //#elseif MC > 11502
        //$$ Optional.ofNullable(source).ifPresent(sourceStack -> sourceStack.sendSuccess(messages, source.getServer().getLevel(Level.OVERWORLD) != null));
        //#else
        //$$ Optional.ofNullable(source).ifPresent(sourceStack -> sourceStack.sendSuccess(messages, source.getServer() != null && source.getServer().getLevel(DimensionType.OVERWORLD) != null));
        //#endif
    }

    public static void sendMessage(CommandSourceStack source, List<Component> messages) {
        MessageUtil.sendMessage(source, MessageUtil.insertComponent(messages));
    }

    public static void sendServerMessage(MinecraftServer server, String message) {
        MessageUtil.sendServerMessage(server, ComponentCompatApi.literal(message));
    }

    public static void sendServerMessage(MinecraftServer server, Component message) {
        Optional.of(server).ifPresent(s -> {
            MagicLibReference.getLogger().info(message.getString());
            s.getPlayerList().getPlayers().forEach(p ->
                    //#if MC > 11802
                    p.sendSystemMessage(message));
                    //#elseif MC > 11502
                    //$$ p.sendMessage(message, p.getUUID()));
                    //#else
                    //$$ p.sendMessage(message));
                    //#endif
        });
    }

    public static void sendServerMessage(MinecraftServer server, List<Component> messages) {
        MessageUtil.sendServerMessage(server, MessageUtil.insertComponent(messages));
    }

    //#if MC > 11802
    private static @NotNull MutableComponent insertComponent(@NotNull List<Component> messages) {
    //#else
    //$$ private static @NotNull BaseComponent insertComponent(@NotNull List<Component> messages) {
    //#endif
        //#if MC > 11802
        MutableComponent components = ComponentCompatApi.literal("");
        //#else
        //$$ BaseComponent components = ComponentCompatApi.literal("");
        //#endif
        messages.forEach(components::append);
        return components;
    }
}
