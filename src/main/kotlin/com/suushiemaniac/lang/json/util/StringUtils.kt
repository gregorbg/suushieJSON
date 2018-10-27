package com.suushiemaniac.lang.json.util

object StringUtils {
    private val UNICODE = "\\\\u([0-9a-fA-F]{4})".toRegex()

    fun String.jsonWrap(): String {
        var blank = this.replace("\\", "\\\\").replace("\"", "\\\"")

        if (!blank.startsWith("\"") || !blank.endsWith("\"")) {
            blank = "\"" + blank + "\""
        }

        return blank
    }

    fun String.jsonUnwrap(): String {
        if (this.startsWith("\"") && this.endsWith("\"") && this.length > 1) {
            return this.substring(1, this.length - 1)
        }

        return this
    }

    fun String.reduceTab(): String {
        var tabbed = String(this.toCharArray())

        while (tabbed.startsWith("\t")) {
            tabbed = tabbed.substring(1)
        }

        return tabbed
    }

    fun String.decodeUnicode(): String {
        var decoded = String(this.toCharArray())
        val unicodeMatcher = UNICODE.findAll(decoded)

        for (res in unicodeMatcher) {
            val num = Integer.parseInt(res.groupValues[1], 16)
            decoded = decoded.replace(Regex.escape(res.value).toRegex(), num.toChar().toString())
        }

        return decoded
    }

    fun String.encodeUnicode(): String {
        val b = StringBuilder()

        for (c in this.toCharArray()) {
            if (c.toInt() >= 256) {
                b.append("\\u").append(String.format("%04X", c.toInt()))
            } else {
                b.append(c)
            }
        }

        return b.toString()
    }
}
