package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;

public class JsonInteger extends JsonNumber {
    public JsonInteger(Number value) {
        this(null, value);
    }

    public JsonInteger(JSON parent, Number value) {
        super(parent, value);
    }

    @Override
    public int intValue() {
        return this.value.intValue();
    }

    @Override
    public String toString() {
        return String.valueOf(this.value.intValue());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonInteger && ((JsonInteger) other).value.intValue() == this.value.intValue();
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonInteger) this.value = value.intValue();
    }
}
