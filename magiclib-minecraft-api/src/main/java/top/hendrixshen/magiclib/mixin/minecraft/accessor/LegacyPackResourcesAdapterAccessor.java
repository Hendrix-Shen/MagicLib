package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.resources.LegacyPackResourcesAdapter;
import net.minecraft.server.packs.PackResources;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19.3+        : subproject 1.19.3 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(LegacyPackResourcesAdapter.class)
public interface LegacyPackResourcesAdapterAccessor {
    @Accessor("source")
    PackResources magiclib$getSource();
}
