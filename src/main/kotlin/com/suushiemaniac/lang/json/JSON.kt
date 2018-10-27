package com.suushiemaniac.lang.json

import com.suushiemaniac.lang.json.lang.JsonReader
import com.suushiemaniac.lang.json.value.JsonArray
import com.suushiemaniac.lang.json.value.JsonElement
import com.suushiemaniac.lang.json.value.JsonObject
import com.suushiemaniac.lang.json.value.JsonType

import java.io.*
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

abstract class JSON(var parent: JSON?) : Iterable<JSON> {
    fun parent(): JSON? {
        return this.parent
    }

    fun hierarchy(): Int {
        var hierarchy = 0
        var parent = this.parent

        while (parent != null) {
            parent = parent.parent
            hierarchy++
        }

        return hierarchy
    }

    fun type(): String {
        return this.javaClass.simpleName.replace("Json", "")
    }

    fun value(): String {
        return this.toString()
    }

    abstract override fun toString(): String

    abstract override fun equals(other: Any?): Boolean

    abstract override fun hashCode(): Int

    abstract operator fun get(keyIndex: JSON): JSON?

    abstract operator fun get(key: String): JSON?

    abstract operator fun get(index: Int): JSON?

    fun getOrElse(keyIndex: JSON, defVal: JSON): JSON {
        val gotten = this[keyIndex]
        return gotten ?: defVal
    }

    fun getOrElse(key: String, defVal: JSON): JSON {
        val gotten = this[key]
        return gotten ?: defVal
    }

    fun getOrElse(index: Int, defVal: JSON): JSON {
        val gotten = this[index]
        return gotten ?: defVal
    }

    abstract operator fun set(keyIndex: JSON, value: JSON)

    abstract operator fun set(key: String, value: JSON)

    abstract operator fun set(index: Int, value: JSON)

    abstract fun set(value: JSON)

    abstract fun add(value: JSON)

    abstract fun add(keyIndex: JSON, value: JSON)

    abstract fun add(key: String, value: JSON)

    abstract fun add(index: Int, value: JSON)

    abstract fun remove(value: JSON)

    abstract fun remove(index: Int)

    abstract fun remove(key: String)

    abstract fun keyIndexOf(content: JSON): JSON

    abstract fun clear()

    abstract fun size(): Int

    abstract fun depth(): Int

    abstract fun deepSize(): Int

    abstract fun toFormatString(): String

    abstract fun toXMLString(): String

    abstract fun booleanValue(): Boolean

    abstract fun intValue(): Int

    abstract fun floatValue(): Float

    abstract fun stringValue(): String

    abstract fun decodedStringValue(): String

    abstract fun nullValue(): Any

    abstract fun collect(): Collection<JSON>

    abstract fun keySet(): Set<JSON>

    abstract fun toNative(): Any

    fun <T> toNative(mapping: (JSON) -> T): T {
        return mapping(this)
    }

    abstract fun nativeKeySet(): Set<String>

    abstract fun nativeMap(): Map<String, Any>

    abstract fun <T> nativeMap(mapping: (JSON) -> T): Map<String, T>

    abstract fun nativeList(): List<Any>

    abstract fun <T> nativeList(mapping: (JSON) -> T): List<T>

    abstract fun nativeSet(): Set<Any>

    abstract fun <T> nativeSet(mapping: (JSON) -> T): Set<T>

    companion object {
        private var READER_INST: JsonReader = JsonReader()

        fun fromString(json: String): JSON {
            return READER_INST.parse(json)
        }

        fun fromURL(url: URL): JSON? {
            try {
                url.openStream().use { `is` -> return JSON.fromStream(`is`) }
            } catch (e: IOException) {
                e.printStackTrace()
                return null
            }

        }

        fun fromStream(inStream: InputStream): JSON? {
            return try {
                val lines = BufferedReader(InputStreamReader(inStream)).readLines()
                JSON.fromString(lines.joinToString("\n"))
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }

        }

        fun fromFile(file: File): JSON? {
            return JSON.fromFile(file.toPath())
        }

        fun fromFile(path: Path): JSON? {
            return try {
                JSON.fromString(String(Files.readAllBytes(path)))
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }

        }

        fun fromNative(nativeObj: Any?): JSON? {
            if (nativeObj is JSON) {
                return nativeObj
            } else if (nativeObj is Boolean || nativeObj is Float || nativeObj is Double || nativeObj is Int || nativeObj is Long || nativeObj is Short || nativeObj is Byte || nativeObj is String || nativeObj == null) {
                return JsonType.fromNative(nativeObj)
            } else if (nativeObj is Map<*, *> || nativeObj is List<*> || nativeObj is Set<*> || nativeObj is Array<*>) {
                return JsonElement.fromNative(nativeObj)
            }

            return null
        }

        fun emptyArray(): JSON {
            return JsonArray(mutableListOf())
        }

        fun emptyObject(): JSON {
            return JsonObject(sortedMapOf())
        }

        fun toFile(json: JSON, filePath: String, fileName: String): Boolean {
            return toFile(json, File(File(filePath), fileName))
        }

        fun toFile(json: JSON, file: File): Boolean {
            if (!file.exists()) {
                try {
                    val created = file.createNewFile()

                    if (!created) {
                        return false
                    }
                } catch (e: IOException) {
                    return false
                }
            }

            try {
                FileWriter(file, false).use { it.write(json.toFormatString()) }
            } catch (e: IOException) {
                return false
            }

            return true
        }

        fun serialize(obj: Any): JSON? {
            val properties = HashMap<String, Any>()

            for (field in obj.javaClass.fields) {
                try {
                    properties[field.name] = field.get(obj)
                } catch (ignored: IllegalAccessException) {}
            }

            return JSON.fromNative(properties)
        }
    }
}
