/*
 * Copyright (c) Copyright 2020 - 2022 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU Lesser General Public
 * License, version 3. If a copy of the LGPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package top.hendrixshen.magiclib.dependency.annotation;

import top.hendrixshen.magiclib.api.rule.RuleOption;

import java.util.function.Predicate;

public interface RuleDependencyPredicate extends Predicate<RuleOption> {
}
