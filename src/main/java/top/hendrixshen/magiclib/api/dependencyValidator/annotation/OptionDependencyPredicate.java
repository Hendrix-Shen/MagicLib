package top.hendrixshen.magiclib.api.dependencyValidator.annotation;

import top.hendrixshen.magiclib.config.Option;

import java.util.function.Predicate;

public class OptionDependencyPredicate implements Predicate<Option> {
    @Override
    public boolean test(Option option) {
        return true;
    }
}
