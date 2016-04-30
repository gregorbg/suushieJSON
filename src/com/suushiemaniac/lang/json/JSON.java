package com.suushiemaniac.lang.json;

import com.suushiemaniac.lang.json.lang.JsonReader;
import com.suushiemaniac.lang.json.value.JSONType;
import com.suushiemaniac.lang.json.value.JsonElement;

import java.util.Iterator;

public class JSON extends JSONType {
    private static JsonReader readerInst;

    private static JsonReader readerInst() {
        if (readerInst == null) {
            readerInst = new JsonReader();
        }

        return readerInst;
    }

    public static JSON fromJSONString(String json) {
        return readerInst().parse(json);
    }

    private JsonElement root;

    public JSON(JsonElement root) {
        super(null);
        this.root = root;
    }

    public JSONType get(JSONType indexKey) {
        return this.root.get(indexKey);
    }

    public JSONType get(int index) {
        return this.root.get(index);
    }

    public JSONType get(String key) {
        return this.root.get(key);
    }

    public void set(JSONType keyIndex, JSONType value) {
        this.root.set(keyIndex, value);
    }

    public void set(String key, JSONType value) {
        this.root.set(key, value);
    }

    public void set(int index, JSONType value) {
        this.root.set(index, value);
    }

    public void add(JSONType value) {
        this.root.add(value);
    }

    public void add(JSONType keyIndex, JSONType value) {
        this.root.add(keyIndex, value);
    }

    public void add(String key, JSONType value) {
        this.root.add(key, value);
    }

    @Override
    public int size() {
        return this.root.size();
    }

    @Override
    public int deepSize() {
        return this.root.deepSize();
    }

    @Override
    public Iterator<JSONType> iterator() {
        return this.root.iterator();
    }

    public String toString() {
        return this.root.toString();
    }

    public String toFormatString() {
        return this.root.toFormatString();
    }
}
