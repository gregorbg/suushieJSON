package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.exception.JsonValueTypeException;

public abstract class JsonElement extends JSON {
    public JsonElement(JSON parent) {
        super(parent);
    }

    public boolean booleanValue() {
        throw new JsonValueTypeException();
    }

    public int intValue() {
        throw new JsonValueTypeException();
    }

    public float floatValue() {
        throw new JsonValueTypeException();
    }

    public String stringValue() {
        throw new JsonValueTypeException();
    }

    public Object nullValue() {
        throw new JsonValueTypeException();
    }
}
