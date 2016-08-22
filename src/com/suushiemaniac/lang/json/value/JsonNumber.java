package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;

public abstract class JsonNumber extends JsonType {
    protected Number value;

    public JsonNumber(JSON parent, Number value) {
        super(parent);
        this.value = value;
    }
}
