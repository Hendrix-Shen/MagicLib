# MagicLib Core

MagicLib Core 模块，提供依赖检查、事件管理、I18n 支持、Mixin 增强功能以及抽象加载器的实现。

## 功能

- [依赖检查](#依赖检查)

## 依赖检查

### 背景

该功能最初由 [plusls](https://github.com/plusls) 为其个人模组设计，旨在提供一种简单且灵活的依赖检查方式；为便于维护与复用代码，随后被迁移至 MagicLib。在此之后，受 [Fallen-Breath](https://github.com/Fallen-Breath) 的 [conditional-mixin](https://github.com/Fallen-Breath/conditional-mixin) 启发，该模块又得到了进一步的改进。

### 功能简介

MagicLib 提供注解风格的依赖检查框架。即使在复杂的代码中，您也可以声明语义版本号、运行环境与加载器环境等条件；此外，您还可以通过谓词实现更为复杂的检查。

该模块弥补了各加载器自带依赖检查的不足之处：例如，可选依赖缺失时检查能够软性通过（不会导致硬失败），并且在不同加载器上提供一致的接口。

#### 入口点依赖检查

在 Minecraft 启动之前，MagicLib 会扫描所有入口点方法上的依赖声明并检查其是否满足；检查不通过将终止 Minecraft 启动，并在一个独立窗口中展示所有失败的条目。

请注意，MagicLib 在客户端会扫描 `onInitialize` 与 `onInitializeClient`，在服务端则扫描 `onInitialize` 与 `onInitializeServer`。我们不建议在特定环境的入口点方法上使用运行环境（`DistType`）检查——例如在 `onInitializeServer` 上声明 `DistType.CLIENT`，该检查将始终失败。

在 Forge-Like 平台上，入口点类需要实现 `top.hendrixshen.magiclib.api.entrypoint.ModInitializer` 才能激活入口点依赖检查。

#### Mixin 依赖检查

MagicLib 提供的 `MagicMixinPlugin` 集成了依赖检查功能：mixin 在运行时被应用时会进行依赖检查。注意，mixin 依赖检查不支持方法级与字段级的声明——这在运行时是不安全的。

### 机制概述

条件按组组织，每组条件由 `@Dependencies` 包含：组间为或，任意一组通过即可；组内为与，所有条件都必须通过。

条件可检查模组是否存在、模组版本、运行环境或加载器平台。

`optional` 仅对模组（`MOD_ID`）检查生效：模组缺失时该条目通过；仅当模组已加载时才校验版本谓词，版本不满足仍判定失败。

谓词条目会对被检查的对象执行您自己的代码。

完整属性语义见注解 javadoc，此处不再罗列。

### 示例

#### 仅在模组安装且满足特定版本时应用 Mixin

下面的 mixin 仅在加载了版本大于等于 20.0.0 的 `modmenu` 时应用。

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0"))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // 您的代码
}
```

#### 仅在未安装模组时应用 Mixin

下面的 mixin 仅在未加载 `modmenu` 时应用。

```java
@Dependencies(conflict = @Dependency("modmenu"))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // 您的代码
}
```

#### 模组缺失或满足特定版本时均可应用 Mixin

下面的 mixin 在未加载 `modmenu`，或加载了版本大于等于 20.0.0 的 `modmenu` 时应用。

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0", optional = true))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // 您的代码
}
```

#### 组合条件（与）

下面的 mixin 仅在同时加载 `mod_a` 与 `mod_b` 时应用。

```java
@Dependencies(
        require = {
                @Dependency("mod_a"),
                @Dependency("mod_b")
        }
)
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // 您的代码
}
```

#### 复合条件（或）

下面的 mixin 在加载了 `mod_a` 或 `mod_b` 其中之一时应用。

```java
@Dependencies(require = @Dependency(value = "mod_a"))
@Dependencies(require = @Dependency(value = "mod_b"))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // 您的代码
}
```

#### 自定义谓词检查

让依赖指向一个提供无参构造器的谓词类。

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

#### 入口点依赖检查

下面的入口点在缺少 `fabric-api` 时无法启动 Minecraft。

```java
public class MyModInitializer implements ModInitializer {
    @Dependencies(require = @Dependency(value = "fabric-api"))
    @Override
    public void onInitialize() {
        // 您的代码
    }
}
```

### 说明

版本谓词由运算符加版本号构成，支持 `>=`、`<=`、`>`、`<`、`=`、`~` 与 `^`，用空格分隔多个条件表示需要同时满足，例如 `>=1.16` 或 `>=1.16 <1.17`。

入口点检查会在游戏窗口显示前，对所有已加载的基于 MagicLib 的模组执行。
