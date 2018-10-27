package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON
import com.suushiemaniac.lang.json.exception.JsonNotIterableException
import com.suushiemaniac.lang.json.exception.JsonValueTypeException
import com.suushiemaniac.lang.json.util.StringUtils.decodeUnicode

abstract class JsonType protected constructor(parent: JSON?) : JSON(parent) {
    inner class SingleIterator<T>(private val toRead: T) : Iterator<T> {
        private var hasRead: Boolean = false

        override fun hasNext(): Boolean {
            return !hasRead
        }

        override fun next(): T {
            hasRead = true
            return toRead
        }
    }

    override fun get(keyIndex: JSON): JSON? {
        throw JsonNotIterableException("Can't get child elements from " + this.type())
    }

    override fun get(key: String): JSON? {
        throw JsonNotIterableException("Can't get child elements from " + this.type())
    }

    override fun get(index: Int): JSON? {
        throw JsonNotIterableException("Can't get child elements from " + this.type())
    }

    override fun set(keyIndex: JSON, value: JSON) {
        throw JsonNotIterableException("Can't set type " + this.type() + " on key/index-based access")
    }

    override fun set(key: String, value: JSON) {
        throw JsonNotIterableException("Can't set type " + this.type() + " on key-based access")
    }

    override fun set(index: Int, value: JSON) {
        throw JsonNotIterableException("Can't set type " + this.type() + " on index-based access")
    }

    override fun set(value: JSON) {
        throw JsonNotIterableException("Invalid type for setting. Expected: " + this.type() + ", Found: " + value.type())
    }

    override fun add(value: JSON) {
        throw JsonNotIterableException("Can't add to single value type " + this.type())
    }

    override fun add(keyIndex: JSON, value: JSON) {
        throw JsonNotIterableException("Can't add to single value type " + this.type())
    }

    override fun add(key: String, value: JSON) {
        throw JsonNotIterableException("Can't add to single value type " + this.type())
    }

    override fun add(index: Int, value: JSON) {
        throw JsonNotIterableException("Can't add to single value type " + this.type())
    }

    override fun remove(value: JSON) {
        throw JsonNotIterableException("Can't remove from single value type " + this.type())
    }

    override fun remove(index: Int) {
        throw JsonNotIterableException("Can't remove from single value type " + this.type())
    }

    override fun remove(key: String) {
        throw JsonNotIterableException("Can't remove from single value type " + this.type())
    }

    override fun keyIndexOf(content: JSON): JSON {
        throw JsonNotIterableException("Single value types don't have any keys/indices")
    }

    override fun clear() {
        throw JsonNotIterableException("Can't clear single value types")
    }

    override fun size(): Int {
        return 1
    }

    override fun depth(): Int {
        return 0
    }

    override fun deepSize(): Int {
        return 1
    }

    override fun toFormatString(): String {
        return "\t".repeat(this.hierarchy()) + this.toString()
    }

    override fun toXMLString(): String {
        return this.toString()
    }

    override fun booleanValue(): Boolean {
        throw JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Boolean")
    }

    override fun intValue(): Int {
        throw JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Integer")
    }

    override fun floatValue(): Float {
        throw JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Float")
    }

    override fun stringValue(): String {
        throw JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: String")
    }

    override fun decodedStringValue(): String {
        return this.stringValue().decodeUnicode()
    }

    override fun nullValue(): Any {
        throw JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Null")
    }

    override fun collect(): Collection<JSON> {
        return setOf<JSON>(this)
    }

    override fun keySet(): Set<JSON> {
        return emptySet()
    }

    override fun nativeKeySet(): Set<String> {
        return emptySet()
    }

    override fun nativeMap(): Map<String, Any> {
        throw JsonNotIterableException()
    }

    override fun <T> nativeMap(mapping: (JSON) -> T): Map<String, T> {
        throw JsonNotIterableException()
    }

    override fun nativeList(): List<Any> {
        return listOf<Any>(this)
    }

    override fun <T> nativeList(mapping: (JSON) -> T): List<T> {
        return listOf(mapping(this))
    }

    override fun nativeSet(): Set<Any> {
        return setOf<Any>(this)
    }

    override fun <T> nativeSet(mapping: (JSON) -> T): Set<T> {
        return setOf(mapping(this))
    }

    override fun iterator(): Iterator<JSON> {
        return SingleIterator<JSON>(this)
    }

    companion object {
        fun fromNative(nativeObj: Any?): JSON? {
            return when (nativeObj) {
                is Boolean -> JsonBoolean(nativeObj)
                is Float -> JsonFloat(nativeObj)
                is Double -> JsonFloat(nativeObj)
                is Int -> JsonInteger(nativeObj)
                is Long -> JsonInteger(nativeObj)
                is Short -> JsonInteger(nativeObj)
                is Byte -> JsonInteger(nativeObj)
                is String -> JsonString(nativeObj)
                null -> JsonNull.INST
                else -> null
            }
        }
    }
}
