package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import net.minecraft.server.packs.FilePackResources;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;

import java.io.File;

@Mixin(FilePackResources.class)
public class FilePackResourcesMixin implements PackAccessor {
    @Final
    @Shadow
    private File file;

    @Override
    public File magiclib$getFile() {
        return this.file;
    }
}
