package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;

public class JsonNull extends JsonType {
    public JsonNull() {
        this(null);
    }

    public JsonNull(JSON parent) {
        super(parent);
    }

    @Override
    public Object nullValue() {
        return null;
    }

    public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonNull || other == null;
    }
}
