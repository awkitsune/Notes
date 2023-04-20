package com.awkitsune.notes

import java.text.SimpleDateFormat
import java.util.*

class Note (theme: String, content: String, sig: String) {
    var theme = theme
    var date = Util.getDateInString(Calendar.getInstance())
    var content = content
    var figment = sig
}


