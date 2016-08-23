package com.suushiemaniac.lang.json;

import com.suushiemaniac.lang.json.lang.JsonReader;
import com.suushiemaniac.lang.json.lang.antlr.JSONParser;
import com.suushiemaniac.lang.json.value.JsonArray;
import com.suushiemaniac.lang.json.value.JsonElement;
import com.suushiemaniac.lang.json.value.JsonObject;
import com.suushiemaniac.lang.json.value.JsonType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.BooleanSupplier;
import java.util.stream.Collector;
import java.util.stream.Stream;

public abstract class JSON implements Iterable<JSON> {
	private static JsonReader readerInst;

	private static JsonReader readerInst() {
		if (readerInst == null)
			readerInst = new JsonReader();

		return readerInst;
	}

	public static JSON fromString(String json) {
		return readerInst().parse(json);
	}

	public static JSON fromFile(File file) {
		return fromFile(file.toPath());
	}

	public static JSON fromFile(Path path) {
		try {
			return JSON.fromString(new String(Files.readAllBytes(path)));
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static JSON fromNative(Object nativeObj) {
		if (nativeObj instanceof Map || nativeObj instanceof List || nativeObj instanceof Set) {
			return JsonElement.fromNative(nativeObj);
		} else if (nativeObj instanceof Boolean || nativeObj instanceof Float || nativeObj instanceof Integer || nativeObj instanceof Void || nativeObj == null || nativeObj instanceof String) {
			return JsonType.fromNative(nativeObj);
		}

		return null;
	}

	public static JSON emptyArray() {
		return new JsonArray(new ArrayList<>());
	}

	public static JSON emptyObject() {
		return new JsonObject(new TreeMap<>());
	}

    protected JSON parent;

    public JSON(JSON parent) {
        this.parent = parent;
    }

    public JSON parent() {
        return this.parent;
    }

	public void setParent(JSON parent) {
		this.parent = parent;
	}

    public int hierarchy() {
        int hierarchy = 0;
        JSON parent = this.parent();

        while (parent != null) {
            parent = parent.parent();
            hierarchy++;
        }

        return hierarchy;
    }

	public String type() {
		return this.getClass().getSimpleName().replace("Json", "");
	}

	public String val() {
        return this.toString();
    }

    public abstract String toString();

    public abstract boolean equals(Object other);

    public abstract JSON get(JSON keyIndex);

    public abstract JSON get(String key);

    public abstract JSON get(int index);

	public JSON getOrDefault(JSON keyIndex, JSON defVal) {
		JSON gotten = this.get(keyIndex);
		return gotten == null ? defVal : gotten;
	}

	public JSON getOrDefault(String key, JSON defVal) {
		JSON gotten = this.get(key);
		return gotten == null ? defVal : gotten;
	}

	public JSON getOrDefault(int index, JSON defVal) {
		JSON gotten = this.get(index);
		return gotten == null ? defVal : gotten;
	}

	public abstract void set(JSON keyIndex, JSON value);

	public abstract void set(String key, JSON value);

	public abstract void set(int index, JSON value);

	public abstract void set(JSON value);

	public abstract void add(JSON value);

	public abstract void add(JSON keyIndex, JSON value);

	public abstract void add(String key, JSON value);

	public abstract void add(int index, JSON value);

	public abstract void remove(JSON value);

	public abstract void remove(int index);

	public abstract void remove(String key);

	public abstract JSON keyIndexOf(JSON content);

	public abstract void clear();

	public abstract int size();

	public abstract int deepSize();

    public abstract String toFormatString();

    public abstract String toXMLString();

	public abstract boolean booleanValue();

	public abstract int intValue();

	public abstract float floatValue();

	public abstract String stringValue();

	public abstract Object nullValue();

	public abstract Object toNative();

	public abstract Collection<JSON> collect();

	public abstract Stream<JSON> stream();

	public abstract Set<JSON> keySet();

	public abstract Map<String, Object> nativeMap();

	public abstract List<Object> nativeList();

	public abstract Set<Object> nativeSet();
}
