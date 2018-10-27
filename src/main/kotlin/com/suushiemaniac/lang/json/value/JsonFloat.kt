package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON

class JsonFloat(parent: JSON?, value: Number) : JsonNumber(parent, value) {
    constructor(value: Number) : this(null, value)

    override fun toNative(): Any {
        return this.floatValue()
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonFloat && other.value.toFloat() == this.value.toFloat()
    }

    override fun set(value: JSON) {
        if (value is JsonFloat) this.value = value.floatValue()
    }
}
