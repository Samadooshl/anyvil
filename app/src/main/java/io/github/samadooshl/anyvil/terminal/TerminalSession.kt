package io.github.samadooshl.anyvil.terminal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class TerminalSession(
    val id: String,
    val fileName: String,
    initialOutput: String = ""
) {
    var outputText by mutableStateOf(initialOutput)
        private set

    var isRunning by mutableStateOf(false)
    var isFinished by mutableStateOf(false)

    fun appendOutput(text: String) {
        outputText += text
    }

    fun getOutput(): String = outputText

    fun clear() {
        outputText = ""
    }
}
