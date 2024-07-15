package top.hendrixshen.magiclib.impl.platform.adapter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.neoforged.fml.ModList;
import net.neoforged.neoforgespi.language.IModFileInfo;
import net.neoforged.neoforgespi.language.IModInfo;
import top.hendrixshen.magiclib.api.platform.adapter.forge.ModListAdapter;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NeoForgeModList implements ModListAdapter {
    @Getter(lazy = true)
    private static final ModListAdapter instance = new NeoForgeModList();

    @Override
    public ValueContainer<Collection<IModFileInfo>> getModFiles() {
        return ValueContainer.ofNullable(ModList.get()).map(ModList::getModFiles);
    }

    @Override
    public ValueContainer<Collection<IModInfo>> getMods() {
        return ValueContainer.ofNullable(ModList.get()).map(ModList::getMods);
    }

    @Override
    public ValueContainer<IModFileInfo> getModFileById(String identifier) {
        return ValueContainer.ofNullable(ModList.get()).map(modList -> modList.getModFileById(identifier));
    }
}
