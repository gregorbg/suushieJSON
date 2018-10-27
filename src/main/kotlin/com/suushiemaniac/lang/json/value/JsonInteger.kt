package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON

class JsonInteger(parent: JSON?, value: Number) : JsonNumber(parent, value) {
    constructor(value: Number) : this(null, value)

    override fun toNative(): Any {
        return this.intValue()
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonInteger && other.value.toInt() == this.value.toInt()
    }

    override fun set(value: JSON) {
        if (value is JsonInteger) this.value = value.intValue()
    }
}
