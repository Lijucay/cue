package de.lijucay.cue_read

import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.tech.Ndef
import java.io.IOException

class NfcReadManager {

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
            val uri = record.toUri() ?: return null
            if (uri.scheme != "cue") return null

            val host = uri.host?.takeIf { it.isNotBlank() } ?: return null
            val chipId = uri.pathSegments.firstOrNull()?.takeIf { it.isNotBlank() } ?: return null

            Pair(host, chipId)
        } catch (_: Exception) {
            null
        }
    }
}