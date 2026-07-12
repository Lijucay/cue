package de.lijucay.cue_write

sealed interface WriteResult {
    data class Success(val chip: CueChip) : WriteResult
    data object TagLost : WriteResult
    data object NotWriteable : WriteResult
    data object InsufficientSize : WriteResult
    data object NotNdefCompatible : WriteResult
    data object NotFormatable : WriteResult
    data object FormatSuccess : WriteResult
    data class UnknownError(val cause: Exception) : WriteResult
    data class FormatException(val e: android.nfc.FormatException) : WriteResult
}