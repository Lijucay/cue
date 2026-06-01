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
        private const val CUE_SCHEME = "cue://"
    }

    fun write(tag: Tag, host: String): WriteResult =
        write(tag, host, UUID.randomUUID())

    fun write(tag: Tag, host: String, id: UUID): WriteResult {
        val chipId = id.toString()
        val message = createNdefMessage(chipId, host)

        val ndef = Ndef.get(tag)
        if (ndef != null) return writeToNdef(ndef, message, chipId)

        val formatable = NdefFormatable.get(tag)
        if (formatable != null) return formatAndWrite(formatable, message, chipId)

        return WriteResult.NotNdefCompatible
    }

    private fun writeToNdef(
        ndef: Ndef,
        message: NdefMessage,
        chipId: String
    ): WriteResult {
        return try {
            ndef.connect()

            if (!ndef.isWritable) {
                return WriteResult.NotWriteable
            }

            if (ndef.maxSize < message.toByteArray().size) {
                return WriteResult.InsufficientSize
            }

            ndef.writeNdefMessage(message)
            WriteResult.Success(CueChip(chipId))
        } catch (_: TagLostException) {
            WriteResult.TagLost
        } catch (e: IOException) {
            WriteResult.UnknownError(e)
        } catch (e: FormatException) {
            WriteResult.UnknownError(e)
        } finally {
            try { ndef.close() } catch (_: IOException) {  }
        }
    }

    private fun formatAndWrite(
        formatable: NdefFormatable,
        message: NdefMessage,
        chipId: String
    ): WriteResult {
        return try {
            formatable.connect()
            formatable.format(message)
            WriteResult.Success(CueChip(chipId))
        } catch (_: TagLostException) {
            WriteResult.TagLost
        } catch (e: IOException) {
            WriteResult.UnknownError(e)
        } catch (e: FormatException) {
            WriteResult.UnknownError(e)
        } finally {
            try { formatable.close() } catch (_: IOException) {  }
        }
    }

    private fun createNdefMessage(chipId: String, host: String): NdefMessage {
        val uri = "$CUE_SCHEME$host/$chipId"
        val record = NdefRecord.createUri(uri)
        return NdefMessage(arrayOf(record))
    }
}

