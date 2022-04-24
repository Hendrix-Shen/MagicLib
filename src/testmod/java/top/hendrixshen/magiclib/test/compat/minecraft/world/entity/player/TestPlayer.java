package top.hendrixshen.magiclib.test.compat.minecraft.world.entity.player;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.compat.minecraft.UtilCompatApi;

public class TestPlayer {
    public static void test(Level level) {

        //#if MC > 11502
        Player player = new Player(level, BlockPos.ZERO, 0, new GameProfile(UtilCompatApi.NIL_UUID, "test")) {
            //#else
            //$$ Player player = new Player(level, new GameProfile(UtilCompatApi.NIL_UUID, "test")) {
            //#endif
            @Override
            public boolean isSpectator() {
                return false;
            }

            @Override
            public boolean isCreative() {
                return false;
            }
        };
        player.getInventoryCompat();
        player.getInventory();
    }
}
