# Anyvil Architecture & System Design (Phase C)

This document formalizes the modular architecture, runtime isolation, storage subsystems, and extension boundaries for Anyvil on Android.

---

## 1. Architectural Style: Layered Domain-Driven Design

```
┌────────────────────────────────────────────────────────────────────────┐
│                       Presentation Layer (Compose)                     │
│   • EditorCanvas (TextBuffer, Gutter, CursorHandles, SelectionOverlay) │
│   • BottomActionBar (Thumb-zone triggers)                              │
│   • WorkspaceSheets (TerminalSheet, FileSheet, SearchSheet)            │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                       Domain & Coordination Layer                      │
│   • EditorState (Undo/Redo Stack, Multi-Cursor, FoldingIndex)          │
│   • TerminalSessionManager (PTY lifecycle, I/O multiplexer)            │
│   • ProjectRepository (Workspace tree, Active file state)              │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                    Infrastructure & Runtime Layer                      │
│   • QuickJSEngine (JNI embedded JS/TS runtime)                         │
│   • AndroidPtyManager (Native Linux PTY allocation & POSIX forkpty)    │
│   • DocumentStorageAdapter (SAF + internal private sandbox)            │
│   • WasmExtensionHost (Capability-gated sandboxed extensions)          │
└────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Architecture Decision Records (ADRs)

### ADR 003: Monolith-First Package Modularity vs Multi-Module Gradle

```
Status: Accepted
Context: Anyvil is developed directly on a phone in Termux proot (ARM64). Multi-module Gradle builds
         introduce significant Gradle daemon configuration overhead and memory pressure on mobile CPUs.
Decision: Maintain a single Gradle ':app' module with strict internal package boundaries 
          (core.model, core.editor, core.runtime, core.storage, and ui.*). 
          Extract into standalone Gradle modules only when binary distribution or independent library sharing requires it.
Consequences: Keeps on-device build times ultra-fast (<15s incremental) while preserving clean architectural boundaries.
```

### ADR 004: Dual-Tier Execution Engine (Embedded JNI + Android PTY)

```
Status: Accepted
Context: Android W^X rules prevent executing downloaded binaries, and spawning full shell processes
         for lightweight scripts (JS/Python/Lua) adds massive memory and latency overhead.
Decision: Anyvil implements a dual-tier execution model:
          1. Tier 1 (In-Process Embedded Engines): QuickJS, Lua, and Python linked via JNI.
             Sub-millisecond startup, zero process overhead, memory-contained.
          2. Tier 2 (PTY Terminal Subsystem): Real Linux pseudo-terminals (/dev/pts) using JNI openpty/forkpty
             for interactive shells and pre-packaged native toolchains extracted in the APK's lib/ directory.
Consequences: Instant script runs for 90% of user tasks, with real interactive terminal sessions for the rest.
```

### ADR 005: Dual-Storage Strategy (Internal Sandbox vs Storage Access Framework)

```
Status: Accepted
Context: Android Scoped Storage (SAF) introduces high latency when indexing thousands of files (e.g. node_modules or git objects),
         while the internal app directory (/data/data/...) provides maximum POSIX performance but isn't visible in general file managers.
Decision: Support both:
          1. Primary Workspaces: Stored in app internal filesDir/projects for full POSIX speed and Git reliability.
          2. Export / External Folders: Seamless export/import via Storage Access Framework (SAF) DocumentFile trees.
Consequences: Optimal I/O speed during editing and Git operations; full user ownership of code files via export.
```

---

## 3. Extension Subsystem & Security Boundaries

Extensions run inside a sandboxed WebAssembly (WASM) runtime. Extensions have **zero** ambient permissions:
* **No File Access:** Extensions cannot read or write arbitrary files unless explicitly authorized per project.
* **No Network Access:** Extensions cannot make HTTP requests unless the user grants network permission.
* **Pure API Contract:** Extensions communicate via JSON-RPC / message-passing with the Editor Event Bus:
  - `editor.onFileOpened(uri, language)`
  - `editor.provideDiagnostics(uri)`
  - `editor.registerCommand(id, handler)`
