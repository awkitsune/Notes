package com.awkitsune.notes

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity
class Note (theme: String, content: String) {
    @PrimaryKey(autoGenerate = true)
    var id = 0
    var theme = theme
    var date: String = Util.getDateInString(Calendar.getInstance())
    var content = content

    constructor(theme: String, content: String, date: String)
            : this(theme = theme, content = content){
        this.date = date
    }
}


