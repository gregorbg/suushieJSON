package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.util.StringUtils;
import com.suushiemaniac.lang.json.exception.JsonNotIterableException;

import java.util.*;

public class JsonArray extends JsonElement {
    private List<JSONType> elements;

    public JsonArray(List<JSONType> elements) {
        this(null, elements);
    }

    public JsonArray(JSONType parent, List<JSONType> elements) {
        super(parent);

        for (JSONType jsonType : elements) jsonType.setParent(this);

        this.elements = elements;
    }

    @Override
    public JSONType get(JSONType keyIndex) {
        if (keyIndex instanceof JsonNumber) return this.elements.get(keyIndex.intValue());
        else throw new JsonNotIterableException();
    }

    @Override
    public JSONType get(int index) {
        return this.elements.get(index);
    }

    @Override
    public void set(JSONType keyIndex, JSONType value) {
        value.setParent(this);
        if (keyIndex instanceof JsonNumber) this.elements.set(keyIndex.intValue(), value);
    }

    @Override
    public void set(int index, JSONType value) {
        value.setParent(this);
        this.elements.set(index, value);
    }

    @Override
    public void set(JSONType value) {
        if (value instanceof JsonArray) this.elements = ((JsonArray) value).elements;

        for (JSONType t : this.elements) t.setParent(this);
    }

    @Override
    public void add(JSONType value) {
        value.setParent(this);
        this.elements.add(value);
    }

    @Override
    public void remove(JSONType value) {
        this.elements.remove(value);
    }

    @Override
    public void remove(int index) {
        this.elements.remove(index);
    }

    @Override
    public JSONType keyIndexOf(JSONType content) {
        return new JsonInteger(this.elements.indexOf(content));
    }

    @Override
    public void clear() {
        this.elements.clear();
    }

    @Override
    public int size() {
        return this.elements.size();
    }

    @Override
    public int deepSize() {
        int deepSize = 0;

        for (JSONType t : this.elements) {
            deepSize += t.deepSize();
        }

        return deepSize;
    }

    @Override
    public Iterator<JSONType> iterator() {
        return this.elements.iterator();
    }

    public String toString() {
        List<String> stringedList = new ArrayList<>();

        for (JSONType t : this.elements) {
            stringedList.add(t.toString());
        }

        return "[" + String.join(",", stringedList) + "]";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonArray && ((JsonArray) other).elements.equals(this.elements);
    }

    //TODO maybe simplify
    @Override
    public String toFormatString() {
        List<String> stringedList = new ArrayList<>();

        for (JSONType t : this.elements) {
            stringedList.add(this.size() <= 1 ? StringUtils.reduceTab(t.toFormatString()) : t.toFormatString());
        }

        String openTabbing = StringUtils.copy("\t", this.hierarchy() - (this.parent() instanceof JsonElement && this.parent().size() <= 1 ? 1 : 0));
        String borderBreak = stringedList.size() <= 1 ? "" : "\n";
        String closingTabs = stringedList.size() <= 1 ? "" : openTabbing;
        return openTabbing + "[" + borderBreak + String.join(",\n", stringedList) + borderBreak + closingTabs + "]";
    }

    @Override
    public String toXMLString() {
        List<String> stringedList = new ArrayList<>();

        String key = "array";
        JSONType parent = this.parent();
        if (parent instanceof JsonObject) {
            for (JSONType parentKey : parent.keySet()) {
                if (parent.get(parentKey).equals(this)) {
                    key = parentKey.stringValue();
                }
            }
        }

        for (JSONType t : this.elements) {
            String openTag = "<" + key + ">";
            String closeTag = "</" + key + ">";

            String val = t.toXMLString();
            if (val.length() > 0) stringedList.add(openTag + val + closeTag);
        }

        return String.join("\n", stringedList);
    }

    @Override
    public Collection<JSONType> collect() {
        return new ArrayList<>(this.elements);
    }
}
