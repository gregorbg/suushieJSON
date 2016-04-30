package com.suushiemaniac.lang.json.value;

public class JsonBoolean extends JSONType {
    private boolean value;

    public JsonBoolean(boolean value) {
        this(null, value);
    }

    public JsonBoolean(JSONType parent, boolean value) {
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
    public void set(JSONType value) {
        if (value instanceof JsonBoolean) this.value = value.booleanValue();
    }
}