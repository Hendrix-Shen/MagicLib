package top.hendrixshen.magiclib.util;

import com.google.common.base.Joiner;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependency;

import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class ModPredicate {
    public final String modId;
    public final List<String> versionPredicates;
    public final boolean satisfied;
    public final Predicate<?> predicate;

    private ModPredicate(String modId, List<String> versionPredicates, Predicate<?> predicate, boolean satisfied) {
        this.modId = modId;
        this.versionPredicates = versionPredicates;
        this.predicate = predicate;
        this.satisfied = satisfied;
    }

    public static ModPredicate of(Dependency dependency, @Nullable Object predicateObj) {

        Predicate<?> predicate;
        try {
            predicate = dependency.predicate().getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            predicate = new Dependency.DefaultPredicate() {
            };
        }
        String modId = dependency.value();
        List<String> versionPredicates = Arrays.asList(dependency.versionPredicates());

        return new ModPredicate(modId, versionPredicates, predicate,
                FabricUtil.isModsLoaded(modId, versionPredicates) && testHelper(predicate, predicateObj));
    }

    @SuppressWarnings("unchecked")
    private static <T> boolean testHelper(Predicate<T> l, Object obj) {
        return l.test((T) obj);
    }

    public String getVersionPredicatesString() {
        return this.versionPredicates.isEmpty() ? "" : " " + Joiner.on(" && ").join(this.versionPredicates);
    }

    @Override
    public String toString() {
        return String.format("%s - %s - %s", this.modId, this.getVersionPredicatesString(), this.predicate);
    }
}
