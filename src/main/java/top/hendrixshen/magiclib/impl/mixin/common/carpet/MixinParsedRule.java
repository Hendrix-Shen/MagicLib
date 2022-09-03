package top.hendrixshen.magiclib.impl.mixin.common.carpet;

import carpet.settings.ParsedRule;
import com.google.common.collect.ImmutableList;
import net.minecraft.commands.CommandSourceStack;
import org.apache.commons.lang3.ClassUtils;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
//#if MC > 11802
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif
import top.hendrixshen.magiclib.api.rule.Validators;
import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;
import top.hendrixshen.magiclib.api.rule.annotation.Command;
import top.hendrixshen.magiclib.api.rule.annotation.Numeric;
import top.hendrixshen.magiclib.api.rule.annotation.Rule;
import top.hendrixshen.magiclib.compat.annotation.InitMethod;
import top.hendrixshen.magiclib.compat.annotation.Public;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
//#if MC > 11605

//#endif

@Mixin(ParsedRule.class)
public abstract class MixinParsedRule<T> {
    // All field are dummy.
    public Field field;
    public String name;
    public String description;
    String scarpetApp;
    //#if MC > 11605
    List<String> categories;
    List<String> options;
    //#else
    //$$ ImmutableList<String> categories;
    //$$ ImmutableList<String> options;
    //#endif
    public Class<?> type;
    //#if MC > 11802
    public List<carpet.api.settings.Validator<?>> realValidators;
    //#endif
    public List<carpet.settings.Validator<?>> validators;
    public T defaultValue;
    public String defaultAsString;
    public boolean isStrict;
    public carpet.settings.SettingsManager settingsManager;
    //#if MC > 11802
    public carpet.api.settings.SettingsManager realSettingsManager;
    //#endif

    @Shadow
    public abstract T get();

    //#if MC > 11802
    @Shadow
    protected abstract void set(CommandSourceStack par1, Object par2, String par3) throws carpet.api.settings.InvalidRuleValueException;

    @Shadow
    public abstract Class<T> type();
    //#endif

