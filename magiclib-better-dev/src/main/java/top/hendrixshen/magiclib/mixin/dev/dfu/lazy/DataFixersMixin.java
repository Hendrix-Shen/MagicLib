/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Andrew Steinborn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import com.mojang.datafixers.DataFixerBuilder;
import net.minecraft.util.datafix.DataFixers;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.dfu.lazy.LazyDataFixerBuilder;

/**
 * Reference to <a href="https://github.com/astei/lazydfu/blob/385764a6fb4cf57b7a39e0ff367a704f74f12497/src/main/java/me/steinborn/lazydfu/mixin/SchemasMixin.java">LazyDFU</a>
 */
@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.LazyDFUPredicate.class
                )
        )
)
@Mixin(value = DataFixers.class, remap = false)
public class DataFixersMixin {
    @Redirect(
            method = "createFixerUpper",
            at = @At(
                    value = "NEW",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;"
            )
    )
    private static @NotNull DataFixerBuilder onCreateFixerUpper(int dataVersion) {
        return new LazyDataFixerBuilder(dataVersion);
    }
}
