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

共有三个入口点会消费这些声明——入口点检查、mixin 检查，以及编程式的 `DependencyChecker` API。它们共用同一套注解与同一套分组语义，区别仅在于如何处理检查结果，以及谓词会收到什么对象。

#### 入口点依赖检查

在 Minecraft 启动之前，MagicLib 会扫描所有已加载模组的入口点方法上的依赖声明并检查其是否满足；检查不通过将终止 Minecraft 启动，并在一个独立窗口中展示所有失败的条目。

该检查在 Fabric 上由 MagicLib 的 `PreLaunchEntrypoint` 触发，在 Forge-Like 平台上则由 `@Mod` 构造器触发，因此都发生在游戏窗口出现之前。它只会执行一次；重复触发会抛出 `IllegalStateException`。

MagicLib 在客户端会扫描 `onInitialize` 与 `onInitializeClient`，在服务端则扫描 `onInitialize` 与 `onInitializeServer`。两个方法上找到的声明会被汇总到同一个列表中，因此它们互为备选：任意一个方法通过，该模组即通过。

我们不建议在特定环境的入口点方法上使用运行环境（`DistType`）检查——例如在 `onInitializeServer` 上声明 `DistType.CLIENT`，该检查将始终失败。

具体扫描哪些类取决于平台：

- **Fabric-Like**：在 `main`、`client` 与 `server` 键下声明，且实现了 `ModInitializer`、`ClientModInitializer` 或 `DedicatedServerModInitializer` 的入口点类。仅支持纯类名形式——以 `Class::method` 形式声明的入口点会被跳过。
- **Forge-Like**：标注了 `@Mod` 且实现了 `top.hendrixshen.magiclib.api.entrypoint.ModInitializer`（MagicLib 为跨平台模组提供的 Fabric 风格初始化器）的类。未实现该接口的类完全不会被扫描。

只有名称匹配且描述符为 `()V` 的第一个方法会被读取，因此重载的入口点方法只会保留一份声明。

#### Mixin 依赖检查

MagicLib 提供的 `MagicMixinPlugin` 集成了依赖检查功能：mixin 在运行时被应用时会进行依赖检查，依赖不满足的 mixin 将直接不被应用。注意，mixin 依赖检查不支持方法级与字段级的声明——这在运行时是不安全的。请改为在 mixin 类上声明。

`MagicMixinPlugin` 使用的记忆化检查器会按 mixin 类名缓存结果，因此无论一个 mixin 声明了多少个目标类，它都只被求值一次。无法读取到类节点的 mixin 同样不会被应用。

mixin 检查失败不会终止游戏。失败原因会交给检查失败回调，而 `MagicMixinPlugin` 仅在 mixin 的 `DEBUG_EXPORT` 环境选项启用时才将其记录为警告。

#### 编程式检查

`DependencyChecker` 将同样的规则应用于您自己的代码，支持 `ClassNode`、`MethodNode`、`FieldNode` 或反射得到的 `Field`：

```java
public static void check(Field field, Object obj) {
    DependencyCheckResult result = DependencyChecker.check(field, obj);

    if (!result.isSuccess()) {
        MagicLib.getLogger().warn("Dependencies not satisfied: {}", result.getReason());
    }
}
```

未声明任何依赖的元素会得到一个 `reason` 为 `null` 的成功结果；失败的元素则会得到一棵仅列出实际失败分区的消息树。

### 机制概述

条件按组组织，每组条件由 `@Dependencies` 包含：组间为或，任意一组通过即可；组内为与，所有条件都必须通过。在同一元素上重复标注 `@Dependencies`，等价于将这些组包裹在 `@CompositeDependencies` 中。

未声明任何依赖的元素始终视为满足。

组内所有 `require` 条目都必须通过，且所有 `conflict` 条目都不得被触发：任意一个 conflict 条目被命中都会使整组失败。

`dependencyType` 决定条件读取哪些属性，以及何时判定通过：

| 类型             | 读取的属性                               | 作为 `require` 时的通过条件     |
|------------------|------------------------------------------|---------------------------------|
| `MOD_ID`（默认） | `value`、`versionPredicates`、`optional` | 模组已加载且其版本满足所有谓词  |
| `DIST`           | `distType`                               | 当前运行环境匹配                |
| `PLATFORM`       | `platformType`                           | 当前加载器平台匹配              |
| `PREDICATE`      | `predicate`                              | 您的谓词对被检查对象返回 `true` |

