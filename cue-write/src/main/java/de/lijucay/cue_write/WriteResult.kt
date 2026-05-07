package de.lijucay.cue_write

sealed interface WriteResult {
    data object Success : WriteResult
    data object TagLost : WriteResult
    data object NotWriteable : WriteResult
    data object InsufficientSize : WriteResult
    data object NotNdefCompatible : WriteResult
    data class UnknownError(val cause: Exception) : WriteResult
}