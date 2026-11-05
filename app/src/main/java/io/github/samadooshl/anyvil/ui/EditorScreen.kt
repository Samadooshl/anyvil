package io.github.samadooshl.anyvil.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.samadooshl.anyvil.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    fileName: String = "main.js",
    content: String = "",
    onContentChange: (String) -> Unit = {},
    cursorLine: Int = 1,
    cursorColumn: Int = 1,
    modifier: Modifier = Modifier
) {
    val lineNumbers = remember(content) {
        content.lineCount().coerceAtLeast(1)
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Top bar with file name and cursor position
        TopAppBar(
            title = { Text(fileName) },
            actions = {
                Text(
                    text = "$cursorLine:$cursorColumn",
                    style = MaterialTheme.typography.labelMedium,
                    color = TextSecondary
                )
                Spacer(Modifier.width(16.dp))
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = SurfaceDark,
                titleContentColor = TextPrimary,
                actionIconContentColor = TextPrimary
            )
        )

        // Editor content
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(EditorBackground)
        ) {
            // Line numbers
            Column(
                modifier = Modifier
                    .width(48.dp)
                    .fillMaxHeight()
                    .background(SurfaceDark)
            ) {
                // Line numbers column
                for (i in 1..lineNumbers) {
                    Text(
                        text = i.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp, horizontal = 8.dp),
                        style = TextStyle(
                            color = LineNumber,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 14.sp,
                            textAlign = TextAlign.Right
                        )
                    )
                }
            }

            // Divider
            Box(
                modifier = Modifier
                    .width(1.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF333333))
            )

            // Code editor
            CompositionLocalProvider(
                LocalTextSelectionColors provides TextSelectionColors(
                    handleColor = Accent,
                    backgroundColor = Selection
                )
            ) {
                BasicTextField(
                    value = content,
                    onValueChange = onContentChange,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    textStyle = TextStyle(
                        color = TextPrimary,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    ),
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(Cursor)
                )
            }
        }
    }
}

private fun String.lineCount(): Int {
    return this.count { it == '\n' } + 1
}