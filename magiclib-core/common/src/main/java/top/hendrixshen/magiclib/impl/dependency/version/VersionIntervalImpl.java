/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.hendrixshen.magiclib.impl.dependency.version;

import com.google.common.collect.Lists;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.dependency.version.SemanticVersion;
import top.hendrixshen.magiclib.api.dependency.version.Version;
import top.hendrixshen.magiclib.api.dependency.version.VersionInterval;

import java.util.*;

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/12775fdfe9eb7a0b1e260acf1e27aeb80c930543/src/main/java/net/fabricmc/loader/impl/util/version/VersionIntervalImpl.java">FabricLoader</a>
 */
public class VersionIntervalImpl implements VersionInterval {
    private final Version min;
    private final boolean minInclusive;
    private final Version max;
    private final boolean maxInclusive;

    public VersionIntervalImpl(Version min, boolean minInclusive,
                               Version max, boolean maxInclusive) {
        this.min = min;
        this.minInclusive = min != null ? minInclusive : false;
        this.max = max;
        this.maxInclusive = max != null ? maxInclusive : false;

        assert min != null || !minInclusive;
        assert max != null || !maxInclusive;
        assert min == null || min instanceof SemanticVersion || minInclusive;
        assert max == null || max instanceof SemanticVersion || maxInclusive;
        assert min == null || max == null || min instanceof SemanticVersion &&
                max instanceof SemanticVersion || min.equals(max);
    }

    @Override
    public boolean isSemantic() {
        return (this.min == null || this.min instanceof SemanticVersion)
                && (this.max == null || this.max instanceof SemanticVersion);
    }

    @Override
    public Version getMin() {
        return this.min;
    }

    @Override
    public boolean isMinInclusive() {
        return this.minInclusive;
    }

    @Override
    public Version getMax() {
        return this.max;
    }

    @Override
    public boolean isMaxInclusive() {
        return this.maxInclusive;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VersionInterval) {
            VersionInterval o = (VersionInterval) obj;

            return Objects.equals(this.min, o.getMin()) && this.minInclusive == o.isMinInclusive()
                    && Objects.equals(this.max, o.getMax()) && this.maxInclusive == o.isMaxInclusive();
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return (Objects.hashCode(this.min) + (this.minInclusive ? 1 : 0)) * 31
                + (Objects.hashCode(this.max) + (this.maxInclusive ? 1 : 0)) * 31;
    }

    @Override
    public String toString() {
        if (this.min == null) {
            if (this.max == null) {
                return "(-∞,∞)";
            } else {
                return String.format("(-∞,%s%c", this.max, this.maxInclusive ? ']' : ')');
            }
        } else if (this.max == null) {
            return String.format("%c%s,∞)", this.minInclusive ? '[' : '(', this.min);
        } else {
            return String.format("%c%s,%s%c", this.minInclusive ? '[' : '(', this.min,
                    this.max, this.maxInclusive ? ']' : ')');
        }
    }

    public static VersionInterval and(VersionInterval a, VersionInterval b) {
        if (a == null || b == null) {
            return null;
        }

        if (!a.isSemantic() || !b.isSemantic()) {
            return VersionIntervalImpl.andPlain(a, b);
        } else {
            return VersionIntervalImpl.andSemantic(a, b);
        }
    }

    private static @Nullable VersionInterval andPlain(@NotNull VersionInterval a, @NotNull VersionInterval b) {
        Version aMin = a.getMin();
        Version aMax = a.getMax();
        Version bMin = b.getMin();
        Version bMax = b.getMax();

        if (aMin != null) { // -> min must be aMin or invalid
            if (bMin != null && !aMin.equals(bMin) || bMax != null && !aMin.equals(bMax)) {
                return null;
            }

            if (aMax != null || bMax == null) {
                assert Objects.equals(aMax, bMax) || bMax == null;
                return a;
            } else {
                return new VersionIntervalImpl(aMin, true, bMax, b.isMaxInclusive());
            }
        } else if (aMax != null) { // -> min must be bMin, max must be aMax or invalid
            if (bMin != null && !aMax.equals(bMin) || bMax != null && !aMax.equals(bMax)) {
                return null;
            }

            if (bMin == null) {
                return a;
            } else if (bMax != null) {
                return b;
            } else {
                return new VersionIntervalImpl(bMin, true, aMax, true);
            }
        } else {
            return b;
        }
    }

