package de.lijucay.cue_read

sealed interface ReadResult {
    data class Success(val chipId: String) : ReadResult
    data object NotACueChip : ReadResult
    data object EmptyTag : ReadResult
    data object NotNdefCompatible : ReadResult
    data class UnknownError(val cause: Exception) : ReadResult
}