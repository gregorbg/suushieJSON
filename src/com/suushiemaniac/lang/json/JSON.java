package com.suushiemaniac.lang.json;

import com.suushiemaniac.lang.json.lang.JsonReader;
import com.suushiemaniac.lang.json.value.JSONType;
import com.suushiemaniac.lang.json.value.JsonElement;

import java.util.Collection;
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

    @Override
    public JSONType getOrDefault(int index, JSONType defVal) {
        return this.root.getOrDefault(index, defVal);
    }

    @Override
    public JSONType getOrDefault(String key, JSONType defVal) {
        return this.root.getOrDefault(key, defVal);
    }

    @Override
    public JSONType getOrDefualt(JSONType keyIndex, JSONType defVal) {
        return this.root.getOrDefualt(keyIndex, defVal);
    }

    @Override
    public void set(JSONType value) {
        this.root.set(value);
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
    public void clear() {
        this.root.clear();
    }

    @Override
    public Collection<JSONType> collect() {
        return this.root.collect();
    }

    @Override
    public int hierarchy() {
        return this.root.hierarchy();
    }

    @Override
    public String val() {
        return this.root.val();
    }

    @Override
    public void remove(int index) {
        this.root.remove(index);
    }

    @Override
    public void remove(String key) {
        this.root.remove(key);
    }

    @Override
    public void remove(JSONType value) {
        this.root.remove(value);
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

    @Override
    public String toXMLString() {
        return this.root.toXMLString();
    }
}
