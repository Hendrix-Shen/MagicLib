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

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.dependency.version.SemanticVersion;
import top.hendrixshen.magiclib.api.dependency.version.Version;
import top.hendrixshen.magiclib.api.dependency.version.VersionParsingException;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Arrays;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/12775fdfe9eb7a0b1e260acf1e27aeb80c930543/src/main/java/net/fabricmc/loader/api/SemanticVersion.java">FabricLoader</a>
 *
 * <p>
 * Parser for a superset of the semantic version format described at <a href="https://semver.org">semver.org</a>.
 *
 * <p>
 * This superset allows additionally
 *
 * <ul>
 *     <li>Arbitrary number of {@code <version core>} components, but at least 1
 *     <li>{@code x}, {@code X} or {@code *} for the last {@code <version core>} component with {@code storeX} if not the first
 *     <li>Arbitrary {@code <build>} contents
 * </ul>
 */
public class SemanticVersionImpl implements SemanticVersion {
    private static final Pattern DOT_SEPARATED_ID = Pattern.compile("|[-0-9A-Za-z]+(\\.[-0-9A-Za-z]+)*");
    private static final Pattern UNSIGNED_INTEGER = Pattern.compile("0|[1-9][0-9]*");
    private final int[] components;
    private final String prerelease;
    private final String build;
    private String friendlyName;

    public SemanticVersionImpl(@NotNull String version, boolean storeX) throws VersionParsingException {
        int buildDelimPos = version.indexOf('+');

        if (buildDelimPos >= 0) {
            this.build = version.substring(buildDelimPos + 1);
            version = version.substring(0, buildDelimPos);
        } else {
            this.build = null;
        }

        int dashDelimPos = version.indexOf('-');

        if (dashDelimPos >= 0) {
            this.prerelease = version.substring(dashDelimPos + 1);
            version = version.substring(0, dashDelimPos);
        } else {
            this.prerelease = null;
        }

        if (this.prerelease != null && !SemanticVersionImpl.DOT_SEPARATED_ID.matcher(this.prerelease).matches()) {
            throw new VersionParsingException("Invalid prerelease string '" + this.prerelease + "'!");
        }

        if (version.endsWith(".")) {
            throw new VersionParsingException("Negative version number component found!");
        } else if (version.startsWith(".")) {
            throw new VersionParsingException("Missing version component!");
        }

        String[] componentStrings = version.split("\\.");

        if (componentStrings.length < 1) {
            throw new VersionParsingException("Did not provide version numbers!");
        }

        int[] components = new int[componentStrings.length];
        int firstWildcardIdx = -1;

        for (int i = 0; i < componentStrings.length; i++) {
            String compStr = componentStrings[i];

            if (storeX) {
                if (compStr.equals("x") || compStr.equals("X") || compStr.equals("*")) {
                    if (this.prerelease != null) {
                        throw new VersionParsingException("Pre-release versions are not allowed to use X-ranges!");
                    }

                    components[i] = SemanticVersion.COMPONENT_WILDCARD;
                    if (firstWildcardIdx < 0) firstWildcardIdx = i;
                    continue;
                } else if (i > 0 && components[i - 1] == SemanticVersion.COMPONENT_WILDCARD) {
                    throw new VersionParsingException("Interjacent wildcard (1.x.2) are disallowed!");
                }
            }

            if (compStr.trim().isEmpty()) {
                throw new VersionParsingException("Missing version number component!");
            }

            try {
                components[i] = Integer.parseInt(compStr);

                if (components[i] < 0) {
                    throw new VersionParsingException("Negative version number component '" + compStr + "'!");
                }
            } catch (NumberFormatException e) {
                throw new VersionParsingException("Could not parse version number component '" + compStr + "'!", e);
            }
        }

        if (storeX && components.length == 1 && components[0] == SemanticVersion.COMPONENT_WILDCARD) {
            throw new VersionParsingException("Versions of form 'x' or 'X' not allowed!");
        }

        // strip extra wildcards (1.x.x -> 1.x)
        if (firstWildcardIdx > 0 && components.length > firstWildcardIdx + 1) {
            components = Arrays.copyOf(components, firstWildcardIdx + 1);
        }

        this.components = components;
        this.buildFriendlyName();
    }

    public SemanticVersionImpl(int @NotNull [] components, String prerelease, String build) {
        if (components.length == 0 || components[0] == SemanticVersion.COMPONENT_WILDCARD) {
            throw new IllegalArgumentException("Invalid components: " + Arrays.toString(components));
        }

        this.components = components;
        this.prerelease = prerelease;
        this.build = build;
        this.buildFriendlyName();
    }

