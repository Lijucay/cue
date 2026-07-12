package de.lijucay.cue_write

sealed interface WriteResult {
    data class Success(val chip: CueChip) : WriteResult
    data object TagLost : WriteResult
    data object NotWriteable : WriteResult
    data object InsufficientSize : WriteResult
    data object NotNdefCompatible : WriteResult
    data object NotErasable : WriteResult
    data object EraseSuccess : WriteResult
    data class UnknownError(val cause: Exception) : WriteResult
    data class EraseException(val e: Exception) : WriteResult
}