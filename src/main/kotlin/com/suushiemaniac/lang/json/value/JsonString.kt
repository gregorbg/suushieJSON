package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON
import com.suushiemaniac.lang.json.util.StringUtils.jsonUnwrap
import com.suushiemaniac.lang.json.util.StringUtils.jsonWrap

class JsonString(parent: JSON?, var value: String) : JsonType(parent) {
    constructor(value: String) : this(null, value)

    init {
        this.value = value.jsonUnwrap()
    }

    override fun stringValue(): String {
        return this.value
    }

    override fun toNative(): Any {
        return this.stringValue()
    }

    override fun booleanValue(): Boolean {
        return this.value.isNotEmpty()
    }

    override fun intValue(): Int {
        return try {
            this.stringValue().toInt()
        } catch (e: NumberFormatException) {
            super.intValue()
        }

    }

    override fun floatValue(): Float {
        return try {
            this.value.toFloat()
        } catch (e: NumberFormatException) {
            super.floatValue()
        }
    }

    override fun toString(): String {
        return this.value.jsonWrap()
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonString && other.value == this.value || other is String && other == this.value
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

    override fun set(value: JSON) {
        if (value is JsonString) this.value = value.stringValue()
    }
}
