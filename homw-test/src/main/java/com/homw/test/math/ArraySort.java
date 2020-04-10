package com.homw.test.math;

/**
 * @description 数组排序算法
 * @author Hom
 * @version 1.0
 * @since 2020-03-19
 */
public class ArraySort {

	/**
	 * 快速排序
	 * @param arr
	 * @param low
	 * @param high
	 */
	public static void quickSort(int[] arr, int low, int high) {
		if (low >= high) return;
		
		// 基准值
		int key = arr[low];
		
		// 左右移动指针
		int left = low;
		int right = high;
		
		while (left < right) {
			while (left < right && arr[right] > key) {
				right--;
			}
			arr[left] = arr[right];
			
			while (left < right && arr[left] <= key) {
				left++;
			}
			arr[right] = arr[left];
		}
		// 重置基准值
		arr[left] = key;
		
		quickSort(arr, low, left - 1);
		quickSort(arr, right + 1, high);
	}
	
	/**
	 * 冒泡排序
	 * @param arr
	 */
	public static void bubbleSort(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > arr[j]) {
					swap(arr, i, j);
				}
			}
		}
	}
	
	/**
	 * 堆排序
	 * @param arr
	 */
	public static void heapSort(int[] arr) {
		int len = arr.length;
		
		// 构建大顶堆
		for (int i = (len / 2 - 1); i >= 0; i--) {
			adjustHeap(arr, i, len);
		}
		
		while (len > 0) {
			// 首尾交换
			swap(arr, 0, len - 1);
			len--;
			adjustHeap(arr, 0, len);
		}
	}
	
	/**
	 * 调整堆
	 * @param arr
	 * @param i
	 * @param limit
	 */
	private static void adjustHeap(int[] arr, int i, int limit) {
		int max = i;
		int left = i * 2;
		int right = i * 2 + 1;
		
		// 查找左右子节点中最大值索引
		if (left < limit && arr[left] > arr[max]) {
			max = left;
		}
		if (right < limit && arr[right] > arr[max]) {
			max = right;
		}
		
		if (max != i) {
			swap(arr, i, max);
			adjustHeap(arr, max, limit);
		}
	}
	
	/**
	 * 元素交换
	 * @param arr
	 * @param i 第一个元素索引
	 * @param j 第二个元素索引
	 */
	private static void swap(int[] arr, int i, int j) {
		int swap = arr[i];
		arr[i] = arr[j];
		arr[j] = swap;
	}
	
}
