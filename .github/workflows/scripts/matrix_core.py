__author__ = 'Hendrix_Shen'

import json
import os
from typing import Dict, List

import common


def main():
    target_subproject_env = os.environ.get('TARGET_SUBPROJECT', '')
    target_subprojects = list(filter(None, target_subproject_env.split(',') if target_subproject_env != '' else []))
    print('target_subprojects: {}'.format(target_subprojects))
    subproject_dict: Dict[str, List[str]] = common.get_subproject_dict()
    matrix: Dict[str, List[str]] = {'platform': []}

    if len(target_subprojects) == 0:
        for platform in subproject_dict:
            matrix['platform'].append(platform)
    else:
        for platform in subproject_dict:
            for mc_ver in subproject_dict[platform]:
                if mc_ver in target_subprojects:
                    matrix['platform'].append(platform)
        matrix['platform'] = sorted(list(set(matrix['platform'])))
    with open(os.environ['GITHUB_OUTPUT'], 'w') as f:
        f.write('matrix={}\n'.format(json.dumps(matrix)))

    print('matrix:')
    print(json.dumps(matrix, indent=2))


if __name__ == '__main__':
    main()
