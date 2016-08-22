package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.exception.JsonValueTypeException;
import com.suushiemaniac.lang.json.util.StringUtils;
import com.suushiemaniac.lang.json.exception.JsonNotIterableException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JsonArray extends JsonElement {
    private List<JSON> elements;

    public JsonArray(List<JSON> elements) {
        this(null, elements);
    }

    public JsonArray(JSON parent, List<JSON> elements) {
        super(parent);

        for (JSON jsonType : elements) jsonType.setParent(this);

        this.elements = elements;
    }

    @Override
    public JSON get(JSON keyIndex) {
        if (keyIndex instanceof JsonNumber) return this.elements.get(keyIndex.intValue());
        else throw new JsonNotIterableException("Can't get child element by given JSON type. Expected: Integer, Found: " + keyIndex.type());
    }

	@Override
	public JSON get(String key) {
		throw new JsonValueTypeException("Can't get child element by string key");
	}

	@Override
    public JSON get(int index) {
        return this.elements.get(index);
    }

    @Override
    public void set(JSON keyIndex, JSON value) {
        value.setParent(this);
        if (keyIndex instanceof JsonNumber) this.elements.set(keyIndex.intValue(), value);
    }

	@Override
	public void set(String key, JSON value) {
		throw new JsonValueTypeException("Can't set list element by string key");
	}

	@Override
    public void set(int index, JSON value) {
        value.setParent(this);
        this.elements.set(index, value);
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonArray) this.elements = ((JsonArray) value).elements;

        for (JSON t : this.elements) t.setParent(this);
    }

    @Override
    public void add(JSON value) {
        value.setParent(this);
        this.elements.add(value);
    }

	@Override
	public void add(JSON keyIndex, JSON value) {
		if (keyIndex instanceof JsonInteger) {
			this.add(keyIndex.intValue(), value);
		} else {
			throw new JsonValueTypeException("Can't add child element by given JSON type. Expected: Integer, Found: " + keyIndex.type());
		}
	}

	@Override
	public void add(String key, JSON value) {
		throw new JsonValueTypeException("Can't add child element by string key");
	}

	@Override
	public void add(int index, JSON value) {
		value.setParent(this);
		this.elements.add(index, value);
	}

	@Override
    public void remove(JSON value) {
        this.elements.remove(value);
    }

    @Override
    public void remove(int index) {
        this.elements.remove(index);
    }

	@Override
	public void remove(String key) {
		throw new JsonValueTypeException("Can't remove child element by string key");
	}

	@Override
    public JSON keyIndexOf(JSON content) {
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

        for (JSON t : this.elements)
        	deepSize += t.deepSize();

        return deepSize;
    }

    @Override
    public Iterator<JSON> iterator() {
        return this.elements.iterator();
    }

    public String toString() {
        List<String> stringedList = new ArrayList<>();

        for (JSON t : this.elements)
        	stringedList.add(t.toString());

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

        for (JSON t : this.elements)
			stringedList.add(this.size() <= 1 ? StringUtils.reduceTab(t.toFormatString()) : t.toFormatString());

        String openTabbing = StringUtils.copy("\t", this.hierarchy() - (this.parent() instanceof JsonElement && this.parent().size() <= 1 ? 1 : 0));
        String borderBreak = stringedList.size() <= 1 ? "" : "\n";
        String closingTabs = stringedList.size() <= 1 ? "" : openTabbing;
        return openTabbing + "[" + borderBreak + String.join(",\n", stringedList) + borderBreak + closingTabs + "]";
    }

    @Override
    public String toXMLString() {
        List<String> stringedList = new ArrayList<>();

        String key = "array";
        JSON parent = this.parent();
        if (parent instanceof JsonObject) {
            for (JSON parentKey : parent.keySet()) {
                if (parent.get(parentKey).equals(this)) {
                    key = parentKey.stringValue();
                }
            }
        }

        for (JSON t : this.elements) {
            String openTag = "<" + key + ">";
            String closeTag = "</" + key + ">";

            String val = t.toXMLString();
            if (val.length() > 0) stringedList.add(openTag + val + closeTag);
        }

        return String.join("\n", stringedList);
    }

    @Override
    public Collection<JSON> collect() {
        return this.nativeList();
    }

	@Override
	public Stream<JSON> stream() {
		return this.elements.stream();
	}

	@Override
    public Set<JSON> keySet() {
    	Set<JSON> keySet = new HashSet<>();

		for (int i = 0; i < this.elements.size(); i++) {
			keySet.add(new JsonInteger(i));
		}

        return keySet;
    }

	@Override
	public Map<String, JSON> nativeMap() {
		Map<String, JSON> nativeMap = new HashMap<>();

		for (int i = 0; i < this.elements.size(); i++) {
			nativeMap.put("" + i, this.elements.get(i));
		}

		return nativeMap;
	}

	@Override
	public List<JSON> nativeList() {
		return new ArrayList<>(this.elements);
	}

	@Override
	public Set<JSON> nativeSet() {
		return new HashSet<>(this.elements);
	}
}
