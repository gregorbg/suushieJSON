package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.exception.JsonNotIterableException;
import com.suushiemaniac.lang.json.exception.JsonValueTypeException;
import com.suushiemaniac.lang.json.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

public abstract class JsonType extends JSON {
	public static JSON fromNative(Object nativeObj) {
		if (nativeObj instanceof Boolean) {
			return new JsonBoolean((Boolean) nativeObj);
		} else if (nativeObj instanceof Float) {
			return new JsonFloat((Float) nativeObj);
		} else if (nativeObj instanceof Integer) {
			return new JsonInteger((Integer) nativeObj);
		} else if (nativeObj instanceof Void) {
			return new JsonNull();
		} else if (nativeObj instanceof String) {
			return new JsonString((String) nativeObj);
		}

		return null;
	}

	public class SingleIterator<T> implements Iterator<T> {
		private T toRead;
		private boolean hasRead;

		public SingleIterator(T element) {
			this.hasRead = false;
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

	protected JsonType(JSON parent) {
		super(parent);
	}

	public JSON get(JSON keyIndex) {
		throw new JsonNotIterableException("Can't get child elements from " + this.type());
	}

	public JSON get(String key) {
		throw new JsonNotIterableException("Can't get child elements from " + this.type());
	}

	public JSON get(int index) {
		throw new JsonNotIterableException("Can't get child elements from " + this.type());
	}

	public void set(JSON keyIndex, JSON value) {
		throw new JsonNotIterableException("Can't set type " + this.type() + " on key/index-based access");
	}

	public void set(String key, JSON value) {
		throw new JsonNotIterableException("Can't set type " + this.type() + " on key-based access");
	}

	public void set(int index, JSON value) {
		throw new JsonNotIterableException("Can't set type " + this.type() + " on index-based access");
	}

	public void set(JSON value) {
		throw new JsonNotIterableException("Invalid type for setting. Expected: " + this.type() + ", Found: " + value.type());
	}

	public void add(JSON value) {
		throw new JsonNotIterableException("Can't add to single value type " + this.type());
	}

	public void add(JSON keyIndex, JSON value) {
		throw new JsonNotIterableException("Can't add to single value type " + this.type());
	}

	public void add(String key, JSON value) {
		throw new JsonNotIterableException("Can't add to single value type " + this.type());
	}

	public void add(int index, JSON value) {
		throw new JsonNotIterableException("Can't add to single value type " + this.type());
	}

	public void remove(JSON value) {
		throw new JsonNotIterableException("Can't remove from single value type " + this.type());
	}

	public void remove(int index) {
		throw new JsonNotIterableException("Can't remove from single value type " + this.type());
	}

	public void remove(String key) {
		throw new JsonNotIterableException("Can't remove from single value type " + this.type());
	}

	public JSON keyIndexOf(JSON content) {
		throw new JsonNotIterableException("Single value types don't have any keys/indices");
	}

	public void clear() {
		throw new JsonNotIterableException("Can't clear single value types");
	}

	public int size() {
		return 1;
	}

	public int deepSize() {
		return 1;
	}

	public String toFormatString() {
		return StringUtils.copy("\t", this.hierarchy()) + this.toString();
	}

	public String toXMLString() {
		return this.toString();
	}

	public boolean booleanValue() {
		throw new JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Boolean");
	}

	public int intValue() {
		throw new JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Integer");
	}

	public float floatValue() {
		throw new JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Float");
	}

	public String stringValue() {
		throw new JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: String");
	}

	public Object nullValue() {
		throw new JsonValueTypeException("Accessing by incompatible value. Expected: " + this.type() + ", Found: Null");
	}

	public Collection<JSON> collect() {
		return Collections.singleton(this);
	}

	public Set<JSON> keySet() {
		return Collections.emptySet();
	}

	@Override
	public Stream<JSON> stream() {
		return Stream.of(this);
	}

	@Override
	public Map<String, Object> nativeMap() {
		throw new JsonNotIterableException();
	}

	@Override
	public List<Object> nativeList() {
		return Collections.singletonList(this);
	}

	@Override
	public Set<Object> nativeSet() {
		return Collections.singleton(this);
	}

	@Override
	public Iterator<JSON> iterator() {
		return new SingleIterator<>(this);
	}
}
