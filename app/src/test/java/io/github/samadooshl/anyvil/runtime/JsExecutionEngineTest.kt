package io.github.samadooshl.anyvil.runtime

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class JsExecutionEngineTest {

    private val engine = JsExecutionEngine()

    @Test
    fun testConsoleLogOutput() {
        val outputs = mutableListOf<String>()
        val script = """
            console.log("Hello from Anyvil JS engine!");
            console.log("Math check:", 2 + 3);
        """.trimIndent()

        val result = engine.execute(script) { outputs.add(it) }

        assertTrue(result is ExecutionResult.Success)
        assertEquals(2, outputs.size)
        assertEquals("Hello from Anyvil JS engine!\n", outputs[0])
        assertEquals("Math check: 5\n", outputs[1])
    }

    @Test
    fun testExpressionReturnValue() {
        val outputs = mutableListOf<String>()
        val script = "10 * 4.2;"

        val result = engine.execute(script) { outputs.add(it) }

        assertTrue(result is ExecutionResult.Success)
        assertEquals(1, outputs.size)
        assertEquals("=> 42\n", outputs[0])
    }

    @Test
    fun testSyntaxErrorHandling() {
        val outputs = mutableListOf<String>()
        val brokenScript = "function broken( { return 42; }"

        val result = engine.execute(brokenScript) { outputs.add(it) }

        assertTrue(result is ExecutionResult.Failure)
        assertTrue(outputs.isNotEmpty())
        assertTrue(outputs[0].startsWith("Error [main.js:1]:"))
    }
}
