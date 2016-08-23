package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.util.StringUtils;
import com.suushiemaniac.lang.json.exception.JsonNotIterableException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonObject extends JsonElement {
    private SortedMap<String, JSON> members;

    public JsonObject(SortedMap<String, JSON> members) {
        this(null, members);
    }

    public JsonObject(JSON parent, SortedMap<String, JSON> members) {
        super(parent);

        for (Map.Entry<String, JSON> jsonTypeEntry : members.entrySet()) jsonTypeEntry.getValue().setParent(this);

        this.members = members;
    }

    @Override
    public JSON get(JSON keyIndex) {
    	try {
    		return this.members.get(keyIndex.stringValue());
		} catch (JsonNotIterableException e) {
			throw new JsonNotIterableException("Can't get child element by given JSON type. Expected: String, Found: " + keyIndex.type());
		}
    }

    @Override
    public JSON get(String key) {
        return this.members.get(key);
    }

    @Override
    public JSON get(int index) {
        return this.members.get(String.valueOf(index));
    }

    @Override
    public void set(JSON keyIndex, JSON value) {
        if (keyIndex instanceof JsonString) {
            value.setParent(this);
            this.members.put(keyIndex.stringValue(), value);
        }
    }

    @Override
    public void set(String key, JSON value) {
        value.setParent(this);
        this.members.put(key, value);
    }

    @Override
    public void set(int index, JSON value) {
        value.setParent(this);
        this.members.put(String.valueOf(index), value);
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonObject) this.members = ((JsonObject) value).members;

        for (Map.Entry<String, JSON> tEntry : this.members.entrySet())
            tEntry.getValue().setParent(this);
    }

    @Override
    public void add(JSON value) {
        throw new JsonNotIterableException("Can't add child element without explicit key");
    }

    @Override
    public void add(JSON keyIndex, JSON value) {
        if (keyIndex instanceof JsonString) {
            value.setParent(this);
            this.members.put(keyIndex.stringValue(), value);
        }
    }

    @Override
    public void add(String key, JSON value) {
        value.setParent(this);
        this.members.put(key, value);
    }

    @Override
    public void add(int index, JSON value) {
		value.setParent(this);
		this.members.put(String.valueOf(index), value);
    }

    @Override
    public void remove(String key) {
        this.members.remove(key);
    }

    @Override
    public void remove(JSON value) {
        this.members.remove(value.stringValue());
    }

    @Override
    public void remove(int index) {
		this.members.remove("" + index);
    }

    @Override
    public JSON keyIndexOf(JSON content) {
        for (Map.Entry<String, JSON> entry : this.members.entrySet()) {
            if (entry.getValue().equals(content)) {
                return new JsonString(entry.getKey());
            }
        }

        return null;
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

        for (Map.Entry<String, JSON> tEntry : this.members.entrySet()) {
            deepSize += tEntry.getValue().deepSize();
        }

        return deepSize;
    }

    @Override
    public Iterator<JSON> iterator() {
        List<JSON> typeList = this.members.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());

        return typeList.iterator();
    }

    public String toString() {
        List<String> stringedList = new ArrayList<>();

        for (String key : this.members.keySet()) {
            stringedList.add(StringUtils.jsonWrap(key) + ":" + this.members.get(key).toString());
        }

        return "{" + String.join(",", stringedList) + "}";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonObject && ((JsonObject) other).members.equals(this.members);
    }

    //TODO maybe simplify
    @Override
    public String toFormatString() {
        List<String> stringedList = new ArrayList<>();

        String tabbing = this.members.size() <= 1 ? "" : StringUtils.copy("\t", this.hierarchy() + 1);

        for (String key : this.members.keySet()) {
            JSON val = this.members.get(key);
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

            JSON child = this.members.get(key);

            if (child instanceof JsonObject) {
                child = new JsonObject(child.parent(), new TreeMap<>(((JsonObject) child).members));
                List<String> properties = new ArrayList<>();

                for (JSON childKey : child.keySet()) {
                    JSON childVal = child.get(childKey);

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
    public Object toNative() {
        return this.nativeMap();
    }

    @Override
    public Collection<JSON> collect() {
        return this.members.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }

    @Override
    public Set<JSON> keySet() {
        return this.members.keySet().stream().map(JsonString::new).collect(Collectors.toSet());
    }

    @Override
    public Map<String, Object> nativeMap() {
        SortedMap<String, Object> nativeMap = new TreeMap<>();

        for (String key : this.members.keySet())
        	nativeMap.put(key, this.members.get(key).toNative());

        return nativeMap;
    }

    @Override
    public List<Object> nativeList() {
		return this.nativeMap().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
    }

    @Override
    public Set<Object> nativeSet() {
		return this.nativeMap().entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toSet());
    }
}