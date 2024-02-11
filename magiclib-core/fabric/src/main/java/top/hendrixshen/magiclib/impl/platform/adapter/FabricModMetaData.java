package top.hendrixshen.magiclib.impl.platform.adapter;

import net.fabricmc.loader.api.metadata.ModMetadata;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

public class FabricModMetaData implements ModMetaDataAdapter {
    private final ModMetadata modMetadata;

    public FabricModMetaData(ModMetadata modMetadata) {
        this.modMetadata = modMetadata;
    }

    public ModMetadata get() {
        return this.modMetadata;
    }

    @Override
    public String getModId() {
        return this.modMetadata.getId();
    }

    @Override
    public String getVersion() {
        return this.modMetadata.getVersion().getFriendlyString();
    }

    @Override
    public String getName() {
        return this.modMetadata.getName();
    }

    @Override
    public String getDescription() {
        return this.modMetadata.getDescription();
    }
}
