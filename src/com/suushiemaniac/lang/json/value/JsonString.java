package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.util.StringUtils;

public class JsonString extends JsonType {
    private String value;

    public JsonString(String value) {
        this(null, value);
    }

    public JsonString(JSON parent, String value) {
        super(parent);
        this.value = StringUtils.jsonUnwrap(value);
    }

    @Override
    public String stringValue() {
        return this.value;
    }

    @Override
    public Object toNative() {
        return this.stringValue();
    }

    @Override
    public boolean booleanValue() {
        return this.value.length() > 0;
    }

    @Override
    public String toString() {
        return StringUtils.jsonWrap(this.value);
    }

    @Override
    public boolean equals(Object other) {
        return (other instanceof JsonString && ((JsonString) other).value.equals(this.value))
                || (other instanceof String && other.equals(this.value));
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonString) this.value = value.stringValue();
    }
}
