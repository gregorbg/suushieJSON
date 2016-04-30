package com.suushiemaniac.util.json.value;

public class JsonFloat extends JsonNumber {
    public JsonFloat(Number value) {
        this(null, value);
    }

    public JsonFloat(JSONType parent, Number value) {
        super(parent, value);
    }

    @Override
    public float floatValue() {
        return this.value.floatValue();
    }

    @Override
    public String toString() {
        return String.valueOf(this.value.floatValue());
    }

    @Override
    public void set(JSONType value) {
        if (value instanceof JsonFloat) this.value = value.floatValue();
    }
}
