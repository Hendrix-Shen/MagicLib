package top.hendrixshen.magiclib.api.compat.minecraft.server.level;

import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.player.PlayerCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.server.level.ServerPlayerCompatImpl;

public interface ServerPlayerCompat extends PlayerCompat {
    static @NotNull ServerPlayerCompat of(@NotNull ServerPlayer serverPlayer) {
        return new ServerPlayerCompatImpl(serverPlayer);
    }
}
