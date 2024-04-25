package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import net.minecraft.server.packs.AbstractPackResources;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;

import java.io.File;

@Mixin(AbstractPackResources.class)
public class AbstractResourcePackMixin implements PackAccessor {
    @Final
    @Shadow
    protected File file;

    @Override
    public File magiclib$getFile() {
        return this.file;
    }
}
