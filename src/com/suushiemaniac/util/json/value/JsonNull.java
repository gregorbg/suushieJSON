package com.suushiemaniac.util.json.value;

public class JsonNull extends JSONType {
    public JsonNull() {
        this(null);
    }

    public JsonNull(JSONType parent) {
        super(parent);
    }

    @Override
    public Object nullValue() {
        return null;
    }

    public String toString() {
        return "null";
    }
}
