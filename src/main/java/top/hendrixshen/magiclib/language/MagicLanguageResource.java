package top.hendrixshen.magiclib.language;

import net.minecraft.resources.ResourceLocation;
//#if MC >= 11903
import net.minecraft.server.packs.PackResources;
//#else
//$$ import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
//$$ import org.jetbrains.annotations.NotNull;
//$$ import org.jetbrains.annotations.Nullable;
//#endif
import net.minecraft.server.packs.resources.Resource;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

//#if MC > 11802
public class MagicLanguageResource extends Resource {
//#else
//$$ public class MagicLanguageResource implements Resource {
    //#endif
    //#if MC >= 11903
    private final PackResources resources;
    //#else
    //$$ private final String sourceName;
    //#endif
    private final ResourceLocation location;
    private final Supplier<InputStream> inputStreamSupplier;

    //#if MC > 11903
    public MagicLanguageResource(PackResources resources, ResourceLocation resourceLocation, Supplier<InputStream> inputStreamSupplier) {
        super(resources, inputStreamSupplier::get);
        this.resources =resources;
    //#else
    //$$ public MagicLanguageResource(String sourceName, ResourceLocation resourceLocation, Supplier<InputStream> inputStreamSupplier) {
    //#if MC > 11802
    //$$     super(string, inputStreamSupplier::get);
    //#endif
    //$$     this.sourceName = sourceName;
    //#endif
        this.location = resourceLocation;
        this.inputStreamSupplier = inputStreamSupplier;
    }

    //#if MC <= 11802
    //$$ @Override
    //#endif
    public ResourceLocation getLocation() {
        return this.location;
    }

    @Override
    public InputStream open() {
        return inputStreamSupplier.get();
    }

    //#if MC <= 11802
    //#if MC > 11605
    //$$ @Override
    //$$ public boolean hasMetadata() {
    //$$     return false;
    //$$ }
    //#endif
    //$$
    //$$ @Nullable
    //$$ @Override
    //$$ public <T> T getMetadata(@NotNull MetadataSectionSerializer<T> metadataSectionSerializer) {
    //$$     return null;
    //$$ }
    //$$
    //$$ @Override
    //$$ public void close() {
    //$$ }
    //#endif

    @Override
    public String sourcePackId() {
        //#if MC >= 11903
        return this.resources.packId();
        //#else
        //$$ return this.sourceName;
        //#endif
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        } else if (!(object instanceof MagicLanguageResource)) {
            return false;
        } else {
            MagicLanguageResource magicLanguageResource = (MagicLanguageResource) object;
            return Objects.equals(this.location, magicLanguageResource.location)
                    //#if MC >= 11903
                    && Objects.equals(this.sourcePackId(), magicLanguageResource.sourcePackId());
                    //#else
                    //$$ && Objects.equals(this.sourceName, magicLanguageResource.sourceName);
                    //#endif
        }
    }

    // from SimpleResource
    @Override
    public int hashCode() {
        //#if MC >= 11903
        int i = this.sourcePackId() != null ? this.sourcePackId().hashCode() : 0;
        //#else
        //$$ int i = this.sourceName != null ? this.sourceName.hashCode() : 0;
        //#endif
        i = 31 * i + (this.location != null ? this.location.hashCode() : 0);
        return i;
    }
}
