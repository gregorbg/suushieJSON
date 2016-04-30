package com.suushiemaniac.util.json.value;

import com.suushiemaniac.util.json.StringUtils;
import com.suushiemaniac.util.json.exception.JsonNotIterableException;
import com.suushiemaniac.util.json.exception.JsonValueTypeException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    public String toFormatString() {
        List<String> stringedList = new ArrayList<>();

        for (JSONType t : this.elements) {
            stringedList.add(this.elements.size() > 1 ? t.toFormatString() : StringUtils.reduceTab(t.toFormatString()));
        }

        String borderBreak = stringedList.size() == 0 ? "" : (stringedList.size() == 1 && this.elements.get(0).deepSize() == 1 ? " " : "\n");
        String closingTabs = stringedList.size() == 0 || (stringedList.size() == 1 && this.elements.get(0).deepSize() == 1) ? "" : StringUtils.copy("\t", this.hierarchy());
        return StringUtils.copy("\t", this.hierarchy()) + "[" + borderBreak + String.join(",\n", stringedList) + borderBreak + closingTabs + "]";
    }
}
