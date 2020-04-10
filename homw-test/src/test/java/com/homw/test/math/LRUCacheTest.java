package com.homw.test.math;

import java.util.List;

import org.junit.Test;

import com.homw.test.math.LRUCache;

/**
 * @description 基于LRU的缓存测试
 * @author Hom
 * @version 1.0
 * @since 2020-03-17
 */
public class LRUCacheTest {

	@Test
	public void test() {
		// 1、创建缓存
		LRUCache cache = new LRUCache(3);
		
		for (int i = 0; i < 10; i++) {
			// 2、加入缓存
			cache.put(String.valueOf(i), i);
			
			// 3、模拟随机访问
			int key = (int) (Math.random() * 10);
			System.out.println("random key: " + key + " -> value: " + cache.get(String.valueOf(key)));
			
			// 4、输出缓存内容
			List<Object> list = cache.list();
			for (Object item : list) {
				System.out.print(item + " ");
			}
			System.out.println();
		}
	}
}
