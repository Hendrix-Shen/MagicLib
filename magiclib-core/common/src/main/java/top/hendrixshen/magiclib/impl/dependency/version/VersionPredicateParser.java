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
import top.hendrixshen.magiclib.api.dependency.version.*;

import java.util.*;

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/12775fdfe9eb7a0b1e260acf1e27aeb80c930543/src/main/java/net/fabricmc/loader/impl/util/version/VersionPredicateParser.java">FabricLoader</a>
 */
public class VersionPredicateParser {
    private static final VersionComparisonOperator[] OPERATORS = VersionComparisonOperator.values();

    public static VersionPredicate parse(String predicate) throws VersionParsingException {
        List<SingleVersionPredicate> predicateList = Lists.newArrayList();

        for (String s : predicate.split(" ")) {
            s = s.trim();

            if (s.isEmpty() || s.equals("*")) {
                continue;
            }

            VersionComparisonOperator operator = VersionComparisonOperator.EQUAL;

            for (VersionComparisonOperator op : VersionPredicateParser.OPERATORS) {
                if (s.startsWith(op.getSerialized())) {
                    operator = op;
                    s = s.substring(op.getSerialized().length());
                    break;
                }
            }

            Version version = VersionParser.parse(s, true);

            if (version instanceof SemanticVersion) {
                SemanticVersion semVer = (SemanticVersion) version;

                if (semVer.hasWildcard()) { // .x version -> replace with conventional version by replacing the operator
                    if (operator != VersionComparisonOperator.EQUAL) {
                        throw new VersionParsingException("Invalid predicate: " + predicate + ", version ranges with wildcards (.X) require using the equality operator or no operator at all!");
                    }

                    assert !semVer.getPrereleaseKey().isPresent();

                    int compCount = semVer.getVersionComponentCount();
                    assert compCount == 2 || compCount == 3;

                    operator = compCount == 2 ? VersionComparisonOperator.SAME_TO_NEXT_MAJOR : VersionComparisonOperator.SAME_TO_NEXT_MINOR;

                    int[] newComponents = new int[semVer.getVersionComponentCount() - 1];

                    for (int i = 0; i < semVer.getVersionComponentCount() - 1; i++) {
                        newComponents[i] = semVer.getVersionComponent(i);
                    }

                    version = new SemanticVersionImpl(newComponents, "", semVer.getBuildKey().orElse(null));
                }
            } else if (!operator.isMinInclusive() && !operator.isMaxInclusive()) { // non-semver without inclusive bound
                throw new VersionParsingException("Invalid predicate: " + predicate + ", version ranges need to be semantic version compatible to use operators that exclude the bound!");
            } else { // non-semver with inclusive bound
                operator = VersionComparisonOperator.EQUAL;
            }

            predicateList.add(new SingleVersionPredicate(operator, version));
        }

        if (predicateList.isEmpty()) {
            return AnyVersionPredicate.INSTANCE;
        } else if (predicateList.size() == 1) {
            return predicateList.get(0);
        } else {
            return new MultiVersionPredicate(predicateList);
        }
    }

    public static Set<VersionPredicate> parse(Collection<String> predicates) throws VersionParsingException {
        Set<VersionPredicate> ret = new HashSet<>(predicates.size());

        for (String version : predicates) {
            ret.add(parse(version));
        }

        return ret;
    }

    public static VersionPredicate getAny() {
        return AnyVersionPredicate.INSTANCE;
    }

    static class AnyVersionPredicate implements VersionPredicate {
        static final VersionPredicate INSTANCE = new AnyVersionPredicate();

        private AnyVersionPredicate() {
        }

        @Override
        public boolean test(Version t) {
            return true;
        }

        @Override
        public List<? extends PredicateTerm> getTerms() {
            return Collections.emptyList();
        }

        @Override
        public VersionInterval getInterval() {
            return VersionIntervalImpl.INFINITE;
        }

        @Override
        public String toString() {
            return "*";
        }
    }

    static class SingleVersionPredicate implements VersionPredicate, VersionPredicate.PredicateTerm {
        private final VersionComparisonOperator operator;
        private final Version refVersion;

        SingleVersionPredicate(VersionComparisonOperator operator, Version refVersion) {
            this.operator = operator;
            this.refVersion = refVersion;
        }

        @Override
        public boolean test(Version version) {
            Objects.requireNonNull(version, "null version");

            return this.operator.test(version, this.refVersion);
        }

        @Override
        public List<PredicateTerm> getTerms() {
            return Collections.singletonList(this);
        }

        @Override
        public VersionInterval getInterval() {
            if (this.refVersion instanceof SemanticVersion) {
                SemanticVersion version = (SemanticVersion) this.refVersion;

                return new VersionIntervalImpl(this.operator.minVersion(version), this.operator.isMinInclusive(),
                        this.operator.maxVersion(version), this.operator.isMaxInclusive());
            } else {
                return new VersionIntervalImpl(this.refVersion, true, this.refVersion, true);
            }
        }

        @Override
        public VersionComparisonOperator getOperator() {
            return this.operator;
        }

        @Override
        public Version getReferenceVersion() {
            return this.refVersion;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof SingleVersionPredicate) {
                SingleVersionPredicate o = (SingleVersionPredicate) obj;

                return this.operator == o.operator && this.refVersion.equals(o.refVersion);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return this.operator.ordinal() * 31 + this.refVersion.hashCode();
        }

        @Override
        public String toString() {
            return this.operator.getSerialized().concat(this.refVersion.toString());
        }
    }

    static class MultiVersionPredicate implements VersionPredicate {
        private final List<SingleVersionPredicate> predicates;

        MultiVersionPredicate(List<SingleVersionPredicate> predicates) {
            this.predicates = predicates;
        }

        @Override
        public boolean test(Version version) {
            Objects.requireNonNull(version, "null version");

            for (SingleVersionPredicate predicate : this.predicates) {
                if (!predicate.test(version)) return false;
            }

            return true;
        }

        @Override
        public List<? extends PredicateTerm> getTerms() {
            return this.predicates;
        }

        @Override
        public VersionInterval getInterval() {
            if (this.predicates.isEmpty()) return AnyVersionPredicate.INSTANCE.getInterval();

            VersionInterval ret = this.predicates.get(0).getInterval();

            for (int i = 1; i < this.predicates.size(); i++) {
                ret = VersionIntervalImpl.and(ret, this.predicates.get(i).getInterval());
            }

            return ret;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof MultiVersionPredicate) {
                MultiVersionPredicate o = (MultiVersionPredicate) obj;

                return this.predicates.equals(o.predicates);
            } else {
                return false;
            }
        }

        @Override
        public int hashCode() {
            return this.predicates.hashCode();
        }

        @Override
        public String toString() {
            StringBuilder ret = new StringBuilder();

            for (SingleVersionPredicate predicate : this.predicates) {
                if (ret.length() > 0) ret.append(' ');
                ret.append(predicate.toString());
            }

            return ret.toString();
        }
    }
}