`optional` 仅对模组（`MOD_ID`）检查生效：模组缺失时该条目通过；仅当模组已加载时才校验版本谓词，版本不满足仍判定失败。作为 `conflict` 条目时 `optional` 会被忽略：模组已加载且版本匹配才算触发冲突，模组缺失或版本不匹配则不触发。

`DistType.ANY` 与 `PlatformType.ANY` 匹配一切，而 `PlatformType.UNKNOWN` 不匹配任何平台。`FABRIC_LIKE` 涵盖 `FABRIC` 与 `QUILT`，`FORGE_LIKE` 涵盖 `FORGE` 与 `NEOFORGE`。

谓词条目会对被检查的对象执行您自己的代码，而该对象取决于入口点：mixin 为目标类的 `ClassNode`，配置选项为 `ConfigContainer`，入口点方法则为 `null`。谓词类需要提供无参构造器，每条被解析的声明都会实例化一次，且不能是接口——若谓词可能跨入口点共用，请让它能够容忍参数为 null。

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

#### 将 Mixin 限定在单一加载器平台

下面的 mixin 仅在 Fabric 与 Quilt 上应用。

```java
@Dependencies(require = @Dependency(
        dependencyType = DependencyType.PLATFORM,
        platformType = PlatformType.FABRIC_LIKE
))
@Mixin(Screen.class)
public abstract class ScreenMixin {
    // 您的代码
}
```

#### 匹配版本区间

下面的 mixin 在加载的 `modmenu` 版本大于等于 20.0.0 且小于 21.0.0 时应用。同一字符串内的多个谓词以空格分隔，数组中的每一项都必须匹配。

```java
@Dependencies(require = @Dependency(
        value = "modmenu",
        versionPredicates = {">=20.0.0", "<21.0.0"}
))
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

让依赖指向一个提供无参构造器的谓词类。对 mixin 而言，被检查的对象是目标类的 `ClassNode`，这正是 `MixinPredicate` 所绑定的类型。

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

下面的入口点在缺少 `fabric-api` 时无法启动 Minecraft。在 Forge-Like 平台上，该类还需要实现 `top.hendrixshen.magiclib.api.entrypoint.ModInitializer` 才会被扫描。

```java
public class MyModInitializer implements ModInitializer {
    @Dependencies(require = @Dependency(value = "fabric-api"))
    @Override
    public void onInitialize() {
        // 您的代码
    }
}
```

#### 检查字段上的声明

下面的字段自带一份声明，由 `DependencyChecker` 按需求值。第二个参数是谓词将会收到的对象，因此未声明谓词时传 `null` 即可。

```java
public class MyFeatures {
    @Dependencies(conflict = @Dependency("sodium"))
    public static final boolean LEGACY_RENDERER = true;

    public static void init() throws NoSuchFieldException {
        Field field = MyFeatures.class.getDeclaredField("LEGACY_RENDERER");

        if (!DependencyChecker.check(field, null).isSuccess()) {
            return;
        }

        // 您的代码
    }
}
```

### 说明

版本谓词由运算符加版本号构成，支持 `>=`、`<=`、`>`、`<`、`=`、`~` 与 `^`，用空格分隔多个条件表示需要同时满足，例如 `>=1.16` 或 `>=1.16 <1.17`。`~` 保留主版本与次版本，`^` 仅保留主版本。

末尾的通配符可以替代最后一个版本段——`1.16.x`、`1.16.X` 与 `1.16.*` 均等价于 `~1.16`，`1.x` 等价于 `^1`。通配符必须搭配等号运算符或完全不写运算符，多余的末尾通配符会被丢弃，预发布版本不允许使用通配符。单独一个 `*`，或完全不写谓词，表示匹配所有版本。

被检验的版本号必须能解析为语义版本。已加载模组的版本无法解析时，该检查判定失败并记录一条错误日志；谓词中书写的版本号无法解析时，排除两侧边界的运算符（`>` 与 `<`）会被拒绝，其余运算符一律退化为纯字符串相等比较。

`@Dependency` 自身不声明任何作用目标，因此只能嵌套在 `@Dependencies` 内部使用。`@Dependencies` 与 `@CompositeDependencies` 都可标注在类型、字段或方法上，但各入口点实际读取哪些位置由该入口点决定。

入口点检查会在游戏窗口显示前，对所有已加载模组的入口点类执行，失败即终止启动。mixin 检查则永远不会终止任何事情：它只决定单个 mixin 是否被应用。
