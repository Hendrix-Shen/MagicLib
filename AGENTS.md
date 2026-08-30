# AGENTS.md

## Technical Constraints

### Java Version Compatibility

The Java version is selected automatically based on the Minecraft version:

| Minecraft Version Range | Java Version |
|-------------------------|--------------|
| >= 26.1                 | Java 25      |
| >= 1.20.5               | Java 21      |
| >= 1.18                 | Java 17      |
| >= 1.17                 | Java 16      |
| < 1.17                  | Java 8       |

**Agent note**: When writing code, always consider the Java version limit imposed by the target Minecraft version. Do
not use language features newer than the allowed Java version.

### Preprocessor System (replaymod/preprocessor)

The project uses preprocessor to support multiple Minecraft versions:

- Main source code lives in each module's `mainProject`, the mainProject's name defined in
  `{submodule}/versions/mainProject` file. The mainProject src in `{submodule}/src`
- Preprocessed intermediate files are generated under `build/processed` in each subproject.
- These intermediate files participate in the build but are not persisted.
- To modify them, edit the main project using preprocessor directives.
- If the modification ratio exceeds 50% of a file, create an override file in the corresponding subproject's `/src`
  directory. This override will affect all downstream versions in the dependency chain.

#### Preprocessor Directive Syntax

When adding new preprocessor directives, use the **dotted version format** (e.g., `MC >= 26.2`). The legacy integer
format (e.g., `MC >= 260200`) may still appear in existing code; **do not update legacy directives** to the new format.
Only use the new format for new code.

Example:

```java

@Override
public void setScreen(Screen screen) {
    //#if MC >= 26.2
    //$$ this.get().gui.setScreen(screen);
    //#else
    this.get().setScreen(screen);
    //#endif
}
```

#### Dotted Comparison Semantics

- Dotted comparisons follow version semantics: `MC >= 1.17` covers the whole 1.17.x line (including 1.17.1). Use an
  exact patch version (`MC >= 1.17.1`) only when the boundary is a specific patch.
- When the version list has no patch in between (e.g. `1.20.4` then `1.20.6`), a series-level boundary (`MC >= 1.18`)
  and a patch-level boundary (`MC >= 1.18.2`) are equivalent for the existing versions; prefer the one that matches the
  semantic intent.

#### Override Files and Nested Directives

- An override file may itself contain preprocessor directives to handle finer version differences inside its downstream
  range. Example: a `1.19.2` override keeping a `//#if MC >= 1.19.3` branch for the `CommandBuildContext`
  API change (`new CommandBuildContext(...)` vs `CommandBuildContext.simple(...)`).
- Override files propagate to all downstream versions in the dependency chain; if an even newer override exists for a
  later version, it takes precedence there.

#### Preprocessed Output Format

- The `build/preprocessed` output keeps `//#if`/`//#else`/`//#endif` directive lines and `//$$` prefixes: inactive
  branches remain as comments. **Lines without a `//$$` prefix are the active code** for that version. This output is
  generated (never edit it), but it can be read to verify how a directive expands for a specific version.

---

## Project Structure

### Version Configuration (Dynamic)

> **Important**: The content of `settings.json` changes over time. Agents should always read the actual `settings.json`
> to get the current supported version list instead of relying on any static documentation.

---

## Code Style

- **Checkstyle**: defined in `checkstyle.xml`.
- **EditorConfig**: defined in `.editorconfig`.

Agents must follow the rules defined in these files when writing code.

### Version Guide Javadoc Annotation

For every class that has an override file, append a version-guide list at the end of the class javadoc:

```java
/**
 * ...
 *
 * <li>mc1.14 ~ mc1.18: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19+        : subproject 1.19.2</li>
 */
```

- The `&lt;--------` arrow points to the row of the version this file belongs to: a main-project file points at the
  main-project row, an override file points at its own override row.
- `[dummy]` marks an empty class or a `@Mixin(DummyClass)` placeholder; real implementations do not carry it.
- Use bare `<li>` items (no `<ul>` wrapper; the project checkstyle does not enforce ul/li nesting).

Version range labeling rules (label version *segments*, not subproject names):

- Main project covering a whole series → drop the patch: `mc1.14 ~ mc1.18`.
- Override starting at the next series → `mc1.19+`.
- Main project covering the middle of a series → keep the patch: `mc1.14 ~ mc1.20.4`.
- Override starting at the next patch segment → `mc1.20.5+`.
- When an override serves the low versions and the main project the high ones (e.g. `1.15.2` override + `1.16.5`
  main project): override `mc1.14 ~ mc1.15`, main project `mc1.16+`.
- Align the colons of all rows to the same column.

### ImportOrder Pitfalls

- A `CHECKSTYLE.OFF: ImportOrder` guarded block (conditional imports) must be placed at the **end of its group**,
  directly followed by the next group; otherwise "Extra separation" is reported. Order inside a guarded block is not
  checked.

### Blank Line Before Block Statements

- Put a blank line before block statements (`if`/`for`/`while`/`switch`/`try`), except when the statement is the first
  statement of its block, or part of an `else`/`catch`/`finally` chain.

---

## Build and Check Commands

### Build

```bash
./gradlew build
```

- `checkstyleMain` runs automatically during `build`.

### Checkstyle (standalone)

```bash
./gradlew checkstyleMain
```

### Mixin Audit (must pass)

```bash
./gradlew runMixinAuditClient
```

- Only the client audit is required.
- The project must pass the mixin audit check.

---

## Agent Working Guidelines

### Researching Minecraft Source Code

- Prefer searching local caches for Minecraft source code first.
- If the source is not available in cache, run the `genSourcesWithVineflower` Gradle task of the corresponding
  subproject to generate it.

### Gradle Task Execution

- Do not run multiple Gradle tasks in parallel.

### Temporary Files

- Write all temporary files to the .tmp directory.

### When Modifying Code

1. **Determine target versions**: Identify the Minecraft version range affected by the change.
2. **Check Java version limit**: Use Java syntax appropriate for the target Minecraft version.
3. **Prefer mainProject edits**: Use preprocessor directives in `mainProject` to make changes.
4. **Handle override files**: Only create an override file in a subproject's `/src` if the modification ratio exceeds
   50%. When splitting a heavily-branched file into overrides, prefer the pattern of keeping the main project on a
   contiguous low version range and one override per higher version segment (each override may keep its own nested
   directives).
5. **Follow code style**: Ensure compliance with `checkstyle.xml` and `.editorconfig`, plus the project conventions in
   the Code Style section (version-guide javadoc, blank line before block statements, ImportOrder guarded-block
   placement).
6. **Update version guides**: Whenever an override is created or removed, update the version-guide javadoc of the
   affected classes (main project and override files) so the segments stay accurate.
7. **Verify dependency chain**: Confirm that changes do not break downstream modules.

### When Adding New Version Support

1. Update the version list in root `settings.json`.
2. Ensure preprocessor directives cover the new version.
3. Verify Java version compatibility settings.
4. Run full build and checks.

### Prohibited Actions

- Do not modify anything under magiclib-wrapper directly.
- Do not edit generated files under build/processed.
- Do not use Java syntax that exceeds the target version limit.
- Do not skip the Mixin audit check.
- Do not hardcode the version list; always read it dynamically from settings.json.
- Do not run Gradle tasks in parallel.
- Do not write temporary files outside the .tmp directory.
