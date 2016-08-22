package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.exception.JsonValueTypeException;

public abstract class JsonElement extends JSON {
    public JsonElement(JSON parent) {
        super(parent);
    }

    public boolean booleanValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Boolean");
    }

    public int intValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Integer");
    }

    public float floatValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Float");
    }

    public String stringValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type String");
    }

    public Object nullValue() {
        throw new JsonValueTypeException("Can't access element classes as primitive type Null");
    }
}
