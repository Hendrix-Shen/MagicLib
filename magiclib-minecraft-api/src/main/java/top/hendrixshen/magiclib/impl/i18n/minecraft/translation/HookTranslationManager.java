package top.hendrixshen.magiclib.impl.i18n.minecraft.translation;

import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import top.hendrixshen.magiclib.SharedConstants;

import java.util.HashSet;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HookTranslationManager {
    @Getter(lazy = true)
    private static final HookTranslationManager instance = new HookTranslationManager();

    private final Set<String> namespaces = new HashSet<>();

    public void registerNamespace(String namespace) {
        if (!SharedConstants.getValidLangNamespace().matcher(namespace).matches()) {
            throw new RuntimeException("Invalid namespace: " + namespace);
        }

        this.namespaces.add(namespace);
    }

    @Unmodifiable
    public ImmutableSet<String> getNamespaces() {
        return ImmutableSet.copyOf(this.namespaces);
    }

    public boolean isNamespaceRegistered(@NotNull String path) {
        return this.namespaces.stream().anyMatch(path::startsWith);
    }
}
