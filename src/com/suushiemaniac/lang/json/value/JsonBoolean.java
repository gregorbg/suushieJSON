package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;

public class JsonBoolean extends JsonType {
    private boolean value;

    public JsonBoolean(boolean value) {
        this(null, value);
    }

    public JsonBoolean(JSON parent, boolean value) {
        super(parent);
        this.value = value;
    }

    @Override
    public boolean booleanValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonBoolean && ((JsonBoolean) other).value == this.value;
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonBoolean) this.value = value.booleanValue();
    }
}