package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON

class JsonNull private constructor(parent: JSON? = null) : JsonType(parent) {
    override fun nullValue(): Any {
        return Unit
    }

    override fun toNative(): Any {
        return this.nullValue()
    }

    override fun stringValue(): String {
        return "null"
    }

    override fun floatValue(): Float {
        return 0f
    }

    override fun intValue(): Int {
        return 0
    }

    override fun booleanValue(): Boolean {
        return false
    }

    override fun toString(): String {
        return "null"
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonNull || other == null
    }

    override fun hashCode(): Int {
        return 0
    }

    companion object {
        val INST = JsonNull()
    }
}
