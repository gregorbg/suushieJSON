package com.suushiemaniac.lang.json.value;

import com.suushiemaniac.lang.json.JSON;
import com.suushiemaniac.lang.json.exception.JsonNotIterableException;
import com.suushiemaniac.lang.json.exception.JsonValueTypeException;
import com.suushiemaniac.lang.json.util.StringUtils;

import java.util.*;
import java.util.stream.Stream;

public abstract class JsonType extends JSON {
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

	public JsonType(JSON parent) {
		super(parent);
	}

	public JSON get(JSON keyIndex) {
		throw new JsonNotIterableException();
	}

	public JSON get(String key) {
		throw new JsonNotIterableException();
	}

	public JSON get(int index) {
		throw new JsonNotIterableException();
	}

	public void set(JSON keyIndex, JSON value) {
		throw new JsonNotIterableException();
	}

	public void set(String key, JSON value) {
		throw new JsonNotIterableException();
	}

	public void set(int index, JSON value) {
		throw new JsonNotIterableException();
	}

	public void set(JSON value) {
		throw new JsonNotIterableException();
	}

	public void add(JSON value) {
		throw new JsonNotIterableException();
	}

	public void add(JSON keyIndex, JSON value) {
		throw new JsonNotIterableException();
	}

	public void add(String key, JSON value) {
		throw new JsonNotIterableException();
	}

	public void add(int index, JSON value) {
		throw new JsonNotIterableException();
	}

	public void remove(JSON value) {
		throw new JsonNotIterableException();
	}

	public void remove(int index) {
		throw new JsonNotIterableException();
	}

	public void remove(String key) {
		throw new JsonNotIterableException();
	}

	public JSON keyIndexOf(JSON content) {
		throw new JsonNotIterableException();
	}

	public void clear() {
		throw new JsonNotIterableException();
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

	public Collection<JSON> collect() {
		return Collections.singleton(this);
	}

	public Set<JSON> keySet() {
		throw new JsonNotIterableException();
	}

	@Override
	public Stream<JSON> stream() {
		return Stream.of(this);
	}

	@Override
	public Map<String, JSON> nativeMap() {
		throw new JsonNotIterableException();
	}

	@Override
	public List<JSON> nativeList() {
		return Collections.singletonList(this);
	}

	@Override
	public Set<JSON> nativeSet() {
		return Collections.singleton(this);
	}

	@Override
	public Iterator<JSON> iterator() {
		return new SingleIterator<>(this);
	}
}
