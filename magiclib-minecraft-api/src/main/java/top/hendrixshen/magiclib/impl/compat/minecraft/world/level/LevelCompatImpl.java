package top.hendrixshen.magiclib.impl.compat.minecraft.world.level;

import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.LevelCompat;

public class LevelCompatImpl extends AbstractCompat<Level> implements LevelCompat {
    public LevelCompatImpl(@NotNull Level type) {
        super(type);
    }
}