    private static @Nullable VersionInterval andSemantic(VersionInterval a, VersionInterval b) {
        int minCmp = VersionIntervalImpl.compareMin(a, b);
        int maxCmp = VersionIntervalImpl.compareMax(a, b);

        if (minCmp == 0) { // aMin == bMin
            if (maxCmp == 0) { // aMax == bMax -> a == b -> a/b
                return a;
            } else { // aMax != bMax -> a/b..min(a,b)
                return maxCmp < 0 ? a : b;
            }
        } else if (maxCmp == 0) { // aMax == bMax, aMin != bMin -> max(a,b)..a/b
            return minCmp < 0 ? b : a;
        } else if (minCmp < 0) { // aMin < bMin, aMax != bMax -> b..min(a,b)
            if (maxCmp > 0) return b; // a > b -> b

            SemanticVersion aMax = (SemanticVersion) a.getMax();
            SemanticVersion bMin = (SemanticVersion) b.getMin();
            int cmp = bMin.compareTo(aMax);

            if (cmp < 0 || cmp == 0 && b.isMinInclusive() && a.isMaxInclusive()) {
                return new VersionIntervalImpl(bMin, b.isMinInclusive(), aMax, a.isMaxInclusive());
            } else {
                return null;
            }
        } else { // aMin > bMin, aMax != bMax -> a..min(a,b)
            if (maxCmp < 0) {
                // a < b -> a
                return a;
            }

            SemanticVersion aMin = (SemanticVersion) a.getMin();
            SemanticVersion bMax = (SemanticVersion) b.getMax();
            int cmp = aMin.compareTo(bMax);

            if (cmp < 0 || cmp == 0 && a.isMinInclusive() && b.isMaxInclusive()) {
                return new VersionIntervalImpl(aMin, a.isMinInclusive(), bMax, b.isMaxInclusive());
            } else {
                return null;
            }
        }
    }

    public static List<VersionInterval> and(@NotNull Collection<VersionInterval> a, Collection<VersionInterval> b) {
        if (a.isEmpty() || b.isEmpty()) {
            return Collections.emptyList();
        }

        if (a.size() == 1 && b.size() == 1) {
            VersionInterval merged = VersionIntervalImpl.and(a.iterator().next(), b.iterator().next());
            return merged != null ? Collections.singletonList(merged) : Collections.emptyList();
        }

        // (a0 || a1 || a2) && (b0 || b1 || b2) == a0 && b0 && b1 && b2 || a1 && b0 && b1 && b2 || a2 && b0 && b1 && b2

        List<VersionInterval> allMerged = Lists.newArrayList();

        for (VersionInterval intervalA : a) {
            for (VersionInterval intervalB : b) {
                VersionInterval merged = VersionIntervalImpl.and(intervalA, intervalB);

                if (merged != null) {
                    allMerged.add(merged);
                }
            }
        }

        if (allMerged.isEmpty()) {
            return Collections.emptyList();
        }

        if (allMerged.size() == 1) {
            return allMerged;
        }

        List<VersionInterval> ret = new ArrayList<>(allMerged.size());

        for (VersionInterval v : allMerged) {
            VersionIntervalImpl.merge(v, ret);
        }

        return ret;
    }

    public static List<VersionInterval> or(@NotNull Collection<VersionInterval> a, VersionInterval b) {
        if (a.isEmpty()) {
            if (b == null) {
                return Collections.emptyList();
            } else {
                return Collections.singletonList(b);
            }
        }

        List<VersionInterval> ret = new ArrayList<>(a.size() + 1);

        for (VersionInterval v : a) {
            VersionIntervalImpl.merge(v, ret);
        }

        VersionIntervalImpl.merge(b, ret);

        return ret;
    }

    private static void merge(VersionInterval a, List<VersionInterval> out) {
        if (a == null) {
            return;
        }

        if (out.isEmpty()) {
            out.add(a);
            return;
        }

        if (out.size() == 1) {
            VersionInterval e = out.get(0);

            if (e.getMin() == null && e.getMax() == null) {
                return;
            }
        }

        if (!a.isSemantic()) {
            VersionIntervalImpl.mergePlain(a, out);
        } else {
            VersionIntervalImpl.mergeSemantic(a, out);
        }
    }

