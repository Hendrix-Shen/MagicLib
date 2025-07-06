"""
Modified from github.com/Fallen-Breath/fabric-mod-template
Originally authored by Fallen_Breath

A script to scan through the versions directory and collect all folder names as the subproject list,
then output a json as the github action include matrix
"""
__author__ = 'Hendrix_Shen'

import json
import os
from itertools import count
from typing import Dict, List

import common


def main():
    subproject_dict: Dict[str, List[str]] = common.get_projects_by_platform()
    counter = count()
    matrix: Dict[str, List[Dict[str, Union[str, int]]]] = {
        'include': [
            {
                'platform': platform,
                'mc_ver': mc_ver,
                'delay': delay_counter * 10
            }
            for platform, versions in subproject_dict.items()
            for mc_ver in versions
            for delay_counter in [next(counter)]
        ]
    }

    with open(os.environ['GITHUB_OUTPUT'], 'w') as f:
        f.write('matrix={}\n'.format(json.dumps(matrix)))

    print('matrix:')
    print(json.dumps(matrix, indent=2))


if __name__ == '__main__':
    main()
