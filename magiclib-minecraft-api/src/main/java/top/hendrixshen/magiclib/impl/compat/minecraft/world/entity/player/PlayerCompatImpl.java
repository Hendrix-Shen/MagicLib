package top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.player;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.EntityCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.player.PlayerCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.entity.EntityCompatImpl;

public class PlayerCompatImpl extends EntityCompatImpl implements PlayerCompat {
    public PlayerCompatImpl(@NotNull Player type) {
        super(type);
    }

    @Override
    public @NotNull Player get() {
        return (Player) super.get();
    }

    @Override
    public Inventory getInventory() {
        //#if MC > 11605
        //$$ return this.get().getInventory();
        //#else
        return this.get().inventory;
        //#endif
    }

    @Override
    public Abilities getAbilities() {
        //#if MC > 11605
        //$$ return this.get().getAbilities();
        //#else
        return this.get().abilities;
        //#endif
    }
}
