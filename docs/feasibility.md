# Anyvil Feasibility & Platform Constraints (Phase B)

Anyvil is an original, all-in-one mobile IDE developed on and for mobile devices. This document establishes technical feasibility, execution limits, runtime strategies, and F-Droid packaging constraints.

---

## 1. Operating System & Security Execution Boundaries

### Android Execution Boundaries (API 31 - 34)

1. **$W \oplus X$ (Write XOR Execute) Memory Enforcement:**
   * Modern Linux kernels and Android ART strictly prohibit pages from being simultaneously writable and executable.
   * *Impact:* Dynamic JIT compilation requires distinct write and execute allocation phases or JIT-less bytecode interpretation.
2. **Execution Restrictions on App Storage (API 29+):**
   * Android blocks direct `exec()` invocation of arbitrary binaries located in app-writable storage (such as `filesDir` or `cacheDir`).
   * *Strategy:* Native runtimes and CLI utilities are packaged inside the APK's `lib/<abi>/lib*.so` directory. The Android Package Manager automatically unpacks these into the executable `/data/app/.../lib/` partition with `r-xr-xr-x` permissions.
3. **F-Droid Compliance & FOSS Integrity:**
   * Must build 100% from public source code on F-Droid's isolated build infrastructure.
   * Zero proprietary SDKs, zero Google Play Services dependencies, and strictly reproducible builds.

### iOS Status (Deferred)
* iOS support is deferred to a future development cycle. The architecture maintains modular core interfaces so an iOS client (e.g. SwiftUI or Compose Multiplatform) can be introduced without Android regressions.

---

## 2. Feature Feasibility Matrix (Android)

| Feature | Status | Confidence | Technical Mechanism |
| :--- | :---: | :---: | :--- |
| **Code Editor Core** | **Native** | 99% | Custom Compose text canvas with direct text layout engine; Tree-sitter / TextMate syntax tokenization. |
| **Keyboard Symbol Row** | **Native** | 100% | 48dp+ thumb-anchored symbol row injecting key events directly into the editor state. |
| **File Tree & Navigation** | **Native** | 100% | Scoped storage access using Android Storage Access Framework (SAF) and internal app sandbox. |
| **Bottom Sheet Workspaces** | **Native** | 100% | Compose `ModalBottomSheet` surfaces anchored in the thumb zone for terminals, search, and diagnostics. |
| **Multi-Terminal Surface** | **Native / JNI** | 95% | Android PTY (pseudo-terminal) allocating real Linux pseudo-terminals hooked to embedded processes via JNI. |
| **Live HTML/CSS/JS Preview** | **Native** | 100% | Embedded Android `WebView` with virtual asset stream interception (offline-first). |
| **On-Device LSP (Language Server)** | **Workaround** | 85% | In-process AST analyzers or embedded WASM / Java language servers communicating over local IPC. |
| **On-Device DAP (Debugger)** | **Workaround** | 80% | Socket-based debug adapter connections to local interpreted runtimes (QuickJS, Python). |
| **Sandboxed Extensions** | **Native** | 90% | WebAssembly (WASM) plugin runtimes with strictly capability-gated host APIs. |

---

## 3. Language Runtime Feasibility Matrix (Android)

| Language | Status | Strategy |
| :--- | :---: | :--- |
| **JavaScript / TypeScript** | **Native** | Embedded QuickJS native C library via JNI. Fast, lightweight, zero external process overhead. TypeScript transpiled on-device via standalone JS transpiler. |
| **Python** | **Workaround** | Embedded CPython runtime packaged as `libpython.so` with bundled standard library. Executes offline. |
| **Lua 5.4** | **Native** | Native C Lua interpreter linked via JNI. Sub-millisecond execution with negligible APK size impact (~300KB). |
| **WebAssembly** | **Native** | Embedded WebAssembly runtime engine for extensions and portable toolchains. |
| **C / C++** | **Workaround** | Clang/LLVM toolchain packaged in JNI or compiled to WebAssembly for local compilation. |
| **Compiled (Rust/Go)** | **Hybrid** | Local compilation via specialized toolchain packages or optional remote runner (LAN PC or GitHub Actions CI). |

---

## 4. Verification Checkpoints

* **Verify JNI PTY allocation:** Confirm openpty/forkpty behavior across API 31 through 34.
* **Verify QuickJS bindings:** Confirm zero-overhead execution for script evaluation.
* **Verify SAF performance:** Profile reading/writing directories with 1,000+ files via Android `DocumentFile`.
