package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import net.minecraft.server.packs.PathPackResources;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;

import java.io.File;
import java.nio.file.Path;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.2  : subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.19.3 ~ mc1.20.1: subproject 1.19.3        &lt;--------</li>
 * <li>mc1.20.2+          : subproject 1.20.2</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(PathPackResources.class)
public abstract class PathPackResourcesMixin implements PackAccessor {
    @Final
    @Shadow
    private Path root;

    @Override
    public File magiclib$getFile() {
        return this.root.toFile();
    }
}
