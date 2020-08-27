package com.homw.test.math;

import static org.junit.Assert.*;

import org.junit.Test;

public class ArraySearchTest {

	@Test
	public void testMatrixSearch() {
		int[][] matrix = new int[][] { new int[] { 1, 4, 7, 11, 15 }, new int[] { 2, 5, 8, 12, 19 },
				new int[] { 3, 6, 9, 16, 22 }, new int[] { 10, 13, 14, 17, 24 }, new int[] { 18, 21, 23, 26, 30 } };
		boolean ret = ArraySearch.sortedMatrixSearch(matrix, 5);
		assertTrue(ret);
	}
}
