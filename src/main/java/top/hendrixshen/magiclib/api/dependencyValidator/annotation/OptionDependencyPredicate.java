package top.hendrixshen.magiclib.api.dependencyValidator.annotation;

import top.hendrixshen.magiclib.util.malilib.Option;

import java.util.function.Predicate;

public class OptionDependencyPredicate implements Predicate<Option> {
    @Override
    public boolean test(Option option) {
        return true;
    }
}
