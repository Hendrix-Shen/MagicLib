package top.hendrixshen.magiclib.impl.compat.minecraft.server.level;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.server.level.ServerPlayerCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.player.PlayerCompatImpl;

public class ServerPlayerCompatImpl extends PlayerCompatImpl implements ServerPlayerCompat {
    public ServerPlayerCompatImpl(@NotNull ServerPlayer type) {
        super(type);
    }

    @Override
    public @NotNull ServerPlayer get() {
        return (ServerPlayer) super.get();
    }

    //#if MC > 12101
    //$$ @Override
    //$$ public void sendSystemMessage(@NotNull Component component) {
    //$$     this.get().sendSystemMessage(component);
    //$$ }
    //#endif
}
