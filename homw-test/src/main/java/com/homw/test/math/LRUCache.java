package com.homw.test.math;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @description 基于LRU的缓存实现
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class LRUCache {
	private HashMap<String, LRUListNode> map; // 保证 O(1) 查询效率
	private DoubleLinkedList<LRUListNode> cache; // 保证有序，插入、删除效率
	private int cap = 0; // 缓存容量
	
	/**
	 * @param cap 缓存容量
	 */
	public LRUCache (int cap) {
		map = new HashMap<>();
		cache = new DoubleLinkedList<>();
		this.cap = cap;
	}
	
	/**
	 * 加入缓存
	 * @param key
	 * @param val
	 */
	public void put(String key, Object val) {
		// 1、创建缓存节点
		LRUListNode x = new LRUListNode(key, val);
		
		// 2.若存在，则更新
		if (map.containsKey(key)) {
			LRUListNode node = map.get(key);
			cache.remove(node);
			cache.addFirst(x);
			map.put(key, x);
		} else {
			// 3、若不存在，判断大小
			if (map.size() == cap) {
				// 4、超限，则移除最后一个节点
				LRUListNode node = cache.removeLast();
				map.remove(node.key);
			}
			// 5、加入到缓存
			cache.addFirst(x);
			map.put(key, x);
		}
	}
	
	/**
	 * 获取缓存
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		// 缓存命中，则更新至首节点
		if (map.containsKey(key)) {
			LRUListNode node = map.get(key);
			cache.remove(node);
			cache.addFirst(node);
			return node.val;
		}
		return null;
	}
	
	/**
	 * 缓存大小
	 * @return
	 */
	public int size() {
		return cache.size();
	}
	
	/**
	 * 缓存集合
	 * @return
	 */
	public List<Object> list () {
		int size = size();
		if (size == 0) return null;
		
		List<Object> list = new ArrayList<>(size);
		ListNode n = cache.getFirst();
		list.add(n.val);
		while ((n = n.next) != null) {
			list.add(n.val);
		}
		return list;
	}
	
	/**
	 * @description LRU缓存节点
	 * @author Hom
	 * @version 1.0
	 * @since 2020-03-17
	 */
	class LRUListNode extends ListNode {
		String key;
		public LRUListNode(String key, Object val) {
			super(val);
			this.key = key;
		}
	}
	
}
