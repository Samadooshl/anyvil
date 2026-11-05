package io.github.samadooshl.anyvil.terminal

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import java.util.UUID

/**
 * Manages multiple terminal sessions for concurrent execution
 */
class TerminalManager {

    private val _sessions: SnapshotStateList<TerminalSession> = mutableStateListOf()
    val sessions: List<TerminalSession> get() = _sessions

    private var _activeSessionId: String? = null
    val activeSessionId: String? get() = _activeSessionId

    val activeSession: TerminalSession?
        get() = _sessions.find { it.id == _activeSessionId }

    /**
     * Create a new terminal session
     */
    fun createSession(fileName: String): TerminalSession {
        val session = TerminalSession(
            id = UUID.randomUUID().toString(),
            fileName = fileName
        )
        _sessions.add(session)
        _activeSessionId = session.id
        return session
    }

    /**
     * Switch to a different session
     */
    fun switchSession(sessionId: String) {
        if (_sessions.any { it.id == sessionId }) {
            _activeSessionId = sessionId
        }
    }

    /**
     * Close a session
     */
    fun closeSession(sessionId: String) {
        val session = _sessions.find { it.id == sessionId }
        if (session != null) {
            session.isRunning = false
            _sessions.remove(session)

            // Switch to another session if we closed the active one
            if (_activeSessionId == sessionId) {
                _activeSessionId = _sessions.firstOrNull()?.id
            }
        }
    }

    /**
     * Get a session by ID
     */
    fun getSession(sessionId: String): TerminalSession? {
        return _sessions.find { it.id == sessionId }
    }

    /**
     * Clear all sessions
     */
    fun clearAll() {
        _sessions.forEach { it.isRunning = false }
        _sessions.clear()
        _activeSessionId = null
    }
}