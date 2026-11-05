package io.github.samadooshl.anyvil.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.samadooshl.anyvil.theme.*

/**
 * Bottom Action Bar - 72dp height, thumb-friendly
 * All secondary UI in bottom sheets
 */
@Composable
fun BottomActionBar(
    modifier: Modifier = Modifier,
    showKeyboardHelper: Boolean = false,
    onRunClick: () -> Unit,
    onKeyboardClick: () -> Unit,
    onFilesClick: () -> Unit,
    onSearchClick: () -> Unit,
    onTerminalClick: () -> Unit,
    onMoreClick: () -> Unit,
    keyboardHelperContent: @Composable ColumnScope.() -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        // Keyboard helper row (conditionally shown)
        AnimatedVisibility(
            visible = showKeyboardHelper,
            enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BottomBarBackground)
                    .padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                keyboardHelperContent()
            }
        }

        // Main action bar
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = BottomBarBackground,
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(72.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionButton(text = "▶", onClick = onRunClick)
                ActionButton(text = "⌨", onClick = onKeyboardClick, isActive = showKeyboardHelper)
                ActionButton(text = "📁", onClick = onFilesClick)
                ActionButton(text = "🔍", onClick = onSearchClick)
                ActionButton(text = "▷", onClick = onTerminalClick)
                ActionButton(text = "•••", onClick = onMoreClick)
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    modifier: Modifier = Modifier,
    isActive: Boolean = false,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(if (isActive) AccentSoft else Color.Transparent)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Normal,
            color = if (isActive) Accent else BottomBarIconInactive
        )
    }
}