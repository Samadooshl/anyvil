package io.github.samadooshl.anyvil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.samadooshl.anyvil.terminal.TerminalManager
import io.github.samadooshl.anyvil.terminal.TerminalSession
import io.github.samadooshl.anyvil.theme.*
import io.github.samadooshl.anyvil.ui.*
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    // Terminal manager for multi-window terminal support
    private val terminalManager = TerminalManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create sample JavaScript file
        val sampleFile = File(filesDir, "main.js").apply {
            if (!exists()) {
                writeText("""
// Anyvil IDE - Sample JavaScript
console.log("Hello from Anyvil!");
console.log("This is a mobile-first editor!");

// Simple calculation
let sum = 0;
for (let i = 1; i <= 10; i++) {
    sum += i;
}
console.log("Sum 1-10: " + sum);

// Function example
function greet(name) {
    return "Hello, " + name + "!";
}
console.log(greet("Developer"));

console.log("✓ Execution complete!");
                """.trimIndent())
            }
        }

        setContent {
            AnyvilTheme {
                AnyvilApp(
                    file = sampleFile,
                    terminalManager = terminalManager
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnyvilApp(
    file: File,
    terminalManager: TerminalManager
) {
    // UI state
    var code by remember(file) { mutableStateOf(file.readText()) }
    var showKeyboardHelper by remember { mutableStateOf(false) }
    var showFilesSheet by remember { mutableStateOf(false) }
    var showTerminalSheet by remember { mutableStateOf(false) }
    var showSearchSheet by remember { mutableStateOf(false) }
    var showMoreSheet by remember { mutableStateOf(false) }
    var cursorLine by remember { mutableStateOf(1) }
    var cursorColumn by remember { mutableStateOf(1) }

    Scaffold(
        containerColor = BackgroundDark
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Editor (main content)
            EditorScreen(
                fileName = file.name,
                content = code,
                onContentChange = {
                    code = it
                    file.writeText(it)
                    // Update cursor position
                    val lines = it.substringBeforeLast('\n')
                    cursorLine = lines.count { c -> c == '\n' } + 1
                    cursorColumn = (it.substringAfterLast('\n').length + 1).coerceAtLeast(1)
                },
                cursorLine = cursorLine,
                cursorColumn = cursorColumn,
                modifier = Modifier.weight(1f)
            )

            // Bottom Action Bar
            BottomActionBar(
                showKeyboardHelper = showKeyboardHelper,
                onRunClick = {
                    // Create new terminal session and execute
                    val session = terminalManager.createSession(file.name)
                    session.appendOutput("> Running ${file.name}...\n")
                    showTerminalSheet = true

                    // Simulate execution (in real app, this would run JavaScript)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        session.appendOutput("> Hello from Anyvil!\n")
                        session.appendOutput("> Sum 1-10: 55\n")
                        session.appendOutput("> ✓ Complete!\n")
                        session.isFinished = true
                    }
                },
                onKeyboardClick = { showKeyboardHelper = !showKeyboardHelper },
                onFilesClick = { showFilesSheet = true },
                onSearchClick = { showSearchSheet = true },
                onTerminalClick = { showTerminalSheet = true },
                onMoreClick = { showMoreSheet = true },
                keyboardHelperContent = {
                    KeyboardHelper(onKeyClick = { key ->
                        when (key) {
                            "Done" -> showKeyboardHelper = false
                            "Tab" -> code += "    "
                            "←" -> { /* cursor left */ }
                            "→" -> { /* cursor right */ }
                            "Home" -> { /* cursor to start */ }
                            "End" -> { /* cursor to end */ }
                            else -> code += key
                        }
                    })
                }
            )
        }

        // Bottom Sheets
        if (showFilesSheet) {
            ModalBottomSheet(
                onDismissRequest = { showFilesSheet = false },
                containerColor = SurfaceDark,
                scrimColor = Color.Black.copy(alpha = 0.5f)
            ) {
                FileBrowserSheet(
                    files = listOf(
                        FileItem("main.js", false),
                        FileItem("utils.js", false),
                        FileItem("src", true),
                        FileItem("assets", true)
                    ),
                    onFileClick = { /* Switch to file */ },
                    onNewFile = { /* Create new file */ },
                    onDismiss = { showFilesSheet = false }
                )
            }
        }

        if (showTerminalSheet) {
            ModalBottomSheet(
                onDismissRequest = { showTerminalSheet = false },
                containerColor = SurfaceDark,
                scrimColor = Color.Black.copy(alpha = 0.5f)
            ) {
                TerminalSheet(
                    sessions = terminalManager.sessions,
                    activeSessionId = terminalManager.activeSessionId,
                    onSessionSwitch = { terminalManager.switchSession(it) },
                    onNewTerminal = { terminalManager.createSession("shell") },
                    onCloseSession = { terminalManager.closeSession(it) },
                    onDismiss = { showTerminalSheet = false }
                )
            }
        }
    }
}