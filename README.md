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

[中文](./README_ZH_CN.md)

**Warning: The project is still in the early development stage.**

## Dependencies

| Dependency | Type     | Environment | Link                                                                                                                   |
|------------|----------|-------------|------------------------------------------------------------------------------------------------------------------------|
| Malilib    | Required | Client      | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/malilib)                                                     |
| Mod Menu   | Optional | Client      | [CurseForge](https://www.curseforge.com/minecraft/mc-mods/modmenu) &#124; [Modrinth](https://modrinth.com/mod/modmenu) |

## Description

A library of versatile mod dependencies.

## Feature

### Compat API
- Multi-version compatibility, yes, we have used a number of means to make it compatible with all the latest releases of Minecraft (1.14+) running on the Fabric Loader. How is this done? Magic :(
- We have written compatibility APIs for balancing the differences between versions. For developers maintaining multiple MC versions of mods at the same time, it is possible to ignore Mojang's changes to MC to a certain extent. You don't have to care about what Mojang has done, using the API you will easily be able to use the same code between MC versions.

### Dependency check

We have added a complete dependency checking system to verify dependency availability by means of expressions and even custom predicates, who can be applied in a variety of scenarios, e.g.
- MagicLibMixinPlugin provides additional dependency checking for the module, which makes up for some shortcomings of Fabric Loader, for example by setting different dependencies for the client side and the server side.
- Dependency checking can also be applied to Mixin, and Mixin will only be applied if the conditions are met.
- Dependency checking can also be applied to configuration management so that the configuration item is only displayed if the conditions are met.
- Further features may be included in future developments.

### I18n
- We have reimplemented I18n independent of MC and can set the list of alternate languages for MagicLib I18n. Mojang uses some tricks with I18n text to make some features of String.format unavailable, which is usually annoying.

### Malilib extensions
- We have written a very useful configuration management module for Malilib where we will use basic data types with Java annotations to generate configuration lists wherever possible. Also, for configuration files we have included a configuration versioning system which will help you to quickly write your own custom migration solution in the event of a major change to the configuration file structure.
- We have ported some features from a higher version of Malilib to make it easier to use the same features when using Malilib adapted to a lower version of Minecraft

## Development

### Support

Current main development for Minecraft version: 1.19.2

And use `preprocess` to be compatible with all versions.

**Note: I only accept the following versions of issues, which are the last updates of each MC major version. Please note that this information is time-sensitive and any version of the issue not listed here will be closed**

- Minecraft 1.14.4
- Minecraft 1.15.2
- Minecraft 1.16.5
- Minecraft 1.17.1
- Minecraft 1.18.2
- Minecraft 1.19.2

### Mappings

We are using the **Mojang official** mappings to de-obfuscate Minecraft and insert patches.

### Document

The English doc and the Chinese doc are aligned line by line.

## License

This project is available under the LGPLv3 license. Feel free to learn from it and incorporate it in your own projects.