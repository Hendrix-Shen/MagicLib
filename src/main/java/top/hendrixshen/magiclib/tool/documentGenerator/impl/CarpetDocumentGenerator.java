package top.hendrixshen.magiclib.tool.documentGenerator.impl;

import com.google.common.collect.Lists;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.carpet.api.Validator;
import top.hendrixshen.magiclib.carpet.impl.RuleOption;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;
import top.hendrixshen.magiclib.dependency.impl.Dependencies;
import top.hendrixshen.magiclib.dependency.impl.Dependency;
import top.hendrixshen.magiclib.impl.carpet.CarpetEntrypoint;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class CarpetDocumentGenerator extends BaseDocumentGenerator {
    public final WrappedSettingManager settingManager;

    public CarpetDocumentGenerator(@NotNull String identifier) {
        super(identifier);
        this.settingManager = WrappedSettingManager.get(identifier);
        if (this.settingManager == null) {
            throw new RuntimeException();
        }
    }

    @Override
    public void setCurrentLanguageCode(String currentLanguageCode) {
        CarpetEntrypoint.getSettingManager().getRuleOption("language").setValue(currentLanguageCode);
        super.setCurrentLanguageCode(currentLanguageCode);
    }

    public String trRuleName(@NotNull RuleOption option) {
        return this.settingManager.getTranslatedRuleName(option.getName());
    }

    public String trRuleDesc(@NotNull RuleOption option) {
        return this.settingManager.trRuleDesc(option.getName());
    }

    public String trLabel(String label) {
        return this.tr(String.format("%s.document.rule.label.%s", MagicLibReference.getModIdentifier(), label));
    }

    public String trRuleCategory(@NotNull String category) {
        return this.settingManager.trCategory(category);
    }

    public String trType(String type) {
        return this.tr(String.format("%s.document.rule.type.%s", MagicLibReference.getModIdentifier(), type));
    }

    public String trValidator(String validatorName) {
        return this.tr(String.format("magiclib.document.rule.validator.%s", validatorName));
    }

    public String trType(@NotNull RuleOption option) {
        if (option.getType() == Boolean.class) {
            return this.trType("boolean");
        } else if (option.getType() == Byte.class) {
            return this.trType("byte");
        } else if (option.getType() == Double.class) {
            return this.trType("double");
        } else if (option.getType().isEnum()) {
            return this.trType("enum");
        } else if (option.getType() == Float.class) {
            return this.trType("float");
        } else if (option.getType() == Integer.class) {
            return this.trType("integer");
        } else if (option.getType() == Long.class) {
            return this.trType("long");
        } else if (option.getType() == Short.class) {
            return this.trType("short");
        } else if (option.getType() == String.class) {
            return this.trType("string");
        } else {
            return this.trType("unknown");
        }
    }

    public String genRuleEntry(RuleOption option) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("## %s", this.trRuleName(option)));
        builder.append("\n");
        String desc = this.trRuleDesc(option);

        if (!desc.equals("")) {
            builder.append(this.trRuleDesc(option));
            builder.append("\n");
        }

        List<Component> extraInfo = this.settingManager.trRuleExtraInfo(option.getName());

        if (!extraInfo.isEmpty()) {
            for (Component component : this.settingManager.trRuleExtraInfo(option.getName())) {
                builder.append("\n");
                builder.append(String.format("%s", component.getString()));
                builder.append("\n");
            }
        }

        List<String> categories = Arrays.asList(option.getCategory());

        if (!categories.isEmpty()) {
            List<String> categoriesList = Lists.newArrayList();
            categoriesList.add(String.format("- %s: ", this.trLabel("categories")));

            for (String category : categories) {
                categoriesList.add(String.format("`%s`", trRuleCategory(category)));
                categoriesList.add(", ");
            }

            categoriesList.remove(categoriesList.size() - 1);

            for (String categoryString : categoriesList) {
                builder.append(categoryString);
            }

            builder.append("\n");
        }

        builder.append(String.format("- %s: `%s`", this.trLabel("type"), this.trType(option)));
        builder.append("\n");
        StringBuilder valueEntry = this.genValueEntry(option);

        if (valueEntry != null) {
            builder.append(valueEntry);
            builder.append("\n");
        }

        if (!option.getValidators().isEmpty() || option.getType().isEnum()) {
            StringBuilder validators = this.genValidators(option);

            if (validators != null) {
                builder.append(String.format("- %s:", this.trLabel("validators")));
                builder.append("\n");
                builder.append(validators);
                builder.append("\n");
            }
        }

        StringBuilder dependenciesEntry = this.genDependenciesEntry(option);

        if (dependenciesEntry != null) {
            builder.append(String.format("- %s:", this.trLabel("dependencies")));
            builder.append("\n");
            builder.append(dependenciesEntry);
            builder.append("\n");
        }

        return builder.toString();
    }

    private @Nullable StringBuilder genDependenciesEntry(@NotNull RuleOption option) {
        Dependencies<RuleOption> dependencies = option.getModDependencies();
        StringBuilder stringBuilder = new StringBuilder();

        if (!dependencies.andRequirements.isEmpty()) {
            stringBuilder.append(String.format("  - %s:", this.trLabel("dependencies_and")));
            stringBuilder.append("\n");
        }

        for (Dependency andDependency : dependencies.andRequirements) {
            stringBuilder.append(String.format("    - %s: %s", andDependency.modId, andDependency.versionPredicate));
            stringBuilder.append("\n");
        }

        if (!dependencies.conflicts.isEmpty()) {
            stringBuilder.append(String.format("  - %s:", this.trLabel("dependencies_not")));
            stringBuilder.append("\n");
        }

        for (Dependency orDependency : dependencies.conflicts) {
            stringBuilder.append(String.format("    - %s: %s", orDependency.modId, orDependency.versionPredicate));
            stringBuilder.append("\n");
        }

        if (!dependencies.orRequirements.isEmpty()) {
            stringBuilder.append(String.format("  - %s:", this.trLabel("dependencies_or")));
            stringBuilder.append("\n");
        }

        for (Dependency orDependency : dependencies.orRequirements) {
            stringBuilder.append(String.format("    - %s: %s", orDependency.modId, orDependency.versionPredicate));
            stringBuilder.append("\n");
        }

        if (!dependencies.andRequirements.isEmpty() || !dependencies.conflicts.isEmpty() || !dependencies.orRequirements.isEmpty()) {
            return stringBuilder;
        }

        return null;
    }

    private @Nullable StringBuilder genValueEntry(@NotNull RuleOption option) {
        if (!Arrays.stream((new Class[]{Boolean.class, Byte.class, Double.class, Float.class, Integer.class, Long.class,
                Short.class, String.class})).collect(Collectors.toList()).contains(option.getType()) && !option.getType().isEnum()) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("- %s: %s", this.trLabel("default_value"), option.getDefaultStringValue()));

        if (!option.getOptions().isEmpty()) {
            builder.append("\n");
            builder.append(String.format("- %s: ", this.trLabel("options")));
            List<String> optionList = Lists.newArrayList();

            for (String opt : option.getOptions()) {
                optionList.add(String.format("`%s`", opt));
                optionList.add(", ");
            }

            optionList.remove(optionList.size() - 1);

            for (String opt : optionList) {
                builder.append(opt);
            }
        }

        return builder;
    }

    public StringBuilder genValidators(@NotNull RuleOption option) {
        List<Validator<?>> validators = option.getValidators();
        StringBuilder builder = new StringBuilder();

        for (Validator<?> validator : validators) {
            String[] split = validator.getClass().getSimpleName().split("\\.");
            char[] chars = split[split.length - 1].toCharArray();

            if (chars[0] >= 65 && chars[0] <= 90) {
                chars[0] += 32;
            }

            builder.append(String.format("  - %s", this.trValidator(String.valueOf(chars))));
            String description = validator.description();

            if (description != null) {
                builder.append(String.format("    - %s: %s", this.trLabel("validator.desc"), description.replaceAll("\n", " ")));
            }
        }

        if (option.getType().isEnum()) {
            builder.append(String.format("  - %s", this.trValidator("enum")));
        }

        return builder;
    }

    @Override
    public void generateDocument() {
        String language = this.getCurrentLanguageCode().equals(ConfigDocumentGenerator.DEFAULT_LANGUAGE) ?
                "" : String.format("_%s", this.getCurrentLanguageCode().toUpperCase(Locale.ROOT));
        File file = new File(String.format("./document/%s/rule%s.md", this.getIdentifier(), language));

        if (!file.exists()) {
            new File(String.format("./document/%s", this.getIdentifier())).mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(file);

            for (RuleOption ruleOption : this.settingManager.getRuleOptions().stream()
                    .sorted(Comparator.comparing(RuleOption::getName)).collect(Collectors.toList())) {
                fw.write(this.genRuleEntry(ruleOption));
            }

            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
