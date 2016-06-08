package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.util.StringUtils;

public class JsonString extends JSONType {
    private String value;

    public JsonString(String value) {
        this(null, value);
    }

    public JsonString(JSONType parent, String value) {
        super(parent);
        this.value = value;
    }

    @Override
    public String stringValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return StringUtils.jsonWrap(this.value);
    }

    @Override
    public void set(JSONType value) {
        if (value instanceof JsonString) this.value = value.stringValue();
    }
}
