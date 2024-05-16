package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.List;
import java.util.stream.Collectors;

//#if MC > 11502
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.Level;
//#else
//$$ import net.minecraft.network.chat.BaseComponent;
//$$ import net.minecraft.world.level.dimension.DimensionType;
//#endif

public class MessageUtil {
    public static void sendMessage(CommandSourceStack source, String message) {
        MessageUtil.sendMessage(source, ComponentCompat.literal(message));
    }

    public static void sendMessage(CommandSourceStack source, ComponentCompat message) {
        MessageUtil.sendMessage(source, message.get());
    }

    public static void sendMessage(CommandSourceStack source, Component messages) {
        ValueContainer.ofNullable(source).ifPresent(commandSourceStack ->
                commandSourceStack.sendSuccess(
                        //#if MC > 11904
                        //$$ () -> messages,
                        //#else
                        messages,
                        //#endif
                        //#if MC > 11904
                        //$$ source.getServer().getLevel(Level.OVERWORLD) != null
                        //#elseif MC > 11502
                        source.getServer().getLevel(Level.OVERWORLD) != null
                        //#else
                        //$$ source.getServer() != null && source.getServer().getLevel(DimensionType.OVERWORLD) != null
                        //#endif
                ));
    }

    public static void sendMessage(CommandSourceStack source, List<Component> messages) {
        MessageUtil.sendMessage(source, MessageUtil.insertComponent(messages));
    }

    public static void sendServerMessage(MinecraftServer server, String message) {
        MessageUtil.sendServerMessage(server, ComponentCompat.literal(message));
    }

    public static void sendServerMessage(MinecraftServer server, ComponentCompat message) {
        MessageUtil.sendServerMessage(server, message.get());
    }

    public static void sendServerMessage(MinecraftServer server, Component message) {
        ValueContainer.of(server).ifPresent(s -> {
            MagicLib.getLogger().info(message.getString());
            s.getPlayerList().getPlayers().forEach(p ->
                    //#if MC > 11802
                    //$$ p.sendSystemMessage(message));
                    //#elseif MC > 11502
                    p.sendMessage(message, p.getUUID()));
            //#else
            //$$ p.sendMessage(message));
            //#endif
        });
    }

    public static void sendServerMessage(MinecraftServer server, @NotNull List<Component> messages) {
        MessageUtil.sendServerMessage(server, MessageUtil.insertComponent(messages));
    }

    private static @NotNull Component insertComponent(@NotNull List<Component> messages) {
        return MessageUtil.insertComponentCompat(messages.stream()
                //#if MC > 11502
                .filter(c -> c instanceof MutableComponent)
                .map(c -> (MutableComponent) c)
                //#else
                //$$ .filter(c -> c instanceof BaseComponent)
                //$$ .map(c -> (BaseComponent) c)
                //#endif
                .map(MutableComponentCompat::of)
                .collect(Collectors.toList()));
    }

    private static @NotNull Component insertComponentCompat(@NotNull List<MutableComponentCompat> messages) {
        MutableComponentCompat component = ComponentCompat.literal("");
        messages.forEach(component::append);
        return component.get();
    }
}
