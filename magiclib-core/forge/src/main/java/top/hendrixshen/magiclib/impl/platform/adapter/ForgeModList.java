package top.hendrixshen.magiclib.impl.platform.adapter;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import top.hendrixshen.magiclib.api.platform.adapter.forge.ModListAdapter;
import top.hendrixshen.magiclib.util.CommonUtil;
import top.hendrixshen.magiclib.util.ReflectionUtil;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ForgeModList implements ModListAdapter {
    @Getter(lazy = true)
    private static final ModListAdapter instance = new ForgeModList();
    private static final ValueContainer<Method> getModFilesMethod = CommonUtil.make(() -> {
        ValueContainer<Class<?>> modListClazz = ReflectionUtil.getClass("net.minecraftforge.fml.ModList");

        if (modListClazz.isException()) {
            return ValueContainer.exception(new RuntimeException("Unable to initialize ModList.", modListClazz.getException()));
        }

        return ReflectionUtil.getMethod(modListClazz, "getModFiles");
    });
    private static final Function<ModList, List<IModFileInfo>> getModFilesFunction = (modList) -> {
        if (ForgeModList.getModFilesMethod.isEmpty()) {
            return Lists.newArrayList();
        }

        ValueContainer<List<IModFileInfo>> ret = ReflectionUtil.invoke(ForgeModList.getModFilesMethod, modList);
        return ret.orElse(Lists.newArrayList());
    };

    private static final ValueContainer<Method> getModsMethod = CommonUtil.make(() -> {
        ValueContainer<Class<?>> modListClazz = ReflectionUtil.getClass("net.minecraftforge.fml.ModList");

        if (modListClazz.isException()) {
            return ValueContainer.exception(new RuntimeException("Unable to initialize ModList.", modListClazz.getException()));
        }

        return ReflectionUtil.getMethod(modListClazz, "getMods");
    });
    private static final Function<ModList, List<IModInfo>> getModsFunction = (modList) -> {
        if (ForgeModList.getModsMethod.isEmpty()) {
            return Lists.newArrayList();
        }

        ValueContainer<List<IModInfo>> ret = ReflectionUtil.invoke(ForgeModList.getModsMethod, modList);
        return ret.orElse(Lists.newArrayList());
    };

    private static final ValueContainer<Method> getModFileByIdMethod = CommonUtil.make(() -> {
        ValueContainer<Class<?>> modListClazz = ReflectionUtil.getClass("net.minecraftforge.fml.ModList");

        if (modListClazz.isException()) {
            return ValueContainer.exception(new RuntimeException("Unable to initialize ModList.", modListClazz.getException()));
        }

        return ReflectionUtil.getMethod(modListClazz, "getModFileById", String.class);
    });
    private static final BiFunction<ModList, String, IModFileInfo> getModFileByIdFunction = (modList, identifier) -> {
        if (ForgeModList.getModFileByIdMethod.isEmpty()) {
            return null;
        }

        ValueContainer<IModFileInfo> ret = ReflectionUtil.invoke(ForgeModList.getModFileByIdMethod, modList, identifier);
        return ret.orElse(null);
    };

    @Override
    public ValueContainer<Collection<IModFileInfo>> getModFiles() {
        return ValueContainer.ofNullable(ModList.get()).map(ForgeModList.getModFilesFunction);
    }

    @Override
    public ValueContainer<Collection<IModInfo>> getMods() {
        return ValueContainer.ofNullable(ModList.get()).map(ForgeModList.getModsFunction);
    }

    @Override
    public ValueContainer<IModFileInfo> getModFileById(String identifier) {
        return ValueContainer.ofNullable(ModList.get()).map(modList -> ForgeModList.getModFileByIdFunction.apply(modList, identifier));
    }
}
