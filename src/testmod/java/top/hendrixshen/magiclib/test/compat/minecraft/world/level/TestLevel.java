package top.hendrixshen.magiclib.test.compat.minecraft.world.level;

import net.minecraft.world.level.Level;

public class TestLevel {

    public static void test(Level level) {
        level.getDimensionLocation();
        level.getMinBuildHeightCompat();
        level.getMinBuildHeight();
    }
}
