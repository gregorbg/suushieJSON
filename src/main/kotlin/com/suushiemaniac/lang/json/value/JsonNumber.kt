package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON

abstract class JsonNumber(parent: JSON?, protected var value: Number) : JsonType(parent) {
    override fun floatValue(): Float {
        return this.value.toFloat()
    }

    override fun intValue(): Int {
        return this.value.toInt()
    }

    override fun stringValue(): String {
        return this.toString()
    }

    override fun toString(): String {
        return this.toNative().toString()
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }
}
