
package com.watabou.utils;

import java.util.ArrayList;
import java.util.List;

public class SparseArray<T> extends android.util.SparseArray<T> {

	public int[] keyArray() {
		int size = size();
		int[] array = new int[size];
		for (int i = 0; i < size; i++) {
			array[i] = keyAt(i);
		}
		return array;
	}

	public List<T> values() {
		int size = size();
		ArrayList<T> list = new ArrayList<T>(size);
		for (int i = 0; i < size; i++) {
			list.add(i, valueAt(i));
		}
		return list;
	}
}
