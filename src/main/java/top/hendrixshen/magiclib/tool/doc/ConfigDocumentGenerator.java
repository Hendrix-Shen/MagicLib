package top.hendrixshen.magiclib.tool.doc;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;
import top.hendrixshen.magiclib.config.*;
import top.hendrixshen.magiclib.dependency.Dependencies;
import top.hendrixshen.magiclib.dependency.Dependency;
import top.hendrixshen.magiclib.language.I18n;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ConfigDocumentGenerator extends BaseDocumentGenerator {
    public final ConfigManager configManager;
    public ConfigDocumentGenerator(@NotNull String identifier) {
        super(identifier);
        this.configManager = ConfigManager.get(identifier);
    }

    public String trConfigName(@NotNull Option option) {
        return this.tr(String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName()));
    }

    public String trConfigDesc(@NotNull Option option) {
        return this.tr(String.format("%s.config.%s.%s.comment", this.getIdentifier(), option.getCategory(), option.getName()));
    }

    public String trLabel(String label) {
        return this.tr(String.format("%s.document.config.label.%s", MagicLibReference.getModId(), label));
    }

    public String trConfigCategory(@NotNull Option option) {
        return this.tr(String.format("%s.gui.button.tab.%s", this.getIdentifier(), option.getCategory()));
    }

    public String trType(String type) {
        return this.tr(String.format("%s.document.config.type.%s", MagicLibReference.getModId(), type));
    }

    private String trType(@NotNull Option option) {
        if (option.getConfig() instanceof TranslatableConfigBoolean) {
            return this.trType("boolean");
        } else if (option.getConfig() instanceof TranslatableConfigBooleanHotkeyed) {
            return this.trType("boolean_with_hotkey");
        } else if (option.getConfig() instanceof TranslatableConfigColor) {
            return this.trType("color");
        } else if (option.getConfig() instanceof TranslatableConfigDouble) {
            return this.trType("double");
        } else if (option.getConfig() instanceof TranslatableConfigHotkey) {
            return this.trType("hotkey");
        } else if (option.getConfig() instanceof TranslatableConfigInteger) {
            return this.trType("integer");
        } else if (option.getConfig() instanceof TranslatableConfigOptionList) {
            return this.trType("enum");
        } else if (option.getConfig() instanceof TranslatableConfigString) {
            return this.trType("string");
        } else if (option.getConfig() instanceof TranslatableConfigStringList) {
            return this.trType("string_list");
        }
        MagicLibReference.LOGGER.error("Unknown type {}: {}", this.trConfigName(option), option.getConfig().getType());
        return this.trType("unknown");
    }

    public String defaultConfigName(@NotNull Option option) {
        return I18n.exists(WrapperSettingManager.DEFAULT_LANGUAGE, String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName())) ?
                I18n.getByCode(ConfigDocumentGenerator.DEFAULT_LANGUAGE ,String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName())) : option.getName();
    }

    public String getTranslatedConfigName(Option option) {
        return (!this.getCurrentLanguageCode().equals(ConfigDocumentGenerator.DEFAULT_LANGUAGE) &&
                I18n.exists(this.getCurrentLanguageCode(), String.format("%s.config.%s.%s.name", this.getIdentifier(), option.getCategory(), option.getName()))) ?
                String.format("%s (%s)", this.trConfigName(option), this.defaultConfigName(option)) :
                this.defaultConfigName(option);
    }

    public String genConfigEntry(Option option) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("## %s", this.getTranslatedConfigName(option)));
        builder.append("\n");
        builder.append(this.trConfigDesc(option));
        builder.append("\n");
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

    private @Nullable StringBuilder genDependenciesEntry(@NotNull Option option) {
        Dependencies<Option> dependencies = option.getModDependencies();
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

    public StringBuilder genValueEntry(@NotNull Option option) {
        StringBuilder builder = new StringBuilder();
        if (option.getConfig() instanceof TranslatableConfigBoolean) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigBoolean) option.getConfig()).getStringValue()));
        } else if (option.getConfig() instanceof TranslatableConfigBooleanHotkeyed) {
            builder.append(String.format("- %s: `%s`, `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigBooleanHotkeyed) option.getConfig()).getDefaultStringValue(),
                    ((TranslatableConfigBooleanHotkeyed) option.getConfig()).getKeybind().getStringValue().equals("")
                    ? this.trLabel("no_key") : ((TranslatableConfigBooleanHotkeyed) option.getConfig()).getKeybind().getStringValue()));
        } else if (option.getConfig() instanceof TranslatableConfigColor) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigColor) option.getConfig()).getDefaultStringValue()));
        } else if (option.getConfig() instanceof TranslatableConfigDouble) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigDouble) option.getConfig()).getDefaultStringValue()));
            builder.append("\n");
            builder.append(String.format("- %s: `%s`", this.trLabel("min_value"),
                    ((TranslatableConfigDouble) option.getConfig()).getMinDoubleValue()));
            builder.append("\n");
            builder.append(String.format("- %s: `%s`", this.trLabel("max_value"),
                    ((TranslatableConfigDouble) option.getConfig()).getMaxDoubleValue()));
        } else if (option.getConfig() instanceof TranslatableConfigHotkey) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigHotkey) option.getConfig()).getDefaultStringValue().equals("") ?
                    this.trLabel("no_key") : ((TranslatableConfigHotkey) option.getConfig()).getDefaultStringValue()));
        } else if (option.getConfig() instanceof TranslatableConfigInteger) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigInteger) option.getConfig()).getDefaultStringValue()));
            builder.append("\n");
            builder.append(String.format("- %s: `%s`", this.trLabel("min_value"),
                    ((TranslatableConfigInteger) option.getConfig()).getMinIntegerValue()));
            builder.append("\n");
            builder.append(String.format("- %s: `%s`", this.trLabel("max_value"),
                    ((TranslatableConfigInteger) option.getConfig()).getMaxIntegerValue()));
        } else if (option.getConfig() instanceof TranslatableConfigOptionList) {
            IConfigOptionListEntry defaultValue = ((TranslatableConfigOptionList) option.getConfig()).getDefaultOptionListValue();
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"), defaultValue.getDisplayName()));
            builder.append("\n");
            List<String> values = Lists.newArrayList();
            IConfigOptionListEntry value = ((TranslatableConfigOptionList) option.getConfig()).getDefaultOptionListValue();
            do {
                values.add(String.format("`%s`", value.getDisplayName()));
                values.add(", ");
                value = value.cycle(true);
            } while (value != defaultValue);
            values.remove(values.size() - 1);
            builder.append(String.format("- %s: ", this.trLabel("option_value")));
            for (String string : values) {
                builder.append(string);
            }
        } else if (option.getConfig() instanceof TranslatableConfigString) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigString) option.getConfig()).getDefaultStringValue()));
        } else if (option.getConfig() instanceof TranslatableConfigStringList) {
            builder.append(String.format("- %s: `%s`", this.trLabel("default_value"),
                    ((TranslatableConfigStringList) option.getConfig()).getDefaultStrings().toString()));
        } else {
            return null;
        }
        return builder;
    }

    public void genFile() {
        String language = this.getCurrentLanguageCode().equals(ConfigDocumentGenerator.DEFAULT_LANGUAGE) ?
                "" : String.format("_%s", this.getCurrentLanguageCode().toUpperCase(Locale.ROOT));
        File file = new File(String.format("./document/%s/config%s.md", this.getIdentifier(), language));
        if (!file.exists()) {
            new File(String.format("./document/%s", this.getIdentifier())).mkdirs();
        }

        try {
            FileWriter fw = new FileWriter(file);
            for (String category : this.configManager.getCategories()) {
                for (Option option : this.configManager.getOptionsByCategory(category)) {
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
