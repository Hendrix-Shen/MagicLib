package top.hendrixshen.magiclib.api.compat.minecraft.world.entity.player;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.EntityCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.player.PlayerCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface PlayerCompat extends EntityCompat {
    static @NotNull PlayerCompat of(@NotNull Player player) {
        return new PlayerCompatImpl(player);
    }

    @Override
    @NotNull Player get();

    Inventory getInventory();

    Abilities getAbilities();
}
