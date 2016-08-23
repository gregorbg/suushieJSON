package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.exception.JsonValueTypeException;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Stream;

public abstract class JsonElement extends JSON {
	public static JSON fromNative(Object nativeObj) {
		if (nativeObj instanceof Map) {
			Map nativeMap = (Map) nativeObj;
			SortedMap<String, JSON> jsonMap = new TreeMap<>();

			for (Object key : nativeMap.keySet()) {
				Object element = nativeMap.get(key);
				jsonMap.put(key.toString(), JSON.fromNative(element));
			}

			return new JsonObject(jsonMap);
		} else if (nativeObj instanceof List || nativeObj instanceof Set) {
			Collection nativeCollection = (Collection) nativeObj;
			List<JSON> jsonList = new ArrayList<>();

			for (Object element : nativeCollection) {
				jsonList.add(JSON.fromNative(element));
			}

			return new JsonArray(jsonList);
		}

		return null;
	}

    protected JsonElement(JSON parent) {
        super(parent);
    }

    @Override
    public Stream<JSON> stream() {
        return this.collect().stream();
    }

    public boolean booleanValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Boolean");
    }

    public int intValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Integer");
    }

    public float floatValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Float");
    }

    public String stringValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type String");
    }

    public Object nullValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Null");
    }
}
