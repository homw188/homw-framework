package com.homw.test.math;

/**
 * @description 链表节点
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class ListNode {
	protected Object val;
	protected ListNode next, prev;
	
	public ListNode(Object val) {
		this.val = val;
	}
	
	public Object getVal() {
		return val;
	}
	public void setVal(Object val) {
		this.val = val;
	}
	public ListNode getNext() {
		return next;
	}
	public void setNext(ListNode next) {
		this.next = next;
	}
	public ListNode getPrev() {
		return prev;
	}
	public void setPrev(ListNode prev) {
		this.prev = prev;
	}
	
}
