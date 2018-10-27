package com.suushiemaniac.lang.json.util

object MapUtils {
    fun <K, V> Map<K, V?>.denullify(): Map<K, V> {
        return this.mapNotNull { (key, nullableValue) ->
            nullableValue?.let { key to it }
        }.toMap()
    }
}