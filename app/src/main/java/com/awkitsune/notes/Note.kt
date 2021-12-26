package com.awkitsune.notes

import java.text.SimpleDateFormat
import java.util.*

class Note (theme: String, content: String) {
    var theme = theme
    var date: String = Util.getDateInString(Calendar.getInstance())
    var content = content
}


