package top.hendrixshen.magiclib.api.dependency;

@FunctionalInterface
public interface SimplePredicate<T> {
    boolean test(T t);
}
