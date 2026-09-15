# MagicLib Malilib Extra

Malilib extension of MagicLib, providing enhanced config options: dependency checking, comment processing and usage statistics, plus the MagicLib config GUI.

## Features

- [Config Option Dependency](#config-option-dependency)

## Config Option Dependency

### Background

The config container and base interfaces of this feature reference [TweakerMore](https://github.com/Fallen-Breath/tweakermore) by [Fallen-Breath](https://github.com/Fallen-Breath), and reuse the dependency check annotations of MagicLib Core to bring dependency conditions to config options.

### What it does

Makes the [Dependency Check](../magiclib-core/README.md#dependency-check) feature work together with config options.

#### Option visibility

When the dependencies are not satisfied, MagicConfigGui hides the option by default; with `hideUnavailableConfigs` disabled in the MagicLib config, the option is shown in dark red.

#### Comment hints

When the dependencies are not satisfied, the end of the option comment carries the dependency check results, telling which dependency failed and why the option is unavailable.

#### Config values

Even when the dependencies are not satisfied, reading the value of the config option directly still returns the actual value; it does not fall back to the default value.

### How it works

Put `@Config` and `@Dependencies` on the same static option field; the config manager parses the dependency declarations on the field while registering it.

The object checked by the dependencies is the config container itself, which predicates can access.

Conditions are organized in groups, each group being wrapped by `@Dependencies`: the groups are alternatives (or) — any passing group is enough — while inside a group every condition must pass (and).

A condition can check mod presence, mod version, runtime distribution or loader platform; `optional` only affects the mod (`MOD_ID`) checks.

### Examples

#### Show an option only when a matching mod version is installed

The option below is available only when `modmenu` 20.0.0 or newer is loaded.

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0"))
@Config(category = "general")
public static final MagicConfigBoolean screenRelated = Configs.cf.newConfigBoolean("screenRelated", false);
```

See [Dependency Check](../magiclib-core/README.md#examples) for more examples.

### Notes

Options are created through the `MagicConfigFactory` of your config manager; `Configs.cf` in the examples is that factory.

The full semantics of the dependency check and more condition types are in the Dependency Check feature of MagicLib Core.
