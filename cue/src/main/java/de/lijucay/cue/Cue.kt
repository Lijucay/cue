package de.lijucay.cue

import de.lijucay.cue_read.CueReadManager
import de.lijucay.cue_write.NfcWriteManager

class Cue {
    val writer = NfcWriteManager()
    val reader = CueReadManager()
}