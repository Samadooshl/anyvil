package io.github.samadooshl.anyvil.terminal

data class TerminalSession(
    val id: String,
    val fileName: String,
    val output: StringBuilder = StringBuilder(),
    var isRunning: Boolean = false,
    var isFinished: Boolean = false
) {
    fun appendOutput(text: String) {
        output.append(text)
    }

    fun getOutput(): String = output.toString()

    fun clear() {
        output.clear()
    }
}