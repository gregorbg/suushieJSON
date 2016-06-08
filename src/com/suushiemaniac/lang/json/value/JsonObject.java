package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.util.StringUtils;
import com.suushiemaniac.lang.json.exception.JsonNotIterableException;

import java.util.*;
import java.util.stream.Collectors;

public class JsonObject extends JsonElement {
    private SortedMap<String, JSONType> members;

    public JsonObject(SortedMap<String, JSONType> members) {
        this(null, members);
    }

    public JsonObject(JSONType parent, SortedMap<String, JSONType> members) {
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
        return this.members.get(key);
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
        this.members.put(key, value);
    }

    @Override
    public void set(JSONType value) {
        if (value instanceof JsonObject) this.members = ((JsonObject) value).members;

        for (Map.Entry<String, JSONType> tEntry : this.members.entrySet())
            tEntry.getValue().setParent(this);
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
        this.members.put(key, value);
    }

    @Override
    public void remove(String key) {
        this.members.remove(key);
    }

    @Override
    public void remove(JSONType value) {
        this.members.remove(value.stringValue());
    }

    @Override
    public void clear() {
        this.members.clear();
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

        for (String key : this.members.keySet()) {
            stringedList.add(StringUtils.jsonWrap(key) + ":" + this.members.get(key).toString());
        }

        return "{" + String.join(",", stringedList) + "}";
    }

    //TODO maybe simplify
    @Override
    public String toFormatString() {
        List<String> stringedList = new ArrayList<>();

        String tabbing = this.members.size() <= 1 ? "" : StringUtils.copy("\t", this.hierarchy() + 1);

        for (String key : this.members.keySet()) {
            JSONType val = this.members.get(key);
            stringedList.add(tabbing + StringUtils.jsonWrap(key) + ": " + StringUtils.reduceTab(val.toFormatString()));
        }

        String openTabbing = StringUtils.copy("\t", this.hierarchy() - (this.parent() instanceof JsonElement && this.parent().size() <= 1 ? 1 : 0));
        String borderBreak = stringedList.size() <= 1 ? "" : "\n";
        String closingTabs = stringedList.size() <= 1 ? "" : openTabbing;
        return openTabbing + "{" + borderBreak + String.join(",\n", stringedList) + borderBreak + closingTabs + "}";
    }

    //TODO definitely simplify
    @Override
    public String toXMLString() {
        List<String> stringedList = new ArrayList<>();

        for (String key : this.members.keySet()) {
            String tagKey = StringUtils.jsonUnwrap(key);

            String openTag = "<" + tagKey + ">";
            String closeTag = "</" + StringUtils.jsonUnwrap(key) + ">";

            JSONType child = this.members.get(key);

            if (child instanceof JsonObject) {
                child = new JsonObject(child.parent(), new TreeMap<>(((JsonObject) child).members));
                List<String> properties = new ArrayList<>();

                for (JSONType childKey : child.keySet()) {
                    JSONType childVal = child.get(childKey);

                    if (childVal instanceof JsonElement) continue;

                    properties.add(childKey.stringValue() + "=" + StringUtils.jsonWrap(childVal.toString()));
                    child.remove(childKey);
                }

                tagKey += " " + String.join(" ", properties);
            }

            if (child instanceof JsonArray && child.size() > 0) {
                openTag = closeTag = "";
            }

            String val = child.toXMLString();
            stringedList.add(val.length() > 0 ? openTag + val + closeTag : "<" + tagKey + "/>");
        }

        return String.join("\n", stringedList);
    }

    @Override
    public Collection<JSONType> collect() {
        return this.members.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Collection<JSONType> keySet() {
        return this.members.keySet().stream().map(JsonString::new).collect(Collectors.toList());
    }
}