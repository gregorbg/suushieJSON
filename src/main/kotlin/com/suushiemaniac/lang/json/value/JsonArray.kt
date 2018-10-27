package com.suushiemaniac.lang.json.value

import com.suushiemaniac.lang.json.JSON
import com.suushiemaniac.lang.json.exception.JsonValueTypeException
import com.suushiemaniac.lang.json.util.StringUtils.reduceTab
import com.suushiemaniac.lang.json.exception.JsonNotIterableException

class JsonArray(parent: JSON?, private var elements: MutableList<JSON>) : JsonElement(parent) {
    constructor(elements: MutableList<JSON>) : this(null, elements)

    init {
        for (jsonType in elements) jsonType.parent = this
    }

    override fun get(keyIndex: JSON): JSON? {
        return if (keyIndex is JsonNumber)
            this.elements[keyIndex.intValue()]
        else
            throw JsonNotIterableException("Can't get child element by given JSON type. Expected: Number, Found: " + keyIndex.type())
    }

    override fun get(key: String): JSON? {
        try {
            return this.elements[Integer.parseInt(key)]
        } catch (e: NumberFormatException) {
            throw JsonValueTypeException("Can't get child element by non-numeric string key $key")
        }

    }

    override fun get(index: Int): JSON? {
        return this.elements[index]
    }

    override fun set(keyIndex: JSON, value: JSON) {
        value.parent = this
        if (keyIndex is JsonNumber) this.elements[keyIndex.intValue()] = value
    }

    override fun set(key: String, value: JSON) {
        try {
            this.elements[key.toInt()] = value
            value.parent = this
        } catch (e: NumberFormatException) {
            throw JsonValueTypeException("Can't set list element by non-numeric string key $key")
        }

    }

    override fun set(index: Int, value: JSON) {
        value.parent = this
        this.elements[index] = value
    }

    override fun set(value: JSON) {
        if (value is JsonArray) {
            this.elements = value.elements
        }

        for (t in this.elements) {
            t.parent = this
        }
    }

    override fun add(value: JSON) {
        value.parent = this
        this.elements.add(value)
    }

    override fun add(keyIndex: JSON, value: JSON) {
        if (keyIndex is JsonNumber) {
            this.add(keyIndex.intValue(), value)
        } else {
            throw JsonValueTypeException("Can't add child element by given JSON type. Expected: Number, Found: " + keyIndex.type())
        }
    }

    override fun add(key: String, value: JSON) {
        try {
            this.elements.add(key.toInt(), value)
            value.parent = this
        } catch (e: NumberFormatException) {
            throw JsonValueTypeException("Can't add child element by non-numeric string key $key")
        }

    }

    override fun add(index: Int, value: JSON) {
        value.parent = this
        this.elements.add(index, value)
    }

    override fun remove(value: JSON) {
        this.elements.remove(value)
    }

    override fun remove(index: Int) {
        this.elements.removeAt(index)
    }

    override fun remove(key: String) {
        throw JsonValueTypeException("Can't remove child element by string key")
    }

    override fun keyIndexOf(content: JSON): JSON {
        return JsonInteger(this.elements.indexOf(content))
    }

    override fun clear() {
        this.elements.clear()
    }

    override fun size(): Int {
        return this.elements.size
    }

    override fun deepSize(): Int {
        return this.elements.sumBy { it.deepSize() }
    }

    override fun iterator(): Iterator<JSON> {
        return this.elements.iterator()
    }

    override fun toString(): String {
        return this.elements.joinToString(",", prefix = "[", postfix = "]")
    }

    override fun equals(other: Any?): Boolean {
        return other is JsonArray && other.elements == this.elements
    }

    override fun hashCode(): Int {
        return this.elements.hashCode()
    }

    //TODO maybe simplify
    override fun toFormatString(): String {
        val stringedList = this.elements.map { if (this.size() <= 1 || this.depth() <= 1) it.toFormatString().reduceTab() else it.toFormatString() }

        val openTabbing = "\t".repeat(this.hierarchy() - if (this.parent is JsonElement && this.parent!!.size() <= 1) 1 else 0)

        val borderBreak = if (stringedList.size <= 1 || this.depth() <= 1) "" else "\n"
        val closingTabs = if (stringedList.size <= 1 || this.depth() <= 1) "" else openTabbing

        return openTabbing + "[" + borderBreak + stringedList.joinToString(",$borderBreak") + borderBreak + closingTabs + "]"
    }

    override fun toXMLString(): String {
        val stringedList = ArrayList<String>()

        var key = "array"
        val parent = this.parent()

        if (parent is JsonObject) {
            for (parentKey in parent.keySet()) {
                if (parent[parentKey] == this) {
                    key = parentKey.stringValue()
                }
            }
        }

        for (t in this.elements) {
            val xmlVal = t.toXMLString()
            if (xmlVal.isNotEmpty()) stringedList.add("<$key>$xmlVal</$key>")
        }

        return stringedList.joinToString("\n")
    }

    override fun toNative(): Any {
        return this.nativeList()
    }

    override fun nativeKeySet(): Set<String> {
        return this.nativeMap().keys
    }

    override fun collect(): Collection<JSON> {
        return this.elements.toList()
    }

    override fun keySet(): Set<JSON> {
        return this.elements.indices.map { JsonInteger(it) }.toSet()
    }

    override fun <T> nativeMap(mapping: (JSON) -> T): Map<String, T> {
        return this.elements.mapIndexed { idx, json -> idx.toString() to mapping(json) }.toMap()
    }

    override fun <T> nativeList(mapping: (JSON) -> T): List<T> {
        return this.elements.map(mapping).toList()
    }

    override fun <T> nativeSet(mapping: (JSON) -> T): Set<T> {
        return this.elements.map(mapping).toSet()
    }
}