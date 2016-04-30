package com.suushiemaniac.util.json.value;

import com.suushiemaniac.util.json.StringUtils;
import com.suushiemaniac.util.json.exception.JsonNotIterableException;

import java.util.*;
import java.util.stream.Collectors;

public class JsonObject extends JsonElement {
    private Map<String, JSONType> members;

    public JsonObject(Map<String, JSONType> members) {
        this(null, members);
    }

    public JsonObject(JSONType parent, Map<String, JSONType> members) {
        super(parent);

        for (Map.Entry<String, JSONType> jsonTypeEntry : members.entrySet()) jsonTypeEntry.getValue().setParent(this);

        this.members = members;
    }

    @Override
    public JSONType get(JSONType keyIndex) {
        if (keyIndex instanceof JsonString) return this.members.get(keyIndex.stringValue());
        else throw new JsonNotIterableException();
    }

    @Override
    public JSONType get(String key) {
        return this.members.get(StringUtils.jsonWrap(key));
    }

    @Override
    public void set(JSONType keyIndex, JSONType value) {
        if (keyIndex instanceof JsonString) {
            value.setParent(this);
            this.members.put(keyIndex.stringValue(), value);
        }
    }

    @Override
    public void set(String key, JSONType value) {
        value.setParent(this);
        this.members.put(StringUtils.jsonWrap(key), value);
    }

    @Override
    public void set(JSONType value) {
        if (value instanceof JsonObject) this.members = ((JsonObject) value).members;

        for (Map.Entry<String, JSONType> tEntry : this.members.entrySet()) tEntry.getValue().setParent(this);
    }

    @Override
    public void add(JSONType keyIndex, JSONType value) {
        if (keyIndex instanceof JsonString) {
            value.setParent(this);
            this.members.put(keyIndex.stringValue(), value);
        }
    }

    @Override
    public void add(String key, JSONType value) {
        value.setParent(this);
        this.members.put(StringUtils.jsonWrap(key), value);
    }

    @Override
    public int size() {
        return this.members.size();
    }

    @Override
    public int deepSize() {
        int deepSize = 0;

        for (Map.Entry<String, JSONType> tEntry : this.members.entrySet()) {
            deepSize += tEntry.getValue().deepSize();
        }

        return deepSize;
    }

    @Override
    public Iterator<JSONType> iterator() {
        List<JSONType> typeList = this.members.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

        return typeList.iterator();
    }

    public String toString() {
        List<String> stringedList = new ArrayList<>();
        List<String> sortedKeys = new ArrayList<>(this.members.keySet());
        Collections.sort(sortedKeys, String::compareTo);

        for (String key : sortedKeys) {
            stringedList.add(key + ":" + this.members.get(key).toString());
        }

        return "{" + String.join(",", stringedList) + "}";
    }

    @Override
    public String toFormatString() {
        List<String> stringedList = new ArrayList<>();
        List<String> sortedKeys = new ArrayList<>(this.members.keySet());
        Collections.sort(sortedKeys, String::compareTo);

        String tabbing = this.members.size() == 0 || (this.members.size() == 1 && this.getOnly().deepSize() == 1) ? "" : StringUtils.copy("\t", this.hierarchy() + 1);

        for (String key : sortedKeys) {
            JSONType val = this.members.get(key);
            stringedList.add(tabbing + key + " : " + StringUtils.reduceTab(val.toFormatString()));
        }

        String borderBreak = stringedList.size() == 0 ? "" : (stringedList.size() == 1 && this.getOnly().deepSize() == 1 ? " " : "\n");
        String closingTabs = stringedList.size() == 0 || (stringedList.size() == 1 && this.getOnly().deepSize() == 1) ? "" : StringUtils.copy("\t", this.hierarchy());
        return StringUtils.copy("\t", this.hierarchy()) + "{" + borderBreak + String.join(",\n", stringedList) + borderBreak + closingTabs + "}";
    }

    private JSONType getOnly() {
        if (this.members.size() > 1) return new JsonNull();
        else return new ArrayList<>(this.members.entrySet()).get(0).getValue();
    }
}