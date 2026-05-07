package de.lijucay.cue_write

import android.nfc.FormatException
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.Tag
import android.nfc.TagLostException
import android.nfc.tech.Ndef
import android.nfc.tech.NdefFormatable
import java.io.IOException
import java.util.UUID

class NfcWriteManager {
    companion object {
        private const val CUE_SCHEME = "cue://chip/"
    }

    fun write(tag: Tag): Pair<WriteResult, CueChip?> {
        val chipId = UUID.randomUUID().toString()
        val message = createNdefMessage(chipId)

        // Check if chip is NDEF-formatted
        val ndef = Ndef.get(tag)
        if (ndef != null) return writeToNdef(ndef, message, chipId)

        // If not, check if chip is formatable
        val formatable = NdefFormatable.get(tag)
        if (formatable != null) return formatAndWrite(formatable, message, chipId)

        return Pair(WriteResult.NotNdefCompatible, null)
    }

    private fun writeToNdef(
        ndef: Ndef,
        message: NdefMessage,
        chipId: String
    ): Pair<WriteResult, CueChip?> {
        return try {
            ndef.connect()

            if (!ndef.isWritable) {
                return Pair(WriteResult.NotWriteable, null)
            }

            if (ndef.maxSize < message.toByteArray().size) {
                return Pair(WriteResult.InsufficientSize, null)
            }

            ndef.writeNdefMessage(message)
            Pair(WriteResult.Success, CueChip(chipId))
        } catch (e: TagLostException) {
            Pair(WriteResult.TagLost, null)
        } catch (e: IOException) {
            Pair(WriteResult.UnknownError(e), null)
        } catch (e: FormatException) {
            Pair(WriteResult.UnknownError(e), null)
        } finally {
            try { ndef.close() } catch (_: IOException) {  }
        }
    }

    private fun formatAndWrite(
        formatable: NdefFormatable,
        message: NdefMessage,
        chipId: String
    ): Pair<WriteResult, CueChip?> {
        return try {
            formatable.connect()
            formatable.format(message)
            Pair(WriteResult.Success, CueChip(chipId))
        } catch (e: TagLostException) {
            Pair(WriteResult.TagLost, null)
        } catch (e: IOException) {
            Pair(WriteResult.UnknownError(e), null)
        } catch (e: FormatException) {
            Pair(WriteResult.UnknownError(e), null)
        } finally {
            try { formatable.close() } catch (_: IOException) {  }
        }
    }

    private fun createNdefMessage(chipId: String): NdefMessage {
        val uri = "$CUE_SCHEME$chipId"
        val record = NdefRecord.createUri(uri)
        return NdefMessage(arrayOf(record))
    }
}