package top.hendrixshen.magiclib.api.platform.adapter.forge;

import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;

public interface ModListAdapter {
    ValueContainer<Collection<IModFileInfo>> getModFiles();

    ValueContainer<Collection<IModInfo>> getMods();

    ValueContainer<IModFileInfo> getModFileById(String identifier);
}
