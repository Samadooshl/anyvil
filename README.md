# Anyvil

> *Any language, forged on your phone.*

Anyvil is an original, all-in-one mobile IDE for Android and iOS designed from the ground up for phone ergonomics: write, organize, build, run, debug, and manage code without requiring a PC.

It is **not** a VS Code clone or a shrunk desktop IDE. Anyvil has its own visual identity, custom design language, modular architecture, and thumb-first mobile UX.

---

## Core Principles

* **Mobile-First Ergonomics:** Code fills the entire viewport. Secondary surfaces (terminals, file trees, search, output) appear as bottom sheets anchored in the thumb zone. A dedicated symbol row boosts keyboard speed.
* **100% Free & Open Source:** Licensed under [MPL-2.0](LICENSE). No advertisements, subscriptions, paid feature gating, telemetry, or mandatory accounts.
* **Offline-First:** Local build and run wherever the mobile operating system permits; remote/cloud integration is strictly optional.
* **F-Droid Compliant (Android):** 100% FOSS dependencies, zero Google Play Services or proprietary binary blobs, reproducible builds.
* **App Store Compliant (iOS):** Adheres to iOS sandbox and security rules without compromising editor capability.
* **Independent Platform Execution:** Android and iOS are engineered separately respecting platform capabilities rather than forcing artificial abstractions.

---

## Toolchain & Requirements (Android)

* **JDK:** 17
* **Gradle:** 8.2.1 (use `./gradlew`)
* **Android Gradle Plugin:** 8.1.2
* **Kotlin:** 1.9.20
* **Jetpack Compose Compiler:** 1.5.4
* **Compose BOM:** 2024.06.00 (Material3 1.2.x)
* **Compile SDK:** 34
* **Min SDK:** 31
* **Target SDK:** 34

---

## Building Locally

Anyvil is developed directly on-device using a phone terminal (Ubuntu proot on Termux).

```bash
# Check prerequisites and build debug APK
./gradlew assembleDebug

# Output APK location
app/build/outputs/apk/debug/app-debug.apk
```

---

## Project Structure

```
~/anyvil/
├── app/
│   ├── build.gradle              # Application configuration (minSdk 31, targetSdk 34)
│   └── src/main/
│       ├── AndroidManifest.xml   # Manifest referencing @string/app_name
│       ├── java/io/github/samadooshl/anyvil/
│       │   ├── MainActivity.kt   # App entry point & scaffold coordination
│       │   ├── terminal/         # Multi-window terminal manager & session state
│       │   ├── theme/            # Dark-first color palette, typography & theme
│       │   └── ui/               # EditorScreen, BottomActionBar, BottomSheets
│       └── res/                  # App resources (strings.xml, colors.xml)
├── gradle/                       # Gradle wrapper files
├── CLAUDE.md                     # Locked engineering rules and architecture guidelines
├── LICENSE                       # Mozilla Public License Version 2.0 (MPL-2.0)
├── README.md                     # Project overview and instructions
└── settings.gradle               # Root project definition ('Anyvil')
```

---

## License

This project is licensed under the **Mozilla Public License Version 2.0 (MPL-2.0)**. See the [LICENSE](LICENSE) file for details.