    private static void mergePlain(@NotNull VersionInterval a, @NotNull List<VersionInterval> out) {
        Version aMin = a.getMin();
        Version aMax = a.getMax();
        Version v = aMin != null ? aMin : aMax;
        assert v != null;

        for (int i = 0; i < out.size(); i++) {
            VersionInterval c = out.get(i);

            if (v.equals(c.getMin())) {
                if (aMin == null) {
                    assert aMax.equals(c.getMin());
                    out.clear();
                    out.add(VersionInterval.INFINITE);
                } else if (aMax == null && c.getMax() != null) {
                    out.set(i, a);
                }

                return;
            } else if (v.equals(c.getMax())) {
                assert c.getMin() == null;

                if (aMax == null) {
                    assert aMin.equals(c.getMax());
                    out.clear();
                    out.add(VersionInterval.INFINITE);
                }

                return;
            }
        }

        out.add(a);
    }

    private static void mergeSemantic(@NotNull VersionInterval a, List<VersionInterval> out) {
        SemanticVersion aMin = (SemanticVersion) a.getMin();
        SemanticVersion aMax = (SemanticVersion) a.getMax();

        if (aMin == null && aMax == null) {
            out.clear();
            out.add(VersionInterval.INFINITE);
            return;
        }

        for (int i = 0; i < out.size(); i++) {
            VersionInterval c = out.get(i);

            if (!c.isSemantic()) {
                continue;
            }

            SemanticVersion cMin = (SemanticVersion) c.getMin();
            SemanticVersion cMax = (SemanticVersion) c.getMax();
            int cmp;

            if (aMin == null) { // ..a..]
                if (cMax == null) { // ..a..] [..c..
                    cmp = aMax.compareTo(cMin);

                    if (cmp < 0 || cmp == 0 && !a.isMaxInclusive() && !c.isMinInclusive()) { // ..a..]..[..c.. or ..a..)(..c..
                        out.add(i, a);
                    } else { // ..a..|..c.. or ..a.[..].c..
                        out.clear();
                        out.add(VersionInterval.INFINITE);
                    }

                    return;
                } else { // ..a..] [..c..]
                    cmp = VersionIntervalImpl.compareMax(a, c);

                    if (cmp >= 0) { // a encompasses c
                        out.remove(i);
                        i--;
                    } else if (cMin == null) { // c encompasses a
                        return;
                    } else { // aMax < cMax
                        cmp = aMax.compareTo((Version) cMin);

                        if (cmp < 0 || cmp == 0 && !a.isMaxInclusive() && !c.isMinInclusive()) { // ..a..]..[..c..] or ..a..)(..c..]
                            out.add(i, a);
                        } else { // c extends a to the right
                            out.set(i, new VersionIntervalImpl(null, false, cMax, c.isMaxInclusive()));
                        }

                        return;
                    }
                }
            } else if (cMax == null) { // [..c..
                cmp = compareMin(a, c);

                if (cmp >= 0) { // c encompasses a
                    // no-op
                } else if (aMax == null) { // a encompasses c
                    while (out.size() > i) out.remove(i);
                    out.add(a);
                } else { // aMin < cMin
                    cmp = aMax.compareTo((Version) cMin);

                    if (cmp < 0 || cmp == 0 && !a.isMaxInclusive() && !c.isMinInclusive()) { // [..a..]..[..c.. or [..a..)(..c..
                        out.add(i, a);
                    } else { // a extends c to the left
                        out.set(i, new VersionIntervalImpl(aMin, a.isMinInclusive(), null, false));
                    }
                }

                return;
            } else if ((cmp = aMin.compareTo(cMax)) < 0 || cmp == 0 && (a.isMinInclusive() || c.isMaxInclusive())) {
                int cmp2;

                if (aMax == null || cMin == null || (cmp2 = aMax.compareTo(cMin)) > 0 ||
                        cmp2 == 0 && (a.isMaxInclusive() || c.isMinInclusive())) {
                    int cmpMin = compareMin(a, c);
                    int cmpMax = compareMax(a, c);

                    if (cmpMax <= 0) { // aMax <= cMax
                        if (cmpMin < 0) { // aMin < cMin
                            out.set(i, new VersionIntervalImpl(aMin, a.isMinInclusive(), cMax, c.isMaxInclusive()));
                        }

                        return;
                    } else if (cmpMin > 0) { // aMin > cMin, aMax > cMax
                        a = new VersionIntervalImpl(cMin, c.isMinInclusive(), aMax, a.isMaxInclusive());
                    }

                    out.remove(i);
                    i--;
                } else {
                    out.add(i, a);
                    return;
                }
            }
        }

        out.add(a);
    }

