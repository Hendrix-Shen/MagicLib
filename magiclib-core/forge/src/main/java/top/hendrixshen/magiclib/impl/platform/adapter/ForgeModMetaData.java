package top.hendrixshen.magiclib.impl.platform.adapter;

import net.minecraftforge.forgespi.language.IModInfo;
import top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter;

public class ForgeModMetaData implements ModMetaDataAdapter {
    private final IModInfo modInfo;

    public ForgeModMetaData(IModInfo modInfo) {
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
