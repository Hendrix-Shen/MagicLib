# MagicLib Malilib Extra

Malilib extension of MagicLib, providing enhanced config options: dependency checking, comment processing and usage statistics, plus the MagicLib config GUI.

## Features

- [Config Option Dependency](#config-option-dependency)

## Config Option Dependency

### Background

The config container and base interfaces of this feature reference [TweakerMore](https://github.com/Fallen-Breath/tweakermore) by [Fallen-Breath](https://github.com/Fallen-Breath), and reuse the dependency check annotations of MagicLib Core to bring dependency conditions to config options.

### What it does

Makes the [Dependency Check](../magiclib-core/README.md#dependency-check) feature work together with config options: an option declares the conditions under which it is available, and the config GUI reacts to them.

The check drives the GUI presentation only. It never rewrites a stored value, and it does not stop your own code from reading an option or triggering its hotkey — see [Notes](#notes).

#### Option visibility

`MagicConfigGui` filters an option out of the list when its dependencies are unsatisfied and `hideUnAvailableConfigs()` returns `true`. That method returns `false` in the base class, so hiding is opt-in for your GUI; the MagicLib config GUI overrides it with the `hideUnavailableConfigs` option, which defaults to `true`.

A category navigation button is dropped as well when no option of that category survives the dependency and debug/dev filters, so a fully unavailable category does not show up as an empty tab.

Options that stay visible are still marked: inside a `MagicConfigGui`, their label lines are rendered in dark red, which takes precedence over the blue of `debugOnly` options and the light purple of `devOnly` options.

#### Comment hints

When not every dependency group passes, a `Dependencies:` footer built from the check results is appended to the option comment. It is rendered as a tree of `Composite dependency` / `Or` / `Require` / `Conflict` sections with one line per condition, colored green when the condition passed and red when it failed.

Unlike a failure popup, the footer also lists the conditions that passed, so the whole declaration stays readable. It is appended as soon as one group fails, which can happen while the option itself is still available — a single passing group is enough for that.

The footer is injected through a mixin on the malilib `ConfigBase#getComment`, so it reaches every GUI that reads the option comment; call `getCommentNoFooter()` when you need the comment without it.

#### Config values

Reading the value of an option always returns the value that is actually stored, whether or not its dependencies are satisfied. Loading and saving do not filter by dependency state either, so an unavailable option keeps its value across restarts instead of falling back to the default.

### How it works

Put `@Config` and `@Dependencies` on the same static option field; `MagicConfigManager#parseConfigClass` wraps the field into a `ConfigContainer` and parses the dependency declarations on it at that point.

The object checked by the dependencies is the `ConfigContainer` itself, so a `PREDICATE` condition implements `SimplePredicate<ConfigContainer>` and can reach the option (`getConfig()`), its name, its category and its config manager.

Conditions are organized in groups, each group being wrapped by `@Dependencies`: the groups are alternatives (or) — any passing group is enough — while inside a group every `require` condition must pass and no `conflict` condition may be triggered (and). An option without any dependency declaration is always satisfied.

A condition can check mod presence, mod version, runtime distribution or loader platform; `optional` only affects the mod (`MOD_ID`) checks.

Nothing is cached: `ConfigContainer#isSatisfied()` re-evaluates the conditions on every call, so a GUI redraw always reflects the current environment.

### Examples

#### Show an option only when a matching mod version is installed

The option below is available only when `modmenu` 20.0.0 or newer is loaded.

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0"))
@Config(category = "generic")
public static final MagicConfigBoolean screenRelated = Configs.cf.newConfigBoolean("screenRelated", false);
```

#### Hide an option when a conflicting mod is installed

The option below is available only when `sodium` is not loaded.

```java
@Dependencies(conflict = @Dependency("sodium"))
@Config(category = "generic")
public static final MagicConfigBoolean legacyRenderer = Configs.cf.newConfigBoolean("legacyRenderer", false);
```

#### Show an option when a mod is missing or a matching version is installed

The option below is available when `modmenu` is missing, or when a version 20.0.0 or newer is loaded.

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0", optional = true))
@Config(category = "generic")
public static final MagicConfigBoolean screenRelated = Configs.cf.newConfigBoolean("screenRelated", false);
```

#### Composite conditions (or)

The option below is available when either `mod_a` or `mod_b` is loaded. Repeating `@Dependencies` is equivalent to wrapping the groups in `@CompositeDependencies`.

```java
@Dependencies(require = @Dependency("mod_a"))
@Dependencies(require = @Dependency("mod_b"))
@Config(category = "generic")
public static final MagicConfigBoolean eitherMod = Configs.cf.newConfigBoolean("eitherMod", false);
```

#### Custom predicate check

Point the dependency at a predicate class that provides a no-argument constructor; it receives the config container of the option.

```java
public class MyPredicate implements SimplePredicate<ConfigContainer> {
    @Override
    public boolean test(ConfigContainer container) {
        return container.getConfigManager() != null;
    }
}
```

```java
@Dependencies(require = @Dependency(
        dependencyType = DependencyType.PREDICATE,
        predicate = MyPredicate.class
))
@Config(category = "generic")
public static final MagicConfigBoolean predicated = Configs.cf.newConfigBoolean("predicated", false);
```

See [Dependency Check](../magiclib-core/README.md#examples) for more examples of the condition types shared with the other entry points.

### Notes

Options are created through the `MagicConfigFactory` of your config manager; `Configs.cf` in the examples is that factory.

The check is a GUI concern only: the hotkeys of unsatisfied options stay registered, and their values can still be read and written. Guard the behavior yourself with `ConfigContainer#isSatisfied()` when an option must not take effect.

The bottom line of `MagicConfigGui` counts an option as available only when it passes the dependency check and the debug/dev filters; everything else is reported as unavailable in its hover text.

The full semantics of the dependency check and the version predicate syntax are in the Dependency Check feature of MagicLib Core.
