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

#### Entry point dependency check

Before Minecraft starts, MagicLib scans the dependency declarations on every entry point method and checks whether they are satisfied; an unsatisfied check aborts the Minecraft startup and lists every failed entry in a separate window.

Note that on the client MagicLib scans `onInitialize` and `onInitializeClient`, while on the server it scans `onInitialize` and `onInitializeServer`. We do not recommend distribution (`DistType`) checks on environment specific entry point methods — e.g. declaring `DistType.CLIENT` on `onInitializeServer` makes the check always fail.

On Forge-Like platforms, the entry point class must implement `top.hendrixshen.magiclib.api.entrypoint.ModInitializer` to activate the entry point dependency check.

#### Mixin dependency check

The `MagicMixinPlugin` shipped by MagicLib integrates the dependency check: a mixin is checked when it is applied at runtime. Note that the mixin dependency check does not support method level or field level declarations — that would be unsafe at runtime.

### How it works

Conditions are organized in groups, each group being wrapped by `@Dependencies`: the groups are alternatives (or) — any passing group is enough — while inside a group every condition must pass (and).

A condition can check mod presence, mod version, runtime distribution or loader platform.

`optional` only affects the mod (`MOD_ID`) checks: the entry passes when the mod is missing, and the version predicates are only verified while the mod is loaded — a mismatching version still fails.

A predicate entry executes your own code against the object being checked.

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

Point the dependency at a predicate class that provides a no-argument constructor.

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

The entry point below prevents Minecraft from starting when `fabric-api` is missing.

```java
public class MyModInitializer implements ModInitializer {
    @Dependencies(require = @Dependency(value = "fabric-api"))
    @Override
    public void onInitialize() {
        // Your code
    }
}
```

### Notes

A version predicate is an operator followed by a version, supporting `>=`, `<=`, `>`, `<`, `=`, `~` and `^`; separate several conditions with a space to require all of them, for example `>=1.16` or `>=1.16 <1.17`.

The entry point check runs for every loaded MagicLib based mod before the game window appears.
