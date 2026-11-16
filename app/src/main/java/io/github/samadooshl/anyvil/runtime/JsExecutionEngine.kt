package io.github.samadooshl.anyvil.runtime

import org.mozilla.javascript.BaseFunction
import org.mozilla.javascript.Context
import org.mozilla.javascript.RhinoException
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject

/**
 * On-device JavaScript execution engine powered by Mozilla Rhino.
 * Operates in interpreted mode (optimizationLevel = -1) to strictly adhere to
 * Android's W^X memory execution boundaries with zero dynamic binary generation.
 */
class JsExecutionEngine {

    fun execute(
        source: String,
        sourceName: String = "main.js",
        onOutput: (String) -> Unit
    ): ExecutionResult {
        val startTime = System.currentTimeMillis()
        val context = Context.enter()
        return try {
            // Optimization level -1 disables dynamic bytecode generation (essential for Android W^X)
            context.optimizationLevel = -1
            val scope = context.initSafeStandardObjects()

            // Inject console object with log, error, warn, and info
            val console = object : ScriptableObject() {
                override fun getClassName(): String = "Console"
            }

            val logFunction = object : BaseFunction() {
                override fun call(
                    cx: Context,
                    scope: Scriptable,
                    thisObj: Scriptable,
                    args: Array<out Any?>
                ): Any? {
                    val message = args.joinToString(" ") { Context.toString(it) }
                    onOutput(message + "\n")
                    return Context.getUndefinedValue()
                }
            }

            ScriptableObject.putProperty(console, "log", logFunction)
            ScriptableObject.putProperty(console, "info", logFunction)
            ScriptableObject.putProperty(console, "warn", logFunction)
            ScriptableObject.putProperty(console, "error", logFunction)
            ScriptableObject.putProperty(scope, "console", console)

            val result = context.evaluateString(scope, source, sourceName, 1, null)
            val duration = System.currentTimeMillis() - startTime

            if (result != null && result != Context.getUndefinedValue()) {
                onOutput("=> " + Context.toString(result) + "\n")
            }

            ExecutionResult.Success(durationMs = duration)
        } catch (e: RhinoException) {
            val duration = System.currentTimeMillis() - startTime
            val errorMsg = "Error [${e.sourceName()}:${e.lineNumber()}]: ${e.details()}\n"
            onOutput(errorMsg)
            ExecutionResult.Failure(error = errorMsg, durationMs = duration)
        } catch (e: Throwable) {
            val duration = System.currentTimeMillis() - startTime
            val errorMsg = "Fatal Error: ${e.message ?: e.toString()}\n"
            onOutput(errorMsg)
            ExecutionResult.Failure(error = errorMsg, durationMs = duration)
        } finally {
            Context.exit()
        }
    }
}

sealed interface ExecutionResult {
    val durationMs: Long

    data class Success(override val durationMs: Long) : ExecutionResult
    data class Failure(val error: String, override val durationMs: Long) : ExecutionResult
}
