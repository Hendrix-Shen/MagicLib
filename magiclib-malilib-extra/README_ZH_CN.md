# MagicLib Malilib Extra

MagicLib 的 Malilib 扩展，提供增强的配置选项：依赖检查、注释处理与使用统计，以及 MagicLib 配置 GUI。

## 功能

- [配置选项依赖](#配置选项依赖)

## 配置选项依赖

### 背景

该功能的配置容器与基础接口参考 [Fallen-Breath](https://github.com/Fallen-Breath) 的 [TweakerMore](https://github.com/Fallen-Breath/tweakermore)，并复用 MagicLib Core 的依赖检查注解，将依赖条件引入配置选项。

### 功能简介

使 [依赖检查](../magiclib-core/README_ZH_CN.md#依赖检查) 功能能够与配置选项一起工作：由选项声明它在何种条件下可用，配置 GUI 再据此作出响应。

检查只影响 GUI 的呈现。它既不会改写已存储的值，也不会阻止您自己的代码读取选项或触发其热键——详见 [说明](#说明)。

#### 选项可见性

当选项的依赖条件不满足且 `hideUnAvailableConfigs()` 返回 `true` 时，`MagicConfigGui` 会将该选项从列表中过滤掉。该方法在基类中返回 `false`，因此隐藏行为对您的 GUI 而言是可选择的；MagicLib 配置 GUI 用 `hideUnavailableConfigs` 选项覆写了它，其默认值为 `true`。

当某个分类下没有任何选项能通过依赖与调试/开发环境过滤时，该分类的导航按钮也会一并移除，因此完全不可用的分类不会显示为空标签页。

仍然可见的选项也会被标记：在 `MagicConfigGui` 中，其标签文本行以深红色渲染，该优先级高于 `debugOnly` 选项的蓝色与 `devOnly` 选项的浅紫色。

#### 注释提示

当并非所有依赖组都通过时，选项注释末尾会附加一段由检查结果构建的 `Dependencies:`（中文界面为「依赖:」）页脚。它以 `复合依赖` / `或` / `必要` / `冲突` 分区构成的树形呈现，每个条件占一行，条件通过为绿色，失败为红色。

与失败弹窗不同，页脚也会列出已通过的条件，因此整份声明都保持可读。只要有任意一组失败就会附加页脚，而选项本身仍可能是可用的——一组通过即已足够。

页脚通过对 malilib `ConfigBase#getComment` 的 mixin 注入，因此所有读取选项注释的 GUI 都能获得它；若您需要不含页脚的注释，请调用 `getCommentNoFooter()`。

#### 配置值

读取选项的值时，无论其依赖条件是否满足，返回的都是实际存储的值。加载与保存同样不会按依赖状态过滤，因此不可用的选项在重启后仍会保留其值，而不会回退为默认值。

### 机制概述

将 `@Config` 与 `@Dependencies` 标注在同一个静态选项字段上；`MagicConfigManager#parseConfigClass` 会把该字段包装为 `ConfigContainer`，并在此时解析其上的依赖声明。

依赖检查的对象是 `ConfigContainer` 本身，因此 `PREDICATE` 条件实现 `SimplePredicate<ConfigContainer>`，可访问该选项（`getConfig()`）、其名称、所属分类与配置管理器。

条件按组组织，每组条件由 `@Dependencies` 包含：组间为或，任意一组通过即可；组内为与，所有 `require` 条件都必须通过且所有 `conflict` 条件都不得被触发。未声明任何依赖的选项始终视为满足。

条件可检查模组是否存在、模组版本、运行环境或加载器平台；`optional` 仅对模组（`MOD_ID`）检查生效。

检查结果不会被缓存：`ConfigContainer#isSatisfied()` 在每次调用时重新求值，因此 GUI 重绘总是反映当前环境。

### 示例

#### 仅在模组安装且满足特定版本时显示选项

下面的选项仅在加载了版本大于等于 20.0.0 的 `modmenu` 时可用。

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0"))
@Config(category = "generic")
public static final MagicConfigBoolean screenRelated = Configs.cf.newConfigBoolean("screenRelated", false);
```

#### 在存在冲突模组时隐藏选项

下面的选项仅在未加载 `sodium` 时可用。

```java
@Dependencies(conflict = @Dependency("sodium"))
@Config(category = "generic")
public static final MagicConfigBoolean legacyRenderer = Configs.cf.newConfigBoolean("legacyRenderer", false);
```

#### 模组缺失或满足特定版本时均显示选项

下面的选项在未加载 `modmenu`，或加载了版本大于等于 20.0.0 的 `modmenu` 时可用。

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0", optional = true))
@Config(category = "generic")
public static final MagicConfigBoolean screenRelated = Configs.cf.newConfigBoolean("screenRelated", false);
```

#### 复合条件（或）

下面的选项在加载了 `mod_a` 或 `mod_b` 其中之一时可用。重复标注 `@Dependencies` 等价于将这些组包裹在 `@CompositeDependencies` 中。

```java
@Dependencies(require = @Dependency("mod_a"))
@Dependencies(require = @Dependency("mod_b"))
@Config(category = "generic")
public static final MagicConfigBoolean eitherMod = Configs.cf.newConfigBoolean("eitherMod", false);
```

#### 自定义谓词检查

让依赖指向一个提供无参构造器的谓词类；该谓词会收到选项的配置容器。

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

更多与其他入口点共用的条件类型示例，请参考 [依赖检查](../magiclib-core/README_ZH_CN.md#示例)。

### 说明

选项通过您的配置管理器的 `MagicConfigFactory` 创建，示例中的 `Configs.cf` 即该工厂。

检查仅关乎 GUI：依赖不满足的选项，其热键仍处于注册状态，其值仍可读写。当某选项不应生效时，请自行用 `ConfigContainer#isSatisfied()` 加以保护。

`MagicConfigGui` 的底部统计行仅在选项通过依赖检查与调试/开发环境过滤时将其计为可用；其余选项都会在其悬停文本中被报告为不可用。

依赖检查的完整语义与版本谓词语法见 MagicLib Core 的依赖检查功能。
