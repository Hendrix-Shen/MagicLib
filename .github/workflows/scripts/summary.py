"""
Modified from github.com/Fallen-Breath/fabric-mod-template
Originally authored by Fallen_Breath

A script to scan through all valid mod jars in build-artifacts.zip/$module/$version/build/libs,
and generate an artifact summary table for that to GitHub action step summary
"""
__author__ = 'Hendrix_Shen'

import functools
import glob
import hashlib
import os
from typing import List, Dict

import common
from common import FileData, Module

__EMPTY_FILE_INFO = FileData('*not found*', 0, '*N/A*')


def get_sha256_hash(file_path: str) -> str:
    sha256_hash = hashlib.sha256()

    with open(file_path, 'rb') as f:
        for buf in iter(functools.partial(f.read, 4096), b''):
            sha256_hash.update(buf)

    return sha256_hash.hexdigest()


def get_file_info(file_paths: List[str], subproject: str, warnings: List[str]) -> FileData:
    if len(file_paths) == 0:
        return __EMPTY_FILE_INFO

    if len(file_paths) > 1:
        warnings.append(
            'Found too many build files in subproject {}: {}'.format(subproject, ', '.join(file_paths)))

    return FileData(os.path.basename(file_paths[0]), os.path.getsize(file_paths[0]), get_sha256_hash(file_paths[0]))


def main():
    subproject_dict: Dict[str, List[str]] = common.get_projects_by_platform()
    settings: dict = common.get_settings()

    with open(os.environ['GITHUB_STEP_SUMMARY'], 'w') as f:
        f.write('## Build Artifacts Summary\n')
        warnings: List[str] = []
        wrappers: List[Module] = []
        cores: List[str] = []
        modules: Dict[str, List[Module]] = {}

        for platform in subproject_dict:
            for mc_ver in subproject_dict[platform]:
                module: common.Module = common.Module(mc_ver, platform)
                wrappers.append(module)
                cores.append(module.platform())

                for module_name in settings['projects']:
                    if module.get_str() not in settings['projects'][module_name]['versions']:
                        continue

                    if module_name not in modules:
                        modules[module_name] = []

                    modules[module_name].append(module)

        wrappers = sorted(list(set(wrappers)), key=lambda m: (m.mc_ver(), m.platform()))
        f.write('### MagicLib Mod\n')
        f.write('| Minecraft | Platform | File | Size | SHA-256 |\n')
        f.write('|:---:|:---:|--- | --- | --- |\n')

        for wrapper in wrappers:
            file_paths = glob.glob(
                'build-artifacts/magiclib-wrapper/{}/{}/build/libs/*.jar'.format(wrapper.platform(), wrapper.mc_ver()))
            file_paths = list(filter(
                lambda fp: not fp.endswith('-sources.jar') and not fp.endswith('-javadoc.jar') and not fp.endswith(
                    '-empty.jar'), file_paths))
            file_info: FileData = get_file_info(file_paths, 'magiclib-wrapper', warnings)
            f.write('| {} | {} | {} | {} | {} |\n'.format(wrapper.mc_ver(), wrapper.pretty_platform(),
                                                          file_info.file_name, file_info.get_file_size(),
                                                          file_info.sha256))

        f.write('\n')
        cores = sorted(list(set(cores)))
        f.write('### Module {}\n'.format(common.read_prop('magiclib-core/gradle.properties', 'mod.name')))
        f.write('| Platform | File | Size | SHA-256 |\n')
        f.write('|:---:| --- | --- | --- |\n')

        for platform in cores:
            file_paths = glob.glob('build-artifacts/magiclib-core/{}/build/libs/*.jar'.format(platform))
            file_paths = list(filter(
                lambda fp: not fp.endswith('-sources.jar') and not fp.endswith('-slim.jar') and not fp.endswith(
                    '-javadoc.jar'), file_paths))
            file_info = get_file_info(file_paths, platform, warnings)
            platform = common.pretty_platform(platform)
            f.write('| {} | {} | {} | {} |\n'.format(platform, file_info.file_name, file_info.get_file_size(),
                                                     file_info.sha256))

        modules = {key: modules[key] for key in sorted(modules.keys())}

        for subproject in modules:
            modules[subproject] = sorted(list(set(modules[subproject])), key=lambda m: (m.mc_ver(), m.platform()))
            f.write(
                '### Module {}\n'.format(common.read_prop('{}/gradle.properties'.format(subproject), 'mod.name')))
            f.write('| Minecraft | Platform | File | Size | SHA-256 |\n')
            f.write('|:---:|:---:| --- | --- | --- |\n')

            for module in modules[subproject]:
                platform = module.pretty_platform()
                game_versions = common.read_prop(
                    '{}/versions/{}/gradle.properties'.format(subproject, module.get_str()),
                    'publish.game_version')
                game_versions = game_versions.strip().replace('\n', ', ')
                file_paths = glob.glob(
                    'build-artifacts/{}/versions/{}/build/libs/*.jar'.format(subproject, module.get_str()))
                file_paths = list(filter(
                    lambda fp: not fp.endswith('-sources.jar') and not fp.endswith('-shadow.jar') and not fp.endswith(
                        '-javadoc.jar'), file_paths))
                file_info = get_file_info(file_paths, module.get_str(), warnings)
                f.write('| {} | {} | {} | {} | {} |\n'.format(game_versions, platform, file_info.file_name,
                                                              file_info.get_file_size(), file_info.sha256))
            f.write('\n')

        if len(warnings) > 0:
            f.write('### Warnings\n')

            for warning in warnings:
                f.write('- {}\n'.format(warning))


if __name__ == '__main__':
    main()
