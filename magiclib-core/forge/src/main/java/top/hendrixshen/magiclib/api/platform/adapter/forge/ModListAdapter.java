package top.hendrixshen.magiclib.api.platform.adapter.forge;

import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;

public interface ModListAdapter {
    ValueContainer<Collection<IModFileInfo>> getModFiles();

    ValueContainer<Collection<IModInfo>> getMods();

    ValueContainer<IModFileInfo> getModFileById(String identifier);
}
