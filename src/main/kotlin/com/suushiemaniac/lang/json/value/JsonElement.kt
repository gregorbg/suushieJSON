package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON
import com.suushiemaniac.lang.json.exception.JsonValueTypeException
import com.suushiemaniac.lang.json.util.MapUtils.denullify

abstract class JsonElement protected constructor(parent: JSON?) : JSON(parent) {
    override fun depth(): Int {
        var maxDepth = 0

        for (j in this.collect()) {
            val currentDepth = j.depth() + 1

            if (currentDepth > maxDepth)
                maxDepth = currentDepth
        }

        return maxDepth
    }

    override fun booleanValue(): Boolean {
        throw JsonValueTypeException("Can't access element classes as primitive type Boolean")
    }

    override fun intValue(): Int {
        throw JsonValueTypeException("Can't access element classes as primitive type Integer")
    }

    override fun floatValue(): Float {
        throw JsonValueTypeException("Can't access element classes as primitive type Float")
    }

    override fun stringValue(): String {
        throw JsonValueTypeException("Can't access element classes as primitive type String")
    }

    override fun decodedStringValue(): String {
        throw JsonValueTypeException("Can't access element classes as primitive type String")
    }

    override fun nullValue(): Any {
        throw JsonValueTypeException("Can't access element classes as primitive type Null")
    }

    override fun nativeMap(): Map<String, Any> {
        return this.nativeMap { it.toNative() }
    }

    override fun nativeList(): List<Any> {
        return this.nativeList { it.toNative() }
    }

    override fun nativeSet(): Set<Any> {
        return this.nativeSet { it.toNative() }
    }

    companion object {
        fun fromNative(nativeObj: Any?): JSON? {
            return when (nativeObj) {
                is Map<*, *> -> {
                    val jsonMap = nativeObj.map { it.key.toString() to JSON.fromNative(it.value) }.toMap().denullify()
                    JsonObject(jsonMap.toSortedMap())
                }
                is Iterable<*> -> {
                    val jsonList = nativeObj.mapNotNull { JSON.fromNative(it) }
                    JsonArray(jsonList.toMutableList())
                }
                is Array<*> -> JsonElement.fromNative(nativeObj.toList())
                else -> null
            }
        }
    }
}
