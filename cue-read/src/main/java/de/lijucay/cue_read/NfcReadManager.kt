package de.lijucay.cue_read

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import java.io.IOException

class NfcReadManager {
    companion object {
        private const val CUE_SCHEME = "cue://chip/"
    }

    fun read(tag: Tag): ReadResult {
        val ndef = Ndef.get(tag) ?: return ReadResult.NotNdefCompatible

        return try {
            ndef.connect()

            val message = ndef.cachedNdefMessage ?: ndef.ndefMessage ?: return ReadResult.EmptyTag

            extractCueChipId(message)
        } catch (e: IOException) {
            ReadResult.UnknownError(e)
        } finally {
            try { ndef.close() } catch (e: IOException) {  }
        }
    }

    fun read(ndefMessage: NdefMessage): ReadResult {
        return extractCueChipId(ndefMessage)
    }

    private fun extractCueChipId(message: NdefMessage): ReadResult {
        for (record in message.records) {
            val chipId = extractFromRecord(record)
            if (chipId != null) return ReadResult.Success(chipId)
        }

        return ReadResult.NotACueChip
    }

    private fun extractFromRecord(record: NdefRecord): String? {
        if (record.tnf != NdefRecord.TNF_WELL_KNOWN) return null
        if (!record.type.contentEquals(NdefRecord.RTD_URI)) return null

        return try {
            val uri = record.toUri()?.toString() ?: return null
            if (uri.startsWith(CUE_SCHEME)) {
                uri.removePrefix(CUE_SCHEME).takeIf { it.isNotBlank() }
            } else null
        } catch (e: Exception) {
            null
        }
    }
}