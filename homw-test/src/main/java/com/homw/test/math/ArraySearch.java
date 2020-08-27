package com.homw.test.math;

/**
 * @description 数组查找算法
 * @author Hom
 * @version 1.0
 * @since 2020-07-24
 */
public class ArraySearch {

	/**
	 * 有序矩阵查找
	 * 
	 * @param matrix 从左到右（行） & 从上到下（列）：小->大
	 * @param target 查找目标值
	 * @return
	 */
	public static boolean sortedMatrixSearch(int[][] matrix, int target) {
		if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
			return false;
		}
		int rows = matrix.length;
		int cols = matrix[0].length;
		int r = 0;
		int c = cols - 1;
		while (r < rows && c >= 0) {
			int item = matrix[r][c];
			if (item == target) {
				return true;
			}

			if (item > target) {
				c--;
			} else {
				r++;
			}
		}
		return false;
	}
}
