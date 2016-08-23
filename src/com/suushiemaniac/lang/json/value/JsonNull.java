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

	@Override
	public Object toNative() {
		return this.nullValue();
	}

	@Override
    public String stringValue() {
        return "null";
    }

	@Override
	public float floatValue() {
		return 0f;
	}

	@Override
	public int intValue() {
		return 0;
	}

	@Override
	public boolean booleanValue() {
		return false;
	}

	public String toString() {
        return "null";
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonNull || other == null;
    }
}
