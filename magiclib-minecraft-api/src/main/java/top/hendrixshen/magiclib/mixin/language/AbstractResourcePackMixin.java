package top.hendrixshen.magiclib.mixin.language;

import net.minecraft.server.packs.AbstractResourcePack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.api.fake.language.PackAccessor;

import java.io.File;

@Mixin(AbstractResourcePack.class)
public class AbstractResourcePackMixin implements PackAccessor {
    @Final
    @Shadow
    protected File file;

    @Override
    public File magiclib$getFile() {
        return this.file;
    }
}
