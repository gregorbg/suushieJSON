package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON

class JsonBoolean(parent: JSON?, private var value: Boolean) : JsonType(parent) {
    constructor(value: Boolean) : this(null, value)

    override fun booleanValue(): Boolean {
        return this.value
    }

    override fun stringValue(): String {
        return "" + this.value
    }

    override fun toNative(): Any {
        return this.booleanValue()
    }

    override fun intValue(): Int {
        return if (this.value) 1 else 0
    }

    override fun floatValue(): Float {
        return if (this.value) 1f else 0f
    }

    override fun toString(): String {
        return this.value.toString()
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonBoolean && other.value == this.value
    }

    override fun hashCode(): Int {
        return this.value.hashCode()
    }

    override fun set(value: JSON) {
        if (value is JsonBoolean) this.value = value.booleanValue()
    }
}