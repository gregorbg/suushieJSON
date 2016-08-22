package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;

public class JsonBoolean extends JsonType {
    private boolean value;

    public JsonBoolean(boolean value) {
        this(null, value);
    }

    public JsonBoolean(JSON parent, boolean value) {
        super(parent);
        this.value = value;
    }

    @Override
    public boolean booleanValue() {
        return this.value;
    }

	@Override
	public String stringValue() {
		return "" + this.value;
	}

	@Override
	public int intValue() {
		return this.value ? 1 : 0;
	}

	@Override
	public float floatValue() {
		return this.value ? 1f : 0f;
	}

	@Override
    public String toString() {
        return String.valueOf(this.value);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonBoolean && ((JsonBoolean) other).value == this.value;
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonBoolean) this.value = value.booleanValue();
    }
}