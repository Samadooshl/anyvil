package io.github.samadooshl.anyvil.ui

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.samadooshl.anyvil.terminal.TerminalSession
import io.github.samadooshl.anyvil.theme.*

/**
 * Bottom Sheet for terminal with multi-tab support
 */
@Composable
fun TerminalSheet(
    modifier: Modifier = Modifier,
    sessions: List<TerminalSession>,
    activeSessionId: String?,
    onSessionSwitch: (String) -> Unit,
    onNewTerminal: () -> Unit,
    onCloseSession: (String) -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark)
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(TextSecondary)
            )
        }

        // Header with tabs
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Terminal",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.weight(1f))

            // New terminal button
            TextButton(onClick = onNewTerminal) {
                Text(
                    text = "[+]",
                    color = Accent,
                    fontSize = 14.sp
                )
            }

            // Close button
            TextButton(onClick = onDismiss) {
                Text(
                    text = "✕",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        // Terminal tabs
        if (sessions.isNotEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                sessions.forEach { session ->
                    TerminalTab(
                        session = session,
                        isActive = session.id == activeSessionId,
                        onClick = { onSessionSwitch(session.id) },
                        onClose = { onCloseSession(session.id) }
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = TextDisabled
        )

        // Terminal output (50% screen height)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .background(TerminalBackground)
                .padding(16.dp)
        ) {
            val activeSession = sessions.find { it.id == activeSessionId }

            if (activeSession != null) {
                Column {
                    Text(
                        text = activeSession.getOutput().ifEmpty { "Ready...\n" },
                        style = androidx.compose.ui.text.TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = 12.sp,
                            color = TerminalText
                        )
                    )
                }
            } else {
                Text(
                    text = "No terminal active. Tap [+] to create a new terminal.\n",
                    style = androidx.compose.ui.text.TextStyle(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 12.sp,
                        color = TextSecondary
                    )
                )
            }
        }
    }
}

@Composable
fun TerminalTab(
    session: TerminalSession,
    isActive: Boolean,
    onClick: () -> Unit,
    onClose: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            .background(if (isActive) SurfaceElevated else SurfaceDark)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Running indicator
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(
                    when {
                        session.isRunning -> Success
                        session.isFinished -> TextSecondary
                        else -> TextDisabled
                    }
                )
        )

        Text(
            text = session.fileName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = if (isActive) Accent else TextPrimary
        )

        // Close tab
        Box(
            modifier = Modifier
                .size(16.dp)
                .clip(CircleShape)
                .clickable {
                    if (!session.isRunning) onClose()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "×",
                fontSize = 14.sp,
                color = TextSecondary
            )
        }
    }
}

/**
 * File browser bottom sheet
 */
@Composable
fun FileBrowserSheet(
    modifier: Modifier = Modifier,
    files: List<FileItem>,
    onFileClick: (FileItem) -> Unit,
    onNewFile: () -> Unit,
    onDismiss: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SurfaceDark)
    ) {
        // Handle bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(TextSecondary)
            )
        }

        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Files",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )

            TextButton(onClick = onNewFile) {
                Text(
                    text = "[+] New",
                    color = Accent,
                    fontSize = 14.sp
                )
            }

            TextButton(onClick = onDismiss) {
                Text(
                    text = "✕",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }

        HorizontalDivider(color = TextDisabled)

        // File list
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            itemsIndexed(files) { _, file ->
                FileListItem(
                    file = file,
                    onClick = { onFileClick(file) }
                )
            }
        }
    }
}

data class FileItem(
    val name: String,
    val isDirectory: Boolean,
    val path: String = ""
)

@Composable
fun FileListItem(
    file: FileItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = if (file.isDirectory) "📁" else "📄",
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = file.name,
            style = MaterialTheme.typography.bodyLarge,
            color = TextPrimary
        )
    }
}

/**
 * Keyboard helper row
 */
@Composable
fun KeyboardHelper(
    modifier: Modifier = Modifier,
    onKeyClick: (String) -> Unit
) {
    val row1 = listOf(";", "{", "}", "(", ")", "[", "]", "<", ">", "=", "!")
    val row2 = listOf("&", "|", "^", "~", "+", "-", "*", "/", "%", "@", "#")
    val row3 = listOf("Tab", "←", "→", "Home", "End", "Done")

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        // Row 1
        KeyRow(keys = row1, onKeyClick = onKeyClick)

        // Row 2
        KeyRow(keys = row2, onKeyClick = onKeyClick)

        // Row 3
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            row3.forEach { key ->
                val span = if (key == "Tab" || key == "Done") 2 else 1
                KeyButton(
                    text = key,
                    modifier = Modifier.weight(span * 1f),
                    onClick = { onKeyClick(key) }
                )
            }
        }
    }
}

@Composable
fun KeyRow(
    keys: List<String>,
    onKeyClick: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        keys.forEach { key ->
            KeyButton(
                text = key,
                modifier = Modifier.weight(1f),
                onClick = { onKeyClick(key) }
            )
        }
    }
}

@Composable
fun KeyButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceElevated)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = Accent
        )
    }
}