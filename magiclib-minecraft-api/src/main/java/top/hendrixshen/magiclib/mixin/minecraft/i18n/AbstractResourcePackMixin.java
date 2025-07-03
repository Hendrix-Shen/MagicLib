package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import net.minecraft.server.packs.AbstractPackResources;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import top.hendrixshen.magiclib.api.fake.i18n.PackAccessor;

import java.io.File;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19.3+        : subproject 1.19.3 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(AbstractPackResources.class)
public abstract class AbstractResourcePackMixin implements PackAccessor {
    @Final
    @Shadow
    protected File file;

    @Override
    public File magiclib$getFile() {
        return this.file;
    }
}
