package de.lijucay.cue_read

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import java.io.IOException

class NfcReadManager {
    companion object {
        private const val CUE_SCHEME = "cue://"
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
            try { ndef.close() } catch (_: IOException) {  }
        }
    }

    fun read(ndefMessage: NdefMessage): ReadResult {
        return extractCueChipId(ndefMessage)
    }

    private fun extractCueChipId(message: NdefMessage): ReadResult {
        for (record in message.records) {
            val (host, chipId) = extractFromRecord(record) ?: continue
            return ReadResult.Success(host, chipId)
        }

        return ReadResult.NotACueChip
    }

    private fun extractFromRecord(record: NdefRecord): Pair<String, String>? {
        if (record.tnf != NdefRecord.TNF_WELL_KNOWN) return null
        if (!record.type.contentEquals(NdefRecord.RTD_URI)) return null

        return try {
            val uri = record.toUri()?.toString() ?: return null
            if (!uri.startsWith(CUE_SCHEME)) return null

            val withoutScheme = uri.removePrefix(CUE_SCHEME)
            val parts = withoutScheme.split("/")
            if (parts.size < 2) return null

            val host = parts[0]
            val chipId = parts[1]

            if (host.isBlank() || chipId.isBlank()) null
            else Pair(host, chipId)
        } catch (_: Exception) {
            null
        }
    }
}