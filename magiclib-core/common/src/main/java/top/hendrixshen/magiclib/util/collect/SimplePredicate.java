package top.hendrixshen.magiclib.util.collect;

@FunctionalInterface
public interface SimplePredicate<T> {
    boolean test(T t);
}
