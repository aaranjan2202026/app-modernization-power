# Java Version & Framework Matrix

Pick the target **pair**, not just the Java version. A Java version without a compatible framework produces a broken build.

## Java LTS releases

| Java | Status | GA |
|---|---|---|
| 8 | legacy | 2014 |
| 11 | legacy LTS | 2018 |
| 17 | LTS | 2021 |
| 21 | LTS | 2023 |
| **25** | **current LTS** | 16 Sep 2025 |

## Spring Boot pairing — read this before choosing a target

| Spring Boot | Java support | OSS support status |
|---|---|---|
| 2.x | 8–17 | **EOL** |
| 3.0–3.4 | 17–23 | **EOL** |
| 3.5 | 17–24 | **EOL — 30 Jun 2026** |
| **4.0+** | **17–26**, first-class Java 25 | **current** |

Every Spring Boot 3.x branch is out of OSS support as of mid-2026. A project on
3.x receives no security fixes.

Spring Framework 7 recommends JDK 25+ for production.

### Consequence for target selection

| Target | Requires | Notes |
|---|---|---|
| Java 21 | Spring Boot 3.2+ | Works, but any 3.x is EOL |
| **Java 25** | **Spring Boot 4.0+** | Current supported combination |
| Java 25 on Spring Boot 3.4 | — | **Not supported.** Do not pair these. |

If a project sits on Spring Boot 3.4 and the goal is Java 25, that is **two
migrations**: Java version *and* Spring Boot 3.x → 4.x. Scope them separately —
Spring Boot 4 carries its own breaking changes. Do not silently bundle them.

## Feature availability by version

Apply only features available in the chosen target.

| Feature | Since | Notes |
|---|---|---|
| try-with-resources, diamond operator | 7 | |
| Lambdas, streams, `java.time`, `Optional` | 8 | |
| `var`, `List.of`/`Map.of`, private interface methods | 9–11 | |
| Switch expressions | 14 | |
| Text blocks | 15 | |
| Records, pattern matching for `instanceof` | 16 | |
| Sealed classes | 17 | |
| Virtual threads, sequenced collections, record patterns | 21 | |
| Unnamed variables `_`, statements before `super()` | 22 | |
| Markdown doc comments | 23 | |
| Stream gatherers, class-file API | 24 | |
| Scoped values, module import declarations, compact source files & instance `main`, flexible constructor bodies, KDF API, compact object headers | **25** | finalized |

### Java 25 preview features — do not use in a migration

These require `--enable-preview` and may change or vanish in a later release.
Compiling production code against them ties you to one JDK version.

| Feature | State in 25 |
|---|---|
| Structured concurrency (JEP 505) | 5th **preview** |
| Stable values (JEP 502) | **preview** |
| Primitive types in patterns (JEP 507) | 3rd **preview** |
| PEM encodings (JEP 470) | **preview** |

Structured concurrency is frequently misreported as final. It is not, as of 25.
Virtual threads (final since 21) are the safe concurrency choice.

## Compiling with a newer JDK than the target

A newer JDK can build for an older target and this is normal:

```xml
<maven.compiler.release>21</maven.compiler.release>
```

`--release 21` on JDK 25 emits Java 21 bytecode *and* rejects any API newer
than 21 — the guarantee `-source`/`-target` alone does not give you.

Match `JAVA_HOME` to the target only when the runtime must match for testing.

## Recommended migration paths

| From | To | Framework move |
|---|---|---|
| Java 7/8 servlets | Java 21 | Spring Boot 3.2+ (note: EOL) |
| Java 7/8 servlets | **Java 25** | **Spring Boot 4.x** |
| Java 11/17 | Java 21 | usually in-place |
| Java 17/21 on Boot 3.x | Java 25 | **requires Boot 4.x — separate migration** |

Prefer LTS targets. Migrating to a non-LTS release means another forced move in
six months.
