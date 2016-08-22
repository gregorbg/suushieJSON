package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;

public class JsonFloat extends JsonNumber {
    public JsonFloat(Number value) {
        this(null, value);
    }

    public JsonFloat(JSON parent, Number value) {
        super(parent, value);
    }

    @Override
    public float floatValue() {
        return this.value.floatValue();
    }

	@Override
	public int intValue() {
		return this.value.intValue();
	}

	@Override
	public String stringValue() {
		return "" + this.value.floatValue();
	}

	@Override
    public String toString() {
        return String.valueOf(this.value.floatValue());
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof JsonFloat && ((JsonFloat) other).value.floatValue() == this.value.floatValue();
    }

    @Override
    public void set(JSON value) {
        if (value instanceof JsonFloat) this.value = value.floatValue();
    }
}
