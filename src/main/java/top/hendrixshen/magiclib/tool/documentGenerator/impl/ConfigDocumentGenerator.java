package top.hendrixshen.magiclib.tool.documentGenerator.impl;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.impl.Dependencies;
import top.hendrixshen.magiclib.dependency.impl.Dependency;
import top.hendrixshen.magiclib.language.api.I18n;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;
import top.hendrixshen.magiclib.malilib.impl.ConfigOption;
import top.hendrixshen.magiclib.malilib.impl.config.*;
import top.hendrixshen.magiclib.tool.documentGenerator.api.DocumentGenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class ConfigDocumentGenerator extends BaseDocumentGenerator {
    @Getter
    private final ConfigManager configManager;
    public ConfigDocumentGenerator(@NotNull String identifier) {
        super(identifier);
        this.configManager = ConfigManager.get(identifier);
    }

    public String trConfigName(@NotNull ConfigOption option) {
        return this.tr(String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName()));
    }

    public String trConfigDesc(@NotNull ConfigOption option) {
        return this.tr(String.format("%s.config.%s.%s.comment", this.getIdentifier(), option.getCategory(), option.getName()));
    }

    public String trLabel(String label) {
        return this.tr(String.format("%s.document.config.label.%s", MagicLibReference.getModIdentifier(), label));
    }

    public String trConfigCategory(@NotNull ConfigOption option) {
        return this.tr(String.format("%s.gui.button.tab.%s", this.getIdentifier(), option.getCategory()));
    }

    public String trType(String type) {
        return this.tr(String.format("%s.document.config.type.%s", MagicLibReference.getModIdentifier(), type));
    }

    private String trType(@NotNull ConfigOption option) {
        if (option.getConfig() instanceof MagicConfigBoolean) {
            return this.trType("boolean");
        } else if (option.getConfig() instanceof MagicConfigBooleanHotkeyed) {
            return this.trType("boolean_with_hotkey");
        } else if (option.getConfig() instanceof MagicConfigColor) {
            return this.trType("color");
        } else if (option.getConfig() instanceof MagicConfigDouble) {
            return this.trType("double");
        } else if (option.getConfig() instanceof MagicConfigHotkey) {
            return this.trType("hotkey");
        } else if (option.getConfig() instanceof MagicConfigInteger) {
            return this.trType("integer");
        } else if (option.getConfig() instanceof MagicConfigOptionList) {
            return this.trType("enum");
        } else if (option.getConfig() instanceof MagicConfigString) {
            return this.trType("string");
        } else if (option.getConfig() instanceof MagicConfigStringList) {
            return this.trType("string_list");
        }

        MagicLibReference.getLogger().error("Unknown type {}: {}", this.trConfigName(option), option.getConfig().getType());
        return this.trType("unknown");
    }

    public String defaultConfigName(@NotNull ConfigOption option) {
        return I18n.exists(DocumentGenerator.DEFAULT_LANGUAGE, String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName())) ?
                I18n.getByCode(ConfigDocumentGenerator.DEFAULT_LANGUAGE ,String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName())) : option.getName();
    }

    public String getTranslatedConfigName(ConfigOption option) {
        return (!this.getCurrentLanguageCode().equals(ConfigDocumentGenerator.DEFAULT_LANGUAGE) &&
                I18n.exists(this.getCurrentLanguageCode(), String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName()))) ?
                String.format("%s (%s)", this.trConfigName(option), this.defaultConfigName(option)) :
                this.defaultConfigName(option);
    }

    public String genConfigEntry(ConfigOption option) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("## %s", this.getTranslatedConfigName(option).replaceAll("§[0-9a-fk-norA-FK-NOR]", "")));
        builder.append("\n");
        builder.append(this.trConfigDesc(option).replaceAll("\n", "\n\n"));
        builder.append("\n\n");
        builder.append(String.format("- %s: `%s`", this.trLabel("category"), this.trConfigCategory(option)));
        builder.append("\n");
        builder.append(String.format("- %s: `%s`", this.trLabel("type"), this.trType(option)));
        builder.append("\n");
        StringBuilder valueEntry = this.genValueEntry(option);

        if (valueEntry != null) {
            builder.append(valueEntry);
            builder.append("\n");
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

    private @Nullable StringBuilder genDependenciesEntry(@NotNull ConfigOption option) {
        Dependencies<ConfigOption> dependencies = option.getModDependencies();
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

    public void genBooleanValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigBoolean) option.getConfig()).getStringValue()));
    }

    public void genBooleanHotkeyedValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`, `%s`", this.trLabel("default_value"),
                ((MagicConfigBooleanHotkeyed) option.getConfig()).getDefaultStringValue(),
                ((MagicConfigBooleanHotkeyed) option.getConfig()).getKeybind().getStringValue().equals("")
                        ? this.trLabel("no_key") : ((MagicConfigBooleanHotkeyed) option.getConfig()).getKeybind().getStringValue()));
    }

    public void genColorValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigColor) option.getConfig()).getDefaultStringValue()));
    }

    public void genDoubleValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigDouble) option.getConfig()).getDefaultStringValue()));
        builder.append("\n");
        builder.append(String.format("- %s: `%s`", this.trLabel("min_value"),
                ((MagicConfigDouble) option.getConfig()).getMinDoubleValue()));
        builder.append("\n");
        builder.append(String.format("- %s: `%s`", this.trLabel("max_value"),
                ((MagicConfigDouble) option.getConfig()).getMaxDoubleValue()));
    }

    public void genHotkeyValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigHotkey) option.getConfig()).getDefaultStringValue().equals("") ?
                        this.trLabel("no_key") : ((MagicConfigHotkey) option.getConfig()).getDefaultStringValue()));
    }

    public void genIntegerValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigInteger) option.getConfig()).getDefaultStringValue()));
        builder.append("\n");
        builder.append(String.format("- %s: `%s`", this.trLabel("min_value"),
                ((MagicConfigInteger) option.getConfig()).getMinIntegerValue()));
        builder.append("\n");
        builder.append(String.format("- %s: `%s`", this.trLabel("max_value"),
                ((MagicConfigInteger) option.getConfig()).getMaxIntegerValue()));
    }

    public void genOptionListValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        IConfigOptionListEntry defaultValue = ((MagicConfigOptionList) option.getConfig()).getDefaultOptionListValue();
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"), this.tr(String.format("%s.label.%s.%s",
                this.getIdentifier(), option.getName(), defaultValue.getStringValue()))));
        builder.append("\n");
        List<String> values = Lists.newArrayList();
        IConfigOptionListEntry value = ((MagicConfigOptionList) option.getConfig()).getDefaultOptionListValue();

        do {
            values.add(String.format("`%s`", this.tr(String.format("%s.label.%s.%s", this.getIdentifier(), option.getName(), value.getStringValue()))));
            values.add(", ");
            value = value.cycle(true);
        } while (value != defaultValue);

        values.remove(values.size() - 1);
        builder.append(String.format("- %s: ", this.trLabel("option_value")));

        for (String string : values) {
            builder.append(string);
        }
    }

    public void genStringValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigString) option.getConfig()).getDefaultStringValue()));
    }

    public void genStringListValueEntry(@NotNull ConfigOption option, @NotNull StringBuilder builder) {
        builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                ((MagicConfigStringList) option.getConfig()).getDefaultStrings().toString()));
    }

    public StringBuilder genValueEntry(@NotNull ConfigOption option) {
        StringBuilder builder = new StringBuilder();

        if (option.getConfig() instanceof MagicConfigBoolean) {
            this.genBooleanValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigBooleanHotkeyed) {
            this.genBooleanHotkeyedValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigColor) {
            this.genColorValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigDouble) {
            this.genDoubleValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigHotkey) {
            this.genHotkeyValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigInteger) {
            this.genIntegerValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigOptionList) {
            this.genOptionListValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigString) {
            this.genStringValueEntry(option, builder);
        } else if (option.getConfig() instanceof MagicConfigStringList) {
            this.genStringListValueEntry(option, builder);
        } else {
            return null;
        }

        return builder;
    }

    @Override
    public void generateDocument() {
        String language = this.getCurrentLanguageCode().equals(ConfigDocumentGenerator.DEFAULT_LANGUAGE) ?
                "" : String.format("_%s", this.getCurrentLanguageCode().toUpperCase(Locale.ROOT));
        File file = new File(String.format("./document/%s/config%s.md", this.getIdentifier(), language));

        if (!file.exists()) {
            new File(String.format("./document/%s", this.getIdentifier())).mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(file);

            for (String category : this.configManager.getCategories()) {
                for (ConfigOption option : this.configManager.getOptionsByCategory(category).stream()
                        .sorted(Comparator.comparing(ConfigOption::getName)).collect(Collectors.toList())) {
                    fw.write(this.genConfigEntry(option));
                }
            }

            fw.flush();
            fw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
