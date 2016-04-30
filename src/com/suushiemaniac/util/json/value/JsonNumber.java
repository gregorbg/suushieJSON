package com.suushiemaniac.util.json.value;

public abstract class JsonNumber extends JSONType {
    protected Number value;

    public JsonNumber(JSONType parent, Number value) {
        super(parent);
        this.value = value;
    }
}
