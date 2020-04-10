package com.homw.test.math;

/**
 * @description 链表接口
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public interface LinkedList<E extends ListNode> {
	void addFirst(E node);
	E removeLast();
	void remove(E node);
	int size();
	E getFirst();
}
