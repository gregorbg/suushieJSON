package com.suushiemaniac.util.json.value;

import com.suushiemaniac.util.json.StringUtils;

public class JsonString extends JSONType {
    private String value;

    public JsonString(String value) {
        this(null, value);
    }

    public JsonString(JSONType parent, String value) {
        super(parent);
        this.value = StringUtils.jsonWrap(value);
    }

    @Override
    public String stringValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public void set(JSONType value) {
        if (value instanceof JsonString) this.value = value.stringValue();
    }
}
