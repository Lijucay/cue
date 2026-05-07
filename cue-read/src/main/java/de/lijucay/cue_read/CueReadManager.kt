package de.lijucay.cue_read

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag

class CueReadManager(
    private val nfcReadManager: NfcReadManager = NfcReadManager()
) {
    fun read(intent: Intent): ReadResult {
        val rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        if (!rawMessages.isNullOrEmpty()) {
            val ndefMessage = rawMessages[0] as NdefMessage

            return nfcReadManager.read(ndefMessage)
        }

        val tag = intent.getParcelableExtra<Tag>(NfcAdapter.EXTRA_TAG)
            ?: return ReadResult.EmptyTag

        return nfcReadManager.read(tag)
    }
}