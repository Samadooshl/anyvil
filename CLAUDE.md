# Anyvil — Architecture, Toolchain & Engineering Guidelines

Anyvil ("any language, forged on your phone") is an original, all-in-one mobile IDE for Android and iOS: write, organize, build, run, debug, and manage projects directly on a phone. It is public, long-term, open-source (MPL-2.0), offline-first, and free forever (no ads, subscriptions, paid features, mandatory accounts, or telemetry).

---

## 1. Locked Toolchain (Verified Working On-Device)

> [!CAUTION]
> Do not modify or upgrade these versions without explicit user approval. Never use APIs newer than this toolchain supports.

* **JDK:** 17
* **Gradle:** 8.2.1 via `./gradlew` (never plain `gradle`)
* **Android Gradle Plugin (AGP):** 8.1.2
* **Kotlin:** 1.9.20
* **Compose Compiler:** 1.5.4
* **Compose BOM:** 2024.06.00 (Material3 1.2.x)
* **compileSdk:** 34
* **minSdk:** 31
* **targetSdk:** 34
* **build-tools:** 33.0.1
* **JVM Target:** 17 (Java and Kotlin)
* **Repository Declaration:** Only in `settings.gradle` (never declare an `allprojects` block in `build.gradle`)
* **SDK Path:** `ANDROID_HOME=/root/android/sdk`, `local.properties` specifies `sdk.dir` (never commit `local.properties`)
* **aapt2 arm64 Override:** Configured in `~/.gradle/gradle.properties` (`android.aapt2FromMavenOverride=/usr/bin/aapt2`)

---

## 2. Development Environment & Constraints

* **Development Host:** Developed entirely on an Android phone inside Ubuntu proot (aarch64) in Termux. No PC.
* **Workflows & Tools:** Every script, tool, or workflow must run comfortably inside a phone terminal.
* **Continuous Integration:** Heavy builds and all iOS builds run on free GitHub Actions CI.
* **Distribution Targets:**
  * **Android:** F-Droid. Must strictly adhere to F-Droid criteria: 100% FOSS dependencies, zero proprietary binary blobs / SDKs, no Google Play Services, reproducible builds from public source.
  * **iOS:** App Store. Compliant with iOS sandboxing, no-JIT, and no unapproved downloaded executable restrictions.
* **Platform Separation:** Android and iOS have fundamentally different runtime, security, and execution models. Never assume or pretend they have identical capabilities.

---

## 3. Engineering Rules

1. **Small Increments:** Build in small, runnable steps (~300 lines of changes per step).
2. **Build Verification:** After **every** change, execute `./gradlew assembleDebug` and ensure all compilation errors are resolved before concluding a task.
3. **Explicit Code Quality:**
   * Explicit imports only; no wildcard imports or unresolved references.
   * Accompany features with unit/integration tests where applicable.
   * Never claim a feature works unless it has been explicitly run and verified; otherwise label it `untested`.
   * No stubs presented as finished implementations.
4. **Dependency Hygiene:** Document the license of every dependency introduced. Prioritize permissive, clean FOSS licenses compatible with MPL-2.0 and F-Droid.
5. **Git Hygiene:** Commit after each working, verified step. Keep commits atomic and clean.

---

## 4. Honesty Protocol

* **Facts, Assumptions, Guesses:** Clearly delineate what is verified fact (tested code, verified docs), what is a working architectural assumption, and what is a speculative guess.
* **Platform Infeasibility:** Call out impossible platform capabilities plainly (e.g., dynamic native code compilation on iOS without JIT/entitlements, Android W^X restrictions) and offer the honest, viable alternative.
* **Doc Verification:** Proactively flag any third-party library features or APIs that need live documentation checks.
* **Requirement Conflicts:** State conflicting constraints immediately and recommend reasoned architectural trade-offs.
