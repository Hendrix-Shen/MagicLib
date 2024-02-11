package top.hendrixshen.magiclib.impl.platform.adapter;

import net.neoforged.neoforgespi.language.IModInfo;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

public class NeoForgeModMetaData implements ModMetaDataAdapter {
    private final IModInfo modInfo;

    public NeoForgeModMetaData(IModInfo modInfo) {
        this.modInfo = modInfo;
    }

    public IModInfo get() {
        return this.modInfo;
    }

    @Override
    public String getModId() {
        return this.modInfo.getModId();
    }

    @Override
    public String getVersion() {
        return this.modInfo.getVersion().toString();
    }

    @Override
    public String getName() {
        return this.modInfo.getDisplayName();
    }

    @Override
    public String getDescription() {
        return this.modInfo.getDescription();
    }
}