    private static int compareMin(@NotNull VersionInterval a, @NotNull VersionInterval b) {
        SemanticVersion aMin = (SemanticVersion) a.getMin();
        SemanticVersion bMin = (SemanticVersion) b.getMin();
        int cmp;

        if (aMin == null) { // a <= b
            if (bMin == null) { // a == b == -inf
                return 0;
            } else { // bMin != null -> a < b
                return -1;
            }
        } else if (bMin == null ||
                (cmp = aMin.compareTo(bMin)) > 0 || cmp == 0 && !a.isMinInclusive() && b.isMinInclusive()) { // a > b
            return 1;
        } else if (cmp < 0 || a.isMinInclusive() && !b.isMinInclusive()) { // a < b
            return -1;
        } else { // cmp == 0 && a.minInclusive() == b.minInclusive() -> a == b
            return 0;
        }
    }

    private static int compareMax(@NotNull VersionInterval a, @NotNull VersionInterval b) {
        SemanticVersion aMax = (SemanticVersion) a.getMax();
        SemanticVersion bMax = (SemanticVersion) b.getMax();
        int cmp;

        if (aMax == null) { // a >= b
            if (bMax == null) { // a == b == inf
                return 0;
            } else { // bMax != null -> a > b
                return 1;
            }
        } else if (bMax == null || (cmp = aMax.compareTo(bMax)) < 0 ||
                cmp == 0 && !a.isMaxInclusive() && b.isMaxInclusive()) { // a < b
            return -1;
        } else if (cmp > 0 || a.isMaxInclusive() && !b.isMaxInclusive()) { // a > b
            return 1;
        } else { // cmp == 0 && a.maxInclusive() == b.maxInclusive() -> a == b
            return 0;
        }
    }

    public static List<VersionInterval> not(VersionInterval interval) {
        if (interval == null) { // () = empty interval -> infinite
            return Collections.singletonList(VersionInterval.INFINITE);
        } else if (interval.getMin() == null) { // (-∞, = at least half-open towards min
            if (interval.getMax() == null) { // (-∞,∞) = infinite -> empty
                return Collections.emptyList();
            } else { // (-∞,x = left open towards min -> half open towards max
                return Collections.singletonList(new VersionIntervalImpl(interval.getMax(), !interval.isMaxInclusive(),
                        null, false));
            }
        } else if (interval.getMax() == null) { // x,∞) = half open towards max -> half open towards min
            return Collections.singletonList(new VersionIntervalImpl(null, false,
                    interval.getMin(), !interval.isMinInclusive()));
        } else if (interval.getMin().equals(interval.getMax()) && !interval.isMinInclusive() &&
                !interval.isMaxInclusive()) { // (x,x) = effectively empty interval -> infinite
            return Collections.singletonList(VersionInterval.INFINITE);
        } else { // closed interval -> 2 half open intervals on each side
            List<VersionInterval> ret = new ArrayList<>(2);
            ret.add(new VersionIntervalImpl(null, false, interval.getMin(), !interval.isMinInclusive()));
            ret.add(new VersionIntervalImpl(interval.getMax(), !interval.isMaxInclusive(), null, false));
            return ret;
        }
    }

    public static List<VersionInterval> not(@NotNull Collection<VersionInterval> intervals) {
        if (intervals.isEmpty()) {
            return Collections.singletonList(VersionInterval.INFINITE);
        }

        if (intervals.size() == 1) {
            return VersionIntervalImpl.not(intervals.iterator().next());
        }

        // !(i0 || i1 || i2) == !i0 && !i1 && !i2

        List<VersionInterval> ret = null;

        for (VersionInterval v : intervals) {
            List<VersionInterval> inverted = not(v);

            if (ret == null) {
                ret = inverted;
            } else {
                ret = VersionIntervalImpl.and(ret, inverted);
            }

            if (ret.isEmpty()) {
                break;
            }
        }

        return ret;
    }
}
