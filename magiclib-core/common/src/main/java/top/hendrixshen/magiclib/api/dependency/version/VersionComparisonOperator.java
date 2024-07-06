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

package top.hendrixshen.magiclib.api.dependency.version;

import top.hendrixshen.magiclib.impl.dependency.version.SemanticVersionImpl;

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/12775fdfe9eb7a0b1e260acf1e27aeb80c930543/src/main/java/net/fabricmc/loader/api/metadata/version/VersionComparisonOperator.java">FabricLoader</a>
 */
public enum VersionComparisonOperator {
    // order is important to match the longest substring (e.g. try >= before >)
    GREATER_EQUAL(">=", true, false) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) >= 0;
        }

        @Override
        public SemanticVersion minVersion(SemanticVersion version) {
            return version;
        }
    },
    LESS_EQUAL("<=", false, true) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) <= 0;
        }

        @Override
        public SemanticVersion maxVersion(SemanticVersion version) {
            return version;
        }
    },
    GREATER(">", false, false) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) > 0;
        }

        @Override
        public SemanticVersion minVersion(SemanticVersion version) {
            return version;
        }
    },
    LESS("<", false, false) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) < 0;
        }

        @Override
        public SemanticVersion maxVersion(SemanticVersion version) {
            return version;
        }
    },
    EQUAL("=", true, true) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) == 0;
        }

        @Override
        public SemanticVersion minVersion(SemanticVersion version) {
            return version;
        }

        @Override
        public SemanticVersion maxVersion(SemanticVersion version) {
            return version;
        }
    },
    SAME_TO_NEXT_MINOR("~", true, false) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) >= 0
                    && a.getVersionComponent(0) == b.getVersionComponent(0)
                    && a.getVersionComponent(1) == b.getVersionComponent(1);
        }

        @Override
        public SemanticVersion minVersion(SemanticVersion version) {
            return version;
        }

        @Override
        public SemanticVersion maxVersion(SemanticVersion version) {
            return new SemanticVersionImpl(new int[]{version.getVersionComponent(0),
                    version.getVersionComponent(1) + 1}, "", null);
        }
    },
    SAME_TO_NEXT_MAJOR("^", true, false) {
        @Override
        public boolean test(SemanticVersion a, SemanticVersion b) {
            return a.compareTo(b) >= 0 && a.getVersionComponent(0) == b.getVersionComponent(0);
        }

        @Override
        public SemanticVersion minVersion(SemanticVersion version) {
            return version;
        }

        @Override
        public SemanticVersion maxVersion(SemanticVersion version) {
            return new SemanticVersionImpl(new int[]{version.getVersionComponent(0) + 1}, "", null);
        }
    };

    private final String serialized;
    private final boolean minInclusive;
    private final boolean maxInclusive;

    VersionComparisonOperator(String serialized, boolean minInclusive, boolean maxInclusive) {
        this.serialized = serialized;
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
    }

    public final String getSerialized() {
        return this.serialized;
    }

    public final boolean isMinInclusive() {
        return this.minInclusive;
    }

    public final boolean isMaxInclusive() {
        return this.maxInclusive;
    }

    public final boolean test(Version a, Version b) {
        if (a instanceof SemanticVersion && b instanceof SemanticVersion) {
            return test((SemanticVersion) a, (SemanticVersion) b);
        } else if (this.minInclusive || this.maxInclusive) {
            return a.getFriendlyString().equals(b.getFriendlyString());
        } else {
            return false;
        }
    }

    public abstract boolean test(SemanticVersion a, SemanticVersion b);

    public SemanticVersion minVersion(SemanticVersion version) {
        return null;
    }

    public SemanticVersion maxVersion(SemanticVersion version) {
        return null;
    }
}
