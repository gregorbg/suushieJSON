package com.suushiemaniac.util.json.value;

import com.suushiemaniac.util.json.StringUtils;
import com.suushiemaniac.util.json.exception.JsonNotIterableException;
import com.suushiemaniac.util.json.exception.JsonValueTypeException;

import java.util.Iterator;

public abstract class JSONType implements Iterable<JSONType> {
    private class SingleIterator<T> implements Iterator<T> {
        private T toRead;
        private boolean hasRead;

        public SingleIterator(T element) {
            this.toRead = element;
        }

        @Override
        public boolean hasNext() {
            return !hasRead;
        }

        @Override
        public T next() {
            hasRead = true;
            return toRead;
        }
    }

    private JSONType parent;

    public JSONType(JSONType parent) {
        this.parent = parent;
    }

    public abstract String toString();

    public JSONType parent() {
        return this.parent;
    }

    public int hierarchy() {
        int hierarchy = 0;
        JSONType parent = this.parent();

        while (parent != null) {
            parent = parent.parent();
            hierarchy++;
        }

        return hierarchy;
    }

    public String val() {
        return this.toString();
    }

    public JSONType get(JSONType keyIndex) {
        throw new JsonNotIterableException();
    }

    public JSONType get(String key) {
        throw new JsonNotIterableException();
    }

    public JSONType get(int index) {
        throw new JsonNotIterableException();
    }

    public void set(JSONType keyIndex, JSONType value) {
        throw new JsonNotIterableException();
    }

    public void set(String key, JSONType value) {
        throw new JsonNotIterableException();
    }

    public void set(int index, JSONType value) {
        throw new JsonNotIterableException();
    }

    public void set(JSONType value) {
        throw new JsonNotIterableException();
    }

    public void add(JSONType value) {
        throw new JsonNotIterableException();
    }

    public void add(JSONType keyIndex, JSONType value) {
        throw new JsonNotIterableException();
    }

    public void add(String key, JSONType value) {
        throw new JsonNotIterableException();
    }

    public int size() {
        return 1;
    }

    public int deepSize() {
        return 1;
    }

    public void setParent(JSONType parent) {
        this.parent = parent;
    }

    public String toFormatString() {
        return StringUtils.copy("\t", this.hierarchy()) + this.toString();
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

    @Override
    public Iterator<JSONType> iterator() {
        return new SingleIterator<>(this);
    }
}
