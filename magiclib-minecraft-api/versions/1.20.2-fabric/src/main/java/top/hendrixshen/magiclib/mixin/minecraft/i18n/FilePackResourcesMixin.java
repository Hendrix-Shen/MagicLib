package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import net.minecraft.server.packs.FilePackResources;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;

import java.io.File;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.19.2  : subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.19.3 ~ mc1.20.1: subproject 1.19.3</li>
 * <li>mc1.20.2+          : subproject 1.20.2        &lt;--------</li>
 */
@Mixin(FilePackResources.class)
public abstract class FilePackResourcesMixin implements PackAccessor {
    @Final
    @Shadow
    private FilePackResources.SharedZipFileAccess zipFileAccess;

    @Override
    public File magiclib$getFile() {
        return this.zipFileAccess.file;
    }
}
