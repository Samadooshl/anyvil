# Anyvil UX & Ergonomics Specification (Phase D)

Anyvil is designed specifically for touch interaction on phones. It is editor-first and thumb-first.

---

## 1. Phone Viewport Layout (Portrait)

```
┌────────────────────────────────────────────────────────┐
│ [main.js]  ● unmodified                     [••• Menu] │ ◄ Top Status (32dp)
├────────────────────────────────────────────────────────┤
│ 1 │ import { forge } from 'anyvil';                    │
│ 2 │                                                    │
│ 3 │ function init() {                                  │
│ 4 │     console.log("Forged on your phone!");          │
│ 5 │ }                                                  │
│   │                                                    │
│   │                                                    │
│   │              [ EDITOR CANVAS ]                     │
│   │         Full-screen code workspace                 │
│   │                                                    │
│   │                                                    │
│   │                                                    │
├────────────────────────────────────────────────────────┤
│ [Tab]  [ { ]  [ } ]  [ ( ]  [ ) ]  [ " ]  [ ; ]  [ ← ] │ ◄ Keyboard Helper
│ [ → ]  [ = ]  [ + ]  [ - ]  [ * ]  [ / ]  [ $ ]  [ > ] │   (48dp targets)
├────────────────────────────────────────────────────────┤
│  [ ▶ Run ]   [ ⌨ Keyb ]   [ 📁 Files ]   [ ▷ Term ]   │ ◄ Bottom Action Bar
│   (Amber)     (Toggle)      (Sheet)        (Sheet)     │   (72dp Thumb-Zone)
└────────────────────────────────────────────────────────┘
```

---

## 2. Bottom Sheet Workspaces

Instead of cramped side panels or tabs that consume horizontal space on a 6-inch screen, all auxiliary tools appear as smooth, dismissible bottom sheets:

1. **Terminal Sheet (50% / 90% snap height):**
   * Multi-tab bar at top of sheet with running/idle status dots.
   * Monospace terminal canvas with ANSI color support.
   * Floating quick-commands (`clear`, `kill`, `ctrl-c`).
2. **File Browser Sheet (60% snap height):**
   * Hierarchical tree view with folder icons, file size, and git status color indicators.
   * Top bar: `+ New File`, `+ New Folder`, `Import Folder`.
3. **Search & Replace Sheet (40% snap height):**
   * Regex toggle, case-sensitivity toggle, whole-word matching.
   * Real-time match count with jump-to-next buttons.
4. **Command Palette Sheet:**
   * Global fuzzy search for actions (format code, toggle dark mode, switch project, git commit).

---

## 3. Keyboard Helper Row (Ergonomic Tiers)

The keyboard helper row sits directly above the Android soft keyboard or at the bottom bar when closed:
* **Touch Targets:** 48dp × 48dp minimum to guarantee zero mis-taps.
* **Haptic Feedback:** Subtle tactile response on symbol insertion.
* **Symbol Sets:**
  * **Punctuation & Delimiters:** `{`, `}`, `[`, `]`, `(`, `)`, `;`, `:`, `,`, `.`, `"`, `'`, `` ` ``
  * **Operators & Special:** `=`, `+`, `-`, `*`, `/`, `%`, `!`, `?`, `&`, `|`, `^`, `~`, `<`, `>`, `$`, `#`, `@`
  * **Navigation & Formatting:** `Tab`, `Indent`, `Dedent`, `←`, `→`, `Home`, `End`, `Undo`, `Redo`

---

## 4. Tablet & Foldable Expansion (Dual-Pane)

When screen width exceeds 600dp (foldables unfolded or tablets):
* **Left Pane (320dp):** Persistent File Tree or Terminal Session.
* **Right Pane (Remaining width):** Editor Canvas with split-view capabilities (Editor + Live HTML/JS Preview side-by-side).
