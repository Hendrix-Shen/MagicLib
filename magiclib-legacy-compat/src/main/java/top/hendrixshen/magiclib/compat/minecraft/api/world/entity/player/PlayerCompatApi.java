package top.hendrixshen.magiclib.compat.minecraft.api.world.entity.player;

import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface PlayerCompatApi {
    default Inventory getInventoryCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11700
    default Inventory getInventory() {
        return this.getInventoryCompat();
    }
    //#endif

    default Abilities getAbilitiesCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11700
    default Abilities getAbilities() {
        return this.getAbilitiesCompat();
    }
    //#endif
}
