# MagicLib

[![Minecraft](http://cf.way2muchnoise.eu/versions/Minecraft_576459_all.svg?badge_style=flat)](https://www.curseforge.com/minecraft/mc-mods/magiclib/files)
[![License](https://img.shields.io/github/license/Hendrix-Shen/MagicLib?label=License&style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/blob/master/LICENSE)
![Languages](https://img.shields.io/github/languages/top/Hendrix-Shen/MagicLib?style=flat-square)
![Java](https://img.shields.io/badge/Java-8%20%7C%209%20%7C%2010%20%7C%2011%20%7C%2012%20%7C%2013%20%7C%2014%20%7C%2015%20%7C%2016%20%7C%2017%20%7C%2018-orange?style=flat-square)
[![Issues](https://img.shields.io/github/issues/Hendrix-Shen/MagicLib?style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/issues)
[![Pull Requests](https://img.shields.io/github/issues-pr/Hendrix-Shen/MagicLib?style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/pulls)
[![Public Beta](https://img.shields.io/github/workflow/status/Hendrix-Shen/MagicLib/CI?label=Last%20building&style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/actions/workflows/CI.yml)
[![Stable Release](https://img.shields.io/github/v/release/Hendrix-Shen/MagicLib?label=Stable%20Release&style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/releases)
[![Development Release Downloads](https://img.shields.io/github/v/release/Hendrix-Shen/MagicLib?include_prereleases&label=Development%20Release&style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/releases)
[![Github Release Downloads](https://img.shields.io/github/downloads/Hendrix-Shen/MagicLib/total?label=Github%20Release%20Downloads&style=flat-square)](https://github.com/Hendrix-Shen/MagicLib/releases)
[![Modrinth Downloads](https://img.shields.io/modrinth/dt/mv1zH6ln?label=Modrinth%20Downloads&logo=Modrinth%20Downloads&style=flat-square)](https://modrinth.com/mod/magiclib)
[![CurseForge Downloads](http://cf.way2muchnoise.eu/576459.svg?badge_style=flat)](https://www.curseforge.com/minecraft/mc-mods/magiclib)

[English](./README.md)

**警告: 此项目仍然处于早期开发阶段。**

## 依赖库

| 依赖       | 类型  | 环境  | 链接                                                                                                                     |
|----------|-----|-----|------------------------------------------------------------------------------------------------------------------------|
| Malilib  | 必须  | 客户端 | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/malilib)                                                     |
| Mod Menu | 可选  | 客户端 | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/modmenu) &#124; [Modrinth](https://modrinth.com/mod/modmenu) |

## 描述

一个多功能的模组依赖库。

## 功能

### 兼容 API
- 多版本兼容，是的，我们使用了一些手段来兼容Fabric Loader上运行的所有Minecraft版本（1.14+）。这是怎么做到的？魔法:(
- 我们编写了 兼容API，用于平衡各版本间的差异。对于同时维护多个MC版本模组开发者来说，可以在一定程度上忽视 Mojang 对于 MC 的更改。你不必关心 Mojang 做了什么，使用 API 你将轻松地使不同MC版本间使用相同的代码。

### 依赖检查

我们加入了一套完整的依赖检查系统，通过表达式，甚至自定义谓词来验证依赖可用性，他可以应用于多种场景，例如：
- MagicLibMixinPlugin为模组提供了额外的依赖检查，它弥补了Fabric Loader的一些缺陷，例如，他为客户端侧和服务端侧设置不同的依赖。
- 依赖检查同样可以应用在Mixin上，仅当条件满足时，Mixin才会被应用。
- 依赖检查同样可以应用配置管理上，以实现仅当满足条件时，该配置项才会被展示。
- 在以后的开发中可能还包含更多的功能.

### I18n
- 我们重新实现了独立于MC的I18n，并且可以设置 MagicLib I18n 的备用语言列表。Mojang在对待I18n文本使用了一些手段，使得String.format的部分特性不可用，这通常会让人很恼火。

### Malilib 扩展
- 我们为 Malilib 编写了一个非常实用的配置管理模块，我们将尽可能的使用基本数据类型与 Java注解 来生成配置清单。同时对于配置文件，我们加入了配置版本系统，这将有助于您在配置文件结构发生重大变动时，快速的编写您自定义的迁移解决方案。
- 我们移植了一些来自高版本Malilib的特性，以便于在使用适配较低版本 Minecraft 的 Malilib 时，使用同样的特性。

## 开发

### 支持

当前主开发版本：1.19.2

并且使用 `预处理` 来兼容各版本。

**注意: 我仅接受以下版本的议题，也就是每个MC主要版本的最后一次更新。请注意该信息的时效性，任何不在此列出的版本议题均会被关闭。**

- Minecraft 1.14.4
- Minecraft 1.15.2
- Minecraft 1.16.5
- Minecraft 1.17.1
- Minecraft 1.18.2
- Minecraft 1.19.2

### 混淆映射表

我们使用 **Mojang 官方** 混淆映射表来反混淆 Minecraft 并插入补丁程序。

### 文档

英文文档与中文文档是逐行对应的。

## 许可

此项目在 LGPLv3许可证 下可用。 从中学习，并将其融入到您自己的项目中。