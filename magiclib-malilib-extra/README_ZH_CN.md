# MagicLib Malilib Extra

MagicLib 的 Malilib 扩展，提供增强的配置选项：依赖检查、注释处理与使用统计，以及 MagicLib 配置 GUI。

## 功能

- [配置选项依赖](#配置选项依赖)

## 配置选项依赖

### 背景

该功能的配置容器与基础接口参考 [Fallen-Breath](https://github.com/Fallen-Breath) 的 [TweakerMore](https://github.com/Fallen-Breath/tweakermore)，并复用 MagicLib Core 的依赖检查注解，将依赖条件引入配置选项。

### 功能简介

使 [依赖检查](../magiclib-core/README_ZH_CN.md#依赖检查) 功能能够与配置选项一起工作。

#### 选项可见性

当依赖条件不满足时，MagicConfigGui 默认会隐藏该选项；若关闭 MagicLib 配置中的 `hideUnavailableConfigs`，该选项将以深红色显示。

#### 注释提示

当依赖条件不满足时，选项注释的末尾会附带依赖检查结果，说明是哪一项依赖失败，让玩家明白它为何不可用。

#### 配置值

即使依赖条件不满足，直接读取配置选项的值也将返回实际值，不会因为依赖条件不满足而返回默认值。

### 机制概述

将 `@Config` 与 `@Dependencies` 标注在同一个静态选项字段上，配置管理器在注册时会解析字段上的依赖声明。

依赖检查的对象是配置项容器本身，谓词可以访问它。

条件按组组织，每组条件由 `@Dependencies` 包含：组间为或，任意一组通过即可；组内为与，所有条件都必须通过。

条件可检查模组是否存在、模组版本、运行环境或加载器平台；`optional` 仅对模组（`MOD_ID`）检查生效。

### 示例

#### 仅在模组安装且满足特定版本时显示选项

下面的选项仅在加载了版本大于等于 20.0.0 的 `modmenu` 时可用。

```java
@Dependencies(require = @Dependency(value = "modmenu", versionPredicates = ">=20.0.0"))
@Config(category = "general")
public static final MagicConfigBoolean screenRelated = Configs.cf.newConfigBoolean("screenRelated", false);
```

更多示例请参考 [依赖检查](../magiclib-core/README_ZH_CN.md#示例)。 

### 说明

选项通过您的配置管理器的 `MagicConfigFactory` 创建，示例中的 `Configs.cf` 即该工厂。

依赖检查的完整语义与更多条件类型见 MagicLib Core 的依赖检查功能。
