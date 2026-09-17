# MagicLib Core

The MagicLib Core module, providing dependency checking, event management, I18n support, Mixin enhancements, and an abstract loader implementation.

## Features

- [Dependency Check](#dependency-check)

## Dependency Check

### Background

This feature was originally designed by [plusls](https://github.com/plusls) for his personal mods, aiming at a simple and flexible way of checking dependencies. It was later moved into MagicLib for easier maintenance and code reuse. Since then, inspired by [Fallen-Breath](https://github.com/Fallen-Breath)'s [conditional-mixin](https://github.com/Fallen-Breath/conditional-mixin), the module has been improved further.

### What it does

MagicLib provides an annotation based dependency check framework. Even in complex code, you can declare conditions on semantic version numbers, runtime distributions and loader platforms; besides, you can use predicates for even more complex checks.

This module fills the gaps of the dependency checks shipped with the loaders: for example, a missing optional dependency can pass softly instead of hard failing, and the same interface works on every loader.

Three entry points consume the declarations — the entry point check, the mixin check, and the programmatic `DependencyChecker` API. They share one set of annotations and one set of group semantics, and differ only in what they do with the result and which object a predicate receives.

#### Entry point dependency check

Before Minecraft starts, MagicLib scans the dependency declarations on the entry point methods of every loaded mod and checks whether they are satisfied; an unsatisfied check aborts the Minecraft startup and lists every failed entry in a separate window.

The check runs from the Fabric `PreLaunchEntrypoint` of MagicLib and from the `@Mod` constructor on Forge-Like platforms, so it happens before the game window appears. It runs exactly once; triggering it again throws an `IllegalStateException`.

On the client MagicLib scans `onInitialize` and `onInitializeClient`, while on the server it scans `onInitialize` and `onInitializeServer`. The declarations found on both methods are pooled into one list, which makes them alternatives: the mod passes when either method passes.

We do not recommend distribution (`DistType`) checks on environment specific entry point methods — e.g. declaring `DistType.CLIENT` on `onInitializeServer` makes the check always fail.

Which classes get scanned depends on the platform:

- **Fabric-Like**: the entry point classes declared under the `main`, `client` and `server` keys that implement `ModInitializer`, `ClientModInitializer` or `DedicatedServerModInitializer`. Only the plain class form is supported — an entry point declared as `Class::method` is skipped.
- **Forge-Like**: the `@Mod` annotated classes that implement `top.hendrixshen.magiclib.api.entrypoint.ModInitializer`, the Fabric style initializer MagicLib provides for cross-platform mods. A class without that interface is not scanned at all.

Only the first method matching a name with the `()V` descriptor is read, so an overloaded entry point method keeps a single declaration.

#### Mixin dependency check

The `MagicMixinPlugin` shipped by MagicLib integrates the dependency check: a mixin is checked when it is applied at runtime, and an unsatisfied mixin is simply not applied. Note that the mixin dependency check does not support method level or field level declarations — that would be unsafe at runtime. Declare on the mixin class instead.

Results are cached per mixin class name by the memoized checker that `MagicMixinPlugin` uses, so a mixin is evaluated once no matter how many target classes it declares. A mixin whose class node cannot be read is not applied either.

A failed mixin does not stop the game. Its reason is handed to the check failure callback, and `MagicMixinPlugin` logs it as a warning only while the mixin `DEBUG_EXPORT` environment option is enabled.

#### Programmatic check

`DependencyChecker` applies the same rules to your own code, from a `ClassNode`, `MethodNode`, `FieldNode` or a reflected `Field`:

```java
public static void check(Field field, Object obj) {
    DependencyCheckResult result = DependencyChecker.check(field, obj);

    if (!result.isSuccess()) {
        MagicLib.getLogger().warn("Dependencies not satisfied: {}", result.getReason());
    }
}
```

An element without declarations yields a successful result whose reason is `null`; a failing one yields a message tree that lists only the sections which actually failed.

### How it works

Conditions are organized in groups, each group being wrapped by `@Dependencies`: the groups are alternatives (or) — any passing group is enough — while inside a group every condition must pass (and). Repeating `@Dependencies` on one element is equivalent to bundling the groups in `@CompositeDependencies`.

An element without any dependency declaration is always satisfied.

Inside a group, every `require` entry must pass and no `conflict` entry may be triggered: a conflict entry that matches fails the whole group.

`dependencyType` selects what a condition reads and what makes it pass:

| Type               | Attributes read                          | Passes as `require` when                                    |
|--------------------|------------------------------------------|-------------------------------------------------------------|
| `MOD_ID` (default) | `value`, `versionPredicates`, `optional` | the mod is loaded and its version satisfies every predicate |
| `DIST`             | `distType`                               | the current distribution matches                            |
| `PLATFORM`         | `platformType`                           | the current loader platform matches                         |
| `PREDICATE`        | `predicate`                              | your predicate returns `true` for the checked object        |

`optional` only affects the mod (`MOD_ID`) checks: the entry passes when the mod is missing, and the version predicates are only verified while the mod is loaded — a mismatching version still fails. Used as a `conflict` entry, `optional` is ignored; the conflict is triggered when the mod is loaded with a matching version, and stays untriggered when the mod is missing or its version does not match.

`DistType.ANY` and `PlatformType.ANY` match everything, while `PlatformType.UNKNOWN` matches nothing. `FABRIC_LIKE` covers `FABRIC` and `QUILT`, and `FORGE_LIKE` covers `FORGE` and `NEOFORGE`.

A predicate entry executes your own code against the object being checked, and that object depends on the entry point: the target class `ClassNode` for mixins, the `ConfigContainer` for config options, and `null` for entry point methods. The predicate class needs a no-argument constructor, is instantiated once per parsed declaration, and must not be an interface — write predicates that tolerate a null argument when they may be shared across entry points.

The full attribute semantics live in the annotation javadocs, so they are not repeated here.

### Examples

#### Apply a Mixin only when a matching mod version is installed

The mixin below is applied only when `modmenu` 20.0.0 or newer is loaded.

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0"))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Apply a Mixin only when a mod is not installed

The mixin below is applied only when `modmenu` is not loaded.

```java
@Dependencies(conflict = @Dependency("modmenu"))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Apply a Mixin when a mod is missing or a matching version is installed

The mixin below is applied when `modmenu` is missing, or when a version 20.0.0 or newer is loaded.

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0", optional = true))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Restrict a Mixin to one loader platform

The mixin below is applied on Fabric and Quilt only.

```java
@Dependencies(require = @Dependency(
        dependencyType = DependencyType.PLATFORM,
        platformType = PlatformType.FABRIC_LIKE
))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Match a version range

The mixin below is applied when the loaded `modmenu` is at least 20.0.0 but older than 21.0.0. Several predicates in one string are space separated, and every entry of the array must match.

```java
@Dependencies(require = @Dependency(
        value = "modmenu",
        versionPredicates = {">=20.0.0", "<21.0.0"}
))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Combined conditions (and)

The mixin below is applied only when both `mod_a` and `mod_b` are loaded.

```java
@Dependencies(
        require = {
                @Dependency("mod_a"),
                @Dependency("mod_b")
        }
)
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Composite conditions (or)

The mixin below is applied when either `mod_a` or `mod_b` is loaded.

```java
@Dependencies(require = @Dependency(value = "mod_a"))
@Dependencies(require = @Dependency(value = "mod_b"))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // Your code
}
```

#### Custom predicate check

Point the dependency at a predicate class that provides a no-argument constructor. For a mixin the checked object is the `ClassNode` of the target class, which is what `MixinPredicate` binds.

```java
public class MyPredicate implements MixinPredicate {
    @Override
    public boolean test(ClassNode classNode) {
        return classNode.superName != null;
    }
}
```

```java
@Dependencies(require = @Dependency(
        dependencyType = DependencyType.PREDICATE,
        predicate = MyPredicate.class
))
public class PredicatedMixin {
}
```

#### Entry point dependency check

The entry point below prevents Minecraft from starting when `fabric-api` is missing. On Forge-Like platforms the class must also implement `top.hendrixshen.magiclib.api.entrypoint.ModInitializer` to be scanned.

```java
public class MyModInitializer implements ModInitializer {
    @Dependencies(require = @Dependency(value = "fabric-api"))
    @Override
    public void onInitialize() {
        // Your code
    }
}
```

#### Check the declaration on a field

The field below carries its own declaration, which `DependencyChecker` evaluates on demand. The second argument is the object a predicate would receive, so `null` is fine while no predicate is declared.

```java
public class MyFeatures {
    @Dependencies(conflict = @Dependency("sodium"))
    public static final boolean LEGACY_RENDERER = true;

    public static void init() throws NoSuchFieldException {
        Field field = MyFeatures.class.getDeclaredField("LEGACY_RENDERER");

        if (!DependencyChecker.check(field, null).isSuccess()) {
            return;
        }

        // Your code
    }
}
```

### Notes

A version predicate is an operator followed by a version, supporting `>=`, `<=`, `>`, `<`, `=`, `~` and `^`; separate several conditions with a space to require all of them, for example `>=1.16` or `>=1.16 <1.17`. `~` keeps the major and the minor component, `^` keeps the major one.

A trailing wildcard may replace the last component — `1.16.x`, `1.16.X` and `1.16.*` all mean `~1.16`, and `1.x` means `^1`. A wildcard needs the equality operator or no operator at all, extra trailing wildcards are dropped, and pre-release versions may not use wildcards. `*` alone, or no predicate at all, matches every version.

The version being tested must parse as a semantic version. When the version of a loaded mod does not, that check fails and an error is logged; when the version written in the predicate does not, operators that exclude both bounds (`>` and `<`) are rejected and every other operator degrades to plain string equality.

`@Dependency` declares no target of its own, so it can only appear nested inside `@Dependencies`. Both `@Dependencies` and `@CompositeDependencies` can annotate a type, a field or a method, but which of them an entry point actually reads is up to that entry point.

The entry point check runs for the entry point classes of every loaded mod before the game window appears, and aborts startup on failure. The mixin check never aborts anything: it only decides whether one mixin is applied.
