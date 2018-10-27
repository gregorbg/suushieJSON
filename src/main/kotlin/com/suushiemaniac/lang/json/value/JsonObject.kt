package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON
import com.suushiemaniac.lang.json.util.StringUtils.jsonWrap
import com.suushiemaniac.lang.json.util.StringUtils.jsonUnwrap
import com.suushiemaniac.lang.json.util.StringUtils.reduceTab
import com.suushiemaniac.lang.json.exception.JsonNotIterableException

import java.util.*

class JsonObject(parent: JSON?, private var members: SortedMap<String, JSON>) : JsonElement(parent) {
    constructor(members: SortedMap<String, JSON>) : this(null, members)

    init {
        for (value in members.values) value.parent = this
    }

    override fun get(keyIndex: JSON): JSON? {
        try {
            return this.members[keyIndex.stringValue()]
        } catch (e: JsonNotIterableException) {
            throw JsonNotIterableException("Can't get child element by given JSON type. Expected: String, Found: " + keyIndex.type())
        }

    }

    override fun get(key: String): JSON? {
        return this.members[key]
    }

    override fun get(index: Int): JSON? {
        return this.members[index.toString()]
    }

    override fun set(keyIndex: JSON, value: JSON) {
        if (keyIndex is JsonString) {
            value.parent = this
            this.members[keyIndex.stringValue()] = value
        }
    }

    override fun set(key: String, value: JSON) {
        value.parent = this
        this.members[key] = value
    }

    override fun set(index: Int, value: JSON) {
        value.parent = this
        this.members[index.toString()] = value
    }

    override fun set(value: JSON) {
        if (value is JsonObject) this.members = value.members

        for (memberVal in this.members.values)
            memberVal.parent = this
    }

    override fun add(value: JSON) {
        throw JsonNotIterableException("Can't add child element without explicit key")
    }

    override fun add(keyIndex: JSON, value: JSON) {
        this[keyIndex] = value
    }

    override fun add(key: String, value: JSON) {
        this[key] = value
    }

    override fun add(index: Int, value: JSON) {
        this[index.toString()] = value
    }

    override fun remove(key: String) {
        this.members.remove(key)
    }

    override fun remove(value: JSON) {
        this.members.remove(value.stringValue())
    }

    override fun remove(index: Int) {
        this.members.remove(index.toString())
    }

    override fun keyIndexOf(content: JSON): JSON {
        for ((key, value) in this.members) {
            if (value == content) {
                return JsonString(key)
            }
        }

        return JsonNull.INST
    }

    override fun clear() {
        this.members.clear()
    }

    override fun size(): Int {
        return this.members.size
    }

    override fun deepSize(): Int {
        return this.members.values.sumBy { it.deepSize() }
    }

    override fun iterator(): Iterator<JSON> {
        return this.members.values.iterator()
    }

    override fun toString(): String {
        return this.members.map { "${it.key.jsonWrap()}:${it.value}" }.joinToString(",", prefix = "{", postfix = "}")
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonObject && other.members == this.members
    }

    override fun hashCode(): Int {
        return this.members.hashCode()
    }

    //TODO maybe simplify
    override fun toFormatString(): String {
        val tabbing = if (this.members.size <= 1) "" else "\t".repeat(this.hierarchy() + 1)

        val stringedList = this.members.map { "$tabbing${it.key.jsonWrap()}: ${it.value.toFormatString().reduceTab()}" }

        val openTabbing = "\t".repeat(this.hierarchy() - if (this.parent is JsonElement && this.parent!!.size() <= 1) 1 else 0)

        val borderBreak = if (stringedList.size <= 1) "" else "\n"
        val closingTabs = if (stringedList.size <= 1) "" else openTabbing

        return openTabbing + "{" + borderBreak + stringedList.joinToString(",\n") + borderBreak + closingTabs + "}"
    }

    //TODO definitely simplify
    override fun toXMLString(): String {
        val stringedList = ArrayList<String>()

        for ((key, child) in this.members) {
            var tagKey = key.jsonUnwrap()

            var openTag = "<$tagKey>"
            var closeTag = "</$tagKey>"

            if (child is JsonObject) {
                val properties = ArrayList<String>()

                for ((childKey, childVal) in child.members) {
                    if (childVal is JsonElement) continue

                    properties.add("$childKey=${childVal.toString().jsonWrap()}")
                    child.remove(childKey)
                }

                tagKey += properties.joinToString(" ", prefix = " ")
            }

            if (child is JsonArray && child.size() > 0) {
                closeTag = ""
                openTag = closeTag
            }

            val xmlVal = child.toXMLString()
            stringedList.add(if (xmlVal.isNotEmpty()) "$openTag$xmlVal$closeTag" else "<$tagKey/>")
        }

        return stringedList.joinToString("\n")
    }

    override fun toNative(): Any {
        return this.nativeMap()
    }

    override fun nativeKeySet(): Set<String> {
        return this.members.keys
    }

    override fun collect(): Collection<JSON> {
        return this.members.values.toSet()
    }

    override fun keySet(): Set<JSON> {
        return this.members.keys.map { JsonString(it) }.toSet()
    }

    override fun <T> nativeMap(mapping: (JSON) -> T): Map<String, T> {
        return this.members.mapValues { mapping(it.value) }
    }

    override fun <T> nativeList(mapping: (JSON) -> T): List<T> {
        return this.nativeMap(mapping).values.toList()
    }

    override fun <T> nativeSet(mapping: (JSON) -> T): Set<T> {
        return this.nativeMap(mapping).values.toSet()
    }
}