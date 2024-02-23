package top.hendrixshen.magiclib.impl.platform.adapter;

import lombok.Getter;
import net.neoforged.fml.loading.LoadingModList;
import net.neoforged.fml.loading.moddiscovery.ModFileInfo;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import top.hendrixshen.magiclib.api.platform.adapter.forge.ModListAdapter;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;
import java.util.stream.Collectors;

public class NeoForgeLoadingModList implements ModListAdapter {
    @Getter(lazy = true)
    private static final ModListAdapter instance = new NeoForgeLoadingModList();

    @Override
    public ValueContainer<Collection<IModFileInfo>> getModFiles() {
        return ValueContainer.of(LoadingModList.get())
                .map(loadingModList -> loadingModList.getModFiles().stream()
                        .map(modFileInfo -> (IModFileInfo) modFileInfo).collect(Collectors.toList()));
    }

    @Override
    public ValueContainer<Collection<IModInfo>> getMods() {
        return ValueContainer.of(LoadingModList.get())
                .map(loadingModList -> loadingModList.getMods().stream()
                        .map(modFileInfo -> (IModInfo) modFileInfo).collect(Collectors.toList()));
    }
}
