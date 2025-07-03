package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.resources.PackResourcesAdapterV4;
import net.minecraft.server.packs.PackResources;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15  : subproject 1.14.4 [dummy]</li>
 * <li>mc1.16 ~ mc1.19.2: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19.3+        : subproject 1.19.3 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(PackResourcesAdapterV4.class)
public interface PackResourcesAdapterV4Accessor {
    @Accessor("pack")
    PackResources magiclib$getPack();
}