    private void buildFriendlyName() {
        StringBuilder fnBuilder = new StringBuilder();
        boolean first = true;

        for (int i : this.components) {
            if (first) {
                first = false;
            } else {
                fnBuilder.append('.');
            }

            if (i == SemanticVersion.COMPONENT_WILDCARD) {
                fnBuilder.append('x');
            } else {
                fnBuilder.append(i);
            }
        }

        if (this.prerelease != null) {
            fnBuilder.append('-').append(this.prerelease);
        }

        if (this.build != null) {
            fnBuilder.append('+').append(this.build);
        }

        this.friendlyName = fnBuilder.toString();
    }

    @Override
    public int getVersionComponentCount() {
        return this.components.length;
    }

    @Override
    public int getVersionComponent(int pos) {
        if (pos < 0) {
            throw new RuntimeException("Tried to access negative version number component!");
        } else if (pos >= this.components.length) {
            // Repeat "x" if x-range, otherwise repeat "0".
            return this.components[this.components.length - 1] == SemanticVersion.COMPONENT_WILDCARD ?
                    SemanticVersion.COMPONENT_WILDCARD : 0;
        } else {
            return this.components[pos];
        }
    }

    public int[] getVersionComponents() {
        return this.components.clone();
    }

    @Override
    public ValueContainer<String> getPrereleaseKey() {
        return ValueContainer.ofNullable(this.prerelease);
    }

    @Override
    public ValueContainer<String> getBuildKey() {
        return ValueContainer.ofNullable(this.build);
    }

    @Override
    public String getFriendlyString() {
        return this.friendlyName;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof SemanticVersionImpl)) {
            return false;
        } else {
            SemanticVersionImpl other = (SemanticVersionImpl) o;

            if (!equalsComponentsExactly(other)) {
                return false;
            }

            return Objects.equals(this.prerelease, other.prerelease) && Objects.equals(this.build, other.build);
        }
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(this.components) * 73 +
                (this.prerelease != null ? this.prerelease.hashCode() * 11 : 0) +
                (this.build != null ? this.build.hashCode() : 0);
    }

    @Override
    public String toString() {
        return this.getFriendlyString();
    }

    @Override
    public boolean hasWildcard() {
        for (int i : this.components) {
            if (i < 0) {
                return true;
            }
        }

        return false;
    }

    public boolean equalsComponentsExactly(@NotNull SemanticVersionImpl other) {
        for (int i = 0; i < Math.max(this.getVersionComponentCount(), other.getVersionComponentCount()); i++) {
            if (this.getVersionComponent(i) != other.getVersionComponent(i)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public int compareTo(@NotNull Version other) {
        if (!(other instanceof SemanticVersion)) {
            return getFriendlyString().compareTo(other.getFriendlyString());
        }

        SemanticVersion o = (SemanticVersion) other;

        for (int i = 0; i < Math.max(getVersionComponentCount(), o.getVersionComponentCount()); i++) {
            int first = getVersionComponent(i);
            int second = o.getVersionComponent(i);

            if (first == SemanticVersion.COMPONENT_WILDCARD || second == SemanticVersion.COMPONENT_WILDCARD) {
                continue;
            }

            int compare = Integer.compare(first, second);
            if (compare != 0) return compare;
        }

        ValueContainer<String> prereleaseA = getPrereleaseKey();
        ValueContainer<String> prereleaseB = o.getPrereleaseKey();

        if (prereleaseA.isPresent() || prereleaseB.isPresent()) {
            if (prereleaseA.isPresent() && prereleaseB.isPresent()) {
                StringTokenizer prereleaseATokenizer = new StringTokenizer(prereleaseA.get(), ".");
                StringTokenizer prereleaseBTokenizer = new StringTokenizer(prereleaseB.get(), ".");

                while (prereleaseATokenizer.hasMoreElements()) {
                    if (prereleaseBTokenizer.hasMoreElements()) {
                        String partA = prereleaseATokenizer.nextToken();
                        String partB = prereleaseBTokenizer.nextToken();

                        if (SemanticVersionImpl.UNSIGNED_INTEGER.matcher(partA).matches()) {
                            if (SemanticVersionImpl.UNSIGNED_INTEGER.matcher(partB).matches()) {
                                int compare = Integer.compare(partA.length(), partB.length());
                                if (compare != 0) return compare;
                            } else {
                                return -1;
                            }
                        } else {
                            if (SemanticVersionImpl.UNSIGNED_INTEGER.matcher(partB).matches()) {
                                return 1;
                            }
                        }

                        int compare = partA.compareTo(partB);
                        if (compare != 0) return compare;
                    } else {
                        return 1;
                    }
                }

                return prereleaseBTokenizer.hasMoreElements() ? -1 : 0;
            } else if (prereleaseA.isPresent()) {
                return o.hasWildcard() ? 0 : -1;
            } else { // prereleaseB.isPresent()
                return hasWildcard() ? 0 : 1;
            }
        } else {
            return 0;
        }
    }
}