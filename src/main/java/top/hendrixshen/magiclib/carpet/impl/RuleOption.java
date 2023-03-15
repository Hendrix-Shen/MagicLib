package top.hendrixshen.magiclib.carpet.impl;

import carpet.settings.ParsedRule;
import net.minecraft.commands.CommandSourceStack;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.carpet.api.Validator;
import top.hendrixshen.magiclib.carpet.api.annotation.Rule;
import top.hendrixshen.magiclib.dependency.api.RuleDependencyPredicate;
import top.hendrixshen.magiclib.dependency.impl.Dependencies;
import top.hendrixshen.magiclib.util.ReflectUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

//#if MC >= 11901
@SuppressWarnings("removal")
//#endif
public class RuleOption {
    private final Rule annotation;
    private final Dependencies<RuleOption> modDependencies;
    private final RuleDependencyPredicate predicate;
    private final ParsedRule<?> parsedRule;

    public RuleOption(@NotNull Rule ruleAnnotation, ParsedRule<?> rule) {
        this.annotation = ruleAnnotation;
        this.modDependencies = Dependencies.of(annotation.dependencies(), RuleOption.class);
        this.parsedRule = rule;

        try {
            this.predicate = ruleAnnotation.predicate().getDeclaredConstructor().newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getCategory() {
        return this.annotation.categories();
    }

    public ParsedRule<?> getRule() {
        return this.parsedRule;
    }

    public Dependencies<RuleOption> getModDependencies() {
        return this.modDependencies;
    }

    public boolean isEnabled() {
        return this.modDependencies.satisfied(this) && predicate.isSatisfied(this);
    }

    public String getName() {
        //#if MC > 11802
        return this.parsedRule.name();
        //#else
        //$$ return this.parsedRule.name;
        //#endif
    }

    public boolean isDefault() {
        return this.getStringValue().equals(this.getDefaultStringValue());
    }

    public Object getValue() {
        //#if MC > 11802
        return this.parsedRule.value();
        //#else
        //$$ return this.parsedRule.get();
        //#endif
    }

    public Object setValue(String newValue) {
        return this.setValue(null, newValue);
    }

    public Object setValue(CommandSourceStack source, String newValue) {
        //#if MC > 11802
        try {
            this.parsedRule.set(source, newValue);
            return this.getValue();
        } catch (carpet.api.settings.InvalidRuleValueException e) {
            e.printStackTrace();
            return null;
        }
        //#else
        //$$ return this.parsedRule.set(source, newValue);
        //#endif
    }

    public Object resetValue() {
        return this.resetValue(null);
    }

    public Object resetValue(CommandSourceStack source) {
        return this.setValue(source, this.getDefaultStringValue());
    }

    public Object getDefaultValue() {
        //#if MC > 11802
        return this.parsedRule.defaultValue();
        //#else
        //$$ return this.parsedRule.defaultValue;
        //#endif
    }

    public String getStringValue() {
        return this.getAsString(this.getValue());
    }

    public String getDefaultStringValue() {
        return this.getAsString(this.getDefaultValue());
    }

    public Collection<String> getOptions() {
        //#if MC > 11802
        return this.parsedRule.suggestions();
        //#else
        //$$ return this.parsedRule.options;
        //#endif
    }

    @SuppressWarnings("rawtypes")
    private String getAsString(Object value) {
        return value instanceof Enum ? ((Enum)value).name().toLowerCase(Locale.ROOT) : value.toString();
    }

    public Class<?> getType() {
        return (Class<?>) ReflectUtil.getDeclaredFieldValue(ParsedRule.class, "typeCompat", this.getRule()).orElseThrow(RuntimeException::new);
    }

    @SuppressWarnings("unchecked")
    public List<Validator<?>> getValidators() {
        //#if MC > 11802
        return (List<Validator<?>>) ReflectUtil.getFieldValue(ParsedRule.class, "realValidators", this.getRule()).orElseThrow(RuntimeException::new);
        //#else
        //$$ return (List<Validator<?>>) ReflectUtil.getFieldValue(ParsedRule.class, "validators", this.getRule()).orElseThrow(RuntimeException::new);
        //#endif
    }
}
