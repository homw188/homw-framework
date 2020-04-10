package com.homw.test.math;

import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @description 排序算法测试
 * @author Hom
 * @version 1.0
 * @since 2020-03-20
 */
public class ArraySortTest {

	int arr[];// 待排序数组
	int size = 10;// 数组长度
	long start, end;
	
	@Before
	public void before() {
		arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = (int) (Math.random() * size * 0.75);
		}
		System.out.println("排序前：" + Arrays.toString(arr));
		start = System.currentTimeMillis();
	}
	
	@After
	public void after() {
		end = System.currentTimeMillis();
		System.out.println("排序后：" + Arrays.toString(arr));
		System.out.println("排序耗时：" + (end - start) + " ms");
		
		// 排序结果检查
		for (int i = 0; i < arr.length - 1; i++) {
			if (arr[i + 1] < arr[i]) {
				System.out.println("排序失败，存在第" + (i + 2) + "个元素小于第" + (i + 1) + "元素");
				break;
			}
		}
	}
	
	@Test
	public void testQuikSort() {
		ArraySort.quickSort(arr, 0, arr.length - 1);
	}
	
	@Test
	public void testBubbleSort() {
		ArraySort.bubbleSort(arr);
	}
	
	@Test
	public void testHeapSort() {
		ArraySort.heapSort(arr);
	}
}
