package de.lijucay.cue_read

import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Build

class CueReadManager(
    private val nfcReadManager: NfcReadManager = NfcReadManager()
) {
    fun read(intent: Intent): ReadResult {
        val rawMessages = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES,
                NdefMessage::class.java
            )
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        }

        if (!rawMessages.isNullOrEmpty()) {
            val ndefMessage = rawMessages[0] as NdefMessage

            return nfcReadManager.read(ndefMessage)
        }

        val tag = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG, Tag::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(NfcAdapter.EXTRA_TAG)
        } ?: return ReadResult.EmptyTag

        return nfcReadManager.read(tag)
    }
}
