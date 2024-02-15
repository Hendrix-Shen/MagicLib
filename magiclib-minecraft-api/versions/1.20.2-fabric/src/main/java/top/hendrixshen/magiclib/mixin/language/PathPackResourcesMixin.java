package top.hendrixshen.magiclib.mixin.language;

import net.minecraft.server.packs.PathPackResources;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.api.fake.language.PackAccessor;

import java.io.File;
import java.nio.file.Path;

@Mixin(PathPackResources.class)
public class PathPackResourcesMixin implements PackAccessor {
    @Final
    @Shadow
    private Path root;

    @Override
    public File magiclib$getFile() {
        return this.root.toFile();
    }
}
