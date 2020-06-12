package com.homw.test.pattern;

/**
 * @description 迭代器模式实现
 * @author Hom
 * @version 1.0
 * @since 2020-06-12
 */
public class IteratorPattern {

	public static void main(String[] args) {
		Collection<String> collection = new CollectionA<>();
		collection.add("A");
		collection.add("B");
		collection.add("C");

		Iterator<String> iterator = collection.iterator();
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + "\t");
		}
	}

	interface Iterator<E> {
		E next();
		boolean hasNext();
	}

	interface Collection<E> {
		Iterator<E> iterator();
		void add(E e);
		E get(int i);
		int size();
	}

	static class CollectionA<E> implements Collection<E> {
		int capacity;
		int size = 0;
		Object[] datas;

		public CollectionA() {
			this(16);
		}

		public CollectionA(int capacity) {
			this.capacity = capacity;
			datas = new Object[capacity];
		}

		@Override
		public void add(E e) {
			datas[size++] = e;
		}

		@Override
		@SuppressWarnings("unchecked")
		public E get(int i) {
			return (E) datas[i];
		}

		@Override
		public int size() {
			return size;
		}

		@Override
		public Iterator<E> iterator() {
			return new IteratorA<E>(this);
		}
	}

	static class IteratorA<E> implements Iterator<E> {
		int cursor;
		Collection<E> collection;

		public IteratorA(Collection<E> collection) {
			this.collection = collection;
		}

		@Override
		public E next() {
			return collection.get(cursor++);
		}

		@Override
		public boolean hasNext() {
			return cursor < collection.size();
		}
	}
}
