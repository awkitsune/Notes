package com.awkitsune.notes

import java.util.*

class Util {
    companion object{
        fun getDateInString(date: Calendar): String {
            return "${date.get(Calendar.DAY_OF_MONTH)}." +
                    "${date.get(Calendar.MONTH)}." +
                    "${date.get(Calendar.YEAR)}"
        }
    }
}