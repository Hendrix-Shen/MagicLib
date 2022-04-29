package top.hendrixshen.magiclib.language;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.Resource;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.util.Objects;
import java.util.function.Supplier;

//#if MC > 11802
//$$ public class MagicLanguageResource extends Resource {
//#else
public class MagicLanguageResource implements Resource {
    //#endif
    private final String sourceName;
    private final ResourceLocation location;
    private final Supplier<InputStream> inputStreamSupplier;

    public MagicLanguageResource(String string, ResourceLocation resourceLocation, Supplier<InputStream> inputStreamSupplier) {
        //#if MC > 11802
        //$$ super(string, inputStreamSupplier::get);
        //#endif
        this.sourceName = string;
        this.location = resourceLocation;
        this.inputStreamSupplier = inputStreamSupplier;
    }

    //#if MC <= 11802
    @Override
    //#endif
    public ResourceLocation getLocation() {
        return location;
    }

    @Override
    public InputStream getInputStream() {
        return inputStreamSupplier.get();
    }

    //#if MC <= 11802
    //#if MC > 11605
    @Override
    public boolean hasMetadata() {
        return false;
    }
    //#endif

    @Nullable
    @Override
    public <T> T getMetadata(@NotNull MetadataSectionSerializer<T> metadataSectionSerializer) {
        return null;
    }

    @Override
    public void close() {
    }
    //#endif

    @Override
    public String getSourceName() {
        return sourceName;
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
                    && Objects.equals(this.sourceName, magicLanguageResource.sourceName);
        }
    }

    // from SimpleResource
    @Override
    public int hashCode() {
        int i = this.sourceName != null ? this.sourceName.hashCode() : 0;
        i = 31 * i + (this.location != null ? this.location.hashCode() : 0);
        return i;
    }
}
