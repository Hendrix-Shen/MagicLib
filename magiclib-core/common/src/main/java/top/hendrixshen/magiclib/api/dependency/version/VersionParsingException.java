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

/**
 * Reference to <a href="https://github.com/FabricMC/fabric-loader/blob/12775fdfe9eb7a0b1e260acf1e27aeb80c930543/src/main/java/net/fabricmc/loader/api/VersionParsingException.java">FabricLoader</a>
 */
public class VersionParsingException extends RuntimeException {
    public VersionParsingException() {
        super();
    }

    public VersionParsingException(Throwable t) {
        super(t);
    }

    public VersionParsingException(String s) {
        super(s);
    }

    public VersionParsingException(String s, Throwable t) {
        super(s, t);
    }
}
