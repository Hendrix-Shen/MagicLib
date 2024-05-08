package top.hendrixshen.magiclib.compat.minecraft.api.world.entity.player;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface PlayerCompatApi {
    default Inventory getInventoryCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11605
    default Inventory getInventory() {
        return this.getInventoryCompat();
    }
    //#endif

    default Abilities getAbilitiesCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11605
    default Abilities getAbilities() {
        return this.getAbilitiesCompat();
    }
    //#endif
}