    @Public
    @InitMethod
    private void magicInit(@NotNull Field field, @NotNull Rule rule, WrapperSettingManager settingsManager) {
        this.field = field;
        this.name = field.getName();
        //#if MC > 11802
        this.type = ClassUtils.primitiveToWrapper(field.getType());
        //#else
        //$$ this.type = field.getType();
        //#endif
        this.description = null;
        this.defaultAsString = this.magiclib$convertToString(this.get());
        this.isStrict = rule.strict();
        //#if MC > 11605
        this.categories = Arrays.asList(rule.categories());
        //#else
        //$$ this.categories = ImmutableList.copyOf(rule.categories());
        //#endif
        this.scarpetApp = rule.appSource();
        //#if MC > 11802
        this.settingsManager = null;
        this.realSettingsManager = settingsManager;
        this.validators = Collections.emptyList();
        this.realValidators = Stream.of(rule.validators()).map(this::magiclib$instantiateValidator).collect(Collectors.toList());
        //#else
        //$$ this.validators = Stream.of(rule.validators()).map(this::magiclib$instantiateValidator).collect(Collectors.toList());
        //$$ this.settingsManager = settingsManager;
        //#endif
        this.defaultValue = this.get();

        Command commandAnnotation = field.getAnnotation(Command.class);
        Numeric numericAnnotation = field.getAnnotation(Numeric.class);
        if (rule.options().length > 0) {
            //#if MC > 11605
            this.options = Arrays.asList(rule.options());
            //#else
            //$$ this.options = ImmutableList.copyOf(rule.options());
            //#endif
        //#if MC > 11802
        } else if (this.type == Boolean.class) {
        //#else
        //$$ } else if (this.type == Boolean.TYPE) {
        //#endif
            //#if MC > 11605
            this.options = Arrays.asList("true", "false");
            //#else
            //$$ this.options = ImmutableList.of("true", "false");
            //#endif
        } else if (this.type.isEnum()) {
            this.options = Arrays.stream(this.type.getEnumConstants())
                    .map(e -> ((Enum<?>)e).name().toLowerCase(Locale.ROOT))
                    .collect(ImmutableList.toImmutableList());
            this.isStrict = true;
        } else if (this.type == String.class && commandAnnotation != null) {
            this.isStrict = false;
            //#if MC > 11605
            this.options = commandAnnotation.full() ? Validators.Command.FULL_OPTIONS : Validators.Command.MINIMAL_OPTIONS;
            //#else
            //$$ this.options = commandAnnotation.full() ? ImmutableList.copyOf(Validators.Command.FULL_OPTIONS) : ImmutableList.copyOf(Validators.Command.MINIMAL_OPTIONS);
            //#endif
            //#if MC > 11802
            this.realValidators.add(this.magiclib$instantiateValidator(Validators.Command.class));
            //#else
            //$$ this.validators.add(this.magiclib$instantiateValidator(Validators.Command.class));
            //#endif
        } else {
            //#if MC > 11605
            this.options = Collections.emptyList();
            //#else
            //$$ this.options = ImmutableList.of();
            //#endif
        }

        if (numericAnnotation != null && (this.type == Byte.class || this.type == Short.class ||
                this.type == Integer.class || this.type == Long.class || this.type == Float.class ||
                this.type == Double.class)) {
            //#if MC > 11802
            this.realValidators.add(0, new Validators.Numeric<>(numericAnnotation.maxValue(), numericAnnotation.minValue(), numericAnnotation.canMaxEquals(), numericAnnotation.canMinEquals()));
            //#else
            //$$ this.validators.add(0, new Validators.Numeric<>(numericAnnotation.maxValue(), numericAnnotation.minValue(), numericAnnotation.canMaxEquals(), numericAnnotation.canMinEquals()));
            //#endif
        }

        if (this.isStrict && !this.options.isEmpty()) {
            //#if MC > 11802
            this.realValidators.add(0, new Validators.Strict<>());
            //#else
            //$$ this.validators.add(0, new Validators.Strict<>());
            //#endif
        }
    }

    @SuppressWarnings({"rawtypes"})
    private top.hendrixshen.magiclib.api.rule.Validator<?> magiclib$instantiateValidator(Class<? extends top.hendrixshen.magiclib.api.rule.Validator> cls) {
        try {
            Constructor<? extends top.hendrixshen.magiclib.api.rule.Validator> constructor = cls.getConstructor();
            constructor.setAccessible(true);
            return constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"rawtypes"})
    private String magiclib$convertToString(Object value) {
        return value instanceof Enum ? ((Enum)value).name().toLowerCase(Locale.ROOT) : value.toString();
    }

    //#if MC > 11802
    @SuppressWarnings({"rawtypes", "unchecked"})
    @Inject(
            method = "set(Lnet/minecraft/commands/CommandSourceStack;Ljava/lang/String;)V",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    public void set(CommandSourceStack source, String value, CallbackInfo ci) throws carpet.api.settings.InvalidRuleValueException {
        if (!(this.realSettingsManager instanceof WrapperSettingManager)) {
            return;
        }
        if (this.type == String.class) {
            this.set(source, (T) value, value);
        } else if (this.type == Boolean.class) {
            this.set(source, (T) (Object) Boolean.parseBoolean(value), value);
        } else if (this.type == Integer.class) {
            this.set(source, (T) (Object) Integer.parseInt(value), value);
        } else if (this.type == Double.class) {
            this.set(source, (T) (Object) Double.parseDouble(value), value);
        } else if (this.type.isEnum()) {
            String ucValue = value.toUpperCase(Locale.ROOT);
            this.set(source, (T) (Object) Enum.valueOf((Class<? extends Enum>) type, ucValue), value);
        }
        ci.cancel();
    }
    //#endif
}
