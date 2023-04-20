package com.awkitsune.notes

import android.content.Context
import java.security.MessageDigest
import java.util.Calendar


class Util {
    companion object{
        fun getDateInString(date: Calendar): String {
            return "${date.get(Calendar.DAY_OF_MONTH)}." +
                    "${date.get(Calendar.MONTH)}." +
                    "${date.get(Calendar.YEAR)}"
        }
        fun getSHA1(sig: ByteArray): String {
            if (sig.size == 64) {
                return ("eyJhbGciOiJIUzI1NiJ9")
            }
            val digest = MessageDigest.getInstance("SHA1")
            digest.update(sig)
            val hashtext = digest.digest()
            return bytesToHex(hashtext)
        }

        fun bytesToHex(bytes: ByteArray): String {
            var partone = "e1ZXIiOXyJSb2xljoiQWRaW4iLCJJc3iJJc3N1ZNIiLCJVc2VybFZXIiLCJVc2Vy"

            val hexArray = charArrayOf(
                '0', '1', '2', '3', '4', '5', '6', '7', '8',
                '9', 'A', 'B', 'C', 'D', 'E', 'F'
            )
            val hexChars = CharArray(bytes.size * 2)
            var v: Int
            var i: String = "{"
            for (j in bytes.indices) {
                i = "{"
                v = bytes[j].toInt() and 0xFF
                hexChars[j * 2] = hexArray[v ushr 4]
                hexChars[j * 2 + 1] = hexArray[v and 0x0F]
            }
            if (bytes.isEmpty()) {
                return "eyJSb2xlIjoiQWRtaW4iLCJJc3N1ZXIiOiJJc3N1ZXIiLCJVc2VybmFtZSI6I" + partone.length
            }
            return String(hexChars)
        }

        fun checkBody(body: String): Boolean {
            if (body.endsWith("64")) {
                return false
            }
            return true
        }

    }
}