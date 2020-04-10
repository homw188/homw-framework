package com.homw.test.math;

/**
 * @description 双向链表实现
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class DoubleLinkedList<E extends ListNode> implements LinkedList<E> {
	private E first, last;
	private int size;
	
	@Override
	public void addFirst(E node) {
		if (size == 0) {
			first = node;
			last = node;
		} else {
			first.prev = node;
			node.next = first;
			first = node;
		}
		size++;
	}

	@Override
	@SuppressWarnings("unchecked")
	public E removeLast() {
		if (size == 0) return null;
		
		E node = last;
		if (last.prev == null) {
			first = null;
		} else {
			last.prev.next = null;
		}
		last = (E) last.getPrev();
		size--;
		return node;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void remove(E node) {
		if (node.next == null) {
			removeLast();
			return;
		}
		
		if (node.prev == null) {
			first = (E) node.getNext();
		} else {
			node.prev.next = node.next;
			node.next.prev = node.prev;
		}
		node.prev = null;
		node.next = null;
		size--;
	}

	@Override
	public int size() {
		return this.size;
	}

	@Override
	public E getFirst() {
		return this.first;
	}
}
