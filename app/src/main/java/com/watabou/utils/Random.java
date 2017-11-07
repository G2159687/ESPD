
package com.watabou.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Random {

	private static java.util.Random rand = new java.util.Random();

	public static float Float(float min, float max) {
		return (float) (min + Math.random() * (max - min));
	}

	public static float Float(float max) {
		return (float) (Math.random() * max);
	}

	public static float Float() {
		return (float) Math.random();
	}

	public static int Int(int max) {
		return max > 0 ? (int) (Math.random() * max) : 0;
	}

	public static int Int(int min, int max) {
		return min + (int) (Math.random() * (max - min));
	}

	public static int IntRange(int min, int max) {
		return min + (int) (Math.random() * (max - min + 1));
	}

	public static int NormalIntRange(int min, int max) {
		return min + (int) ((Math.random() + Math.random()) * (max - min + 1) / 2f);
	}

	public static int chances(float[] chances) {

		int length = chances.length;

		float sum = chances[0];
		for (int i = 1; i < length; i++) {
			sum += chances[i];
		}

		float value = Float(sum);
		sum = 0;
		for (int i = 0; i < length; i++) {
			sum += chances[i];
			if (value < sum) {
				return i;
			}
		}

		return -1;
	}

	@SuppressWarnings("unchecked")
	public static <K> K chances(HashMap<K, Float> chances) {

		int size = chances.size();

		Object[] values = chances.keySet().toArray();
		float[] probs = new float[size];
		float sum = 0;
		for (int i = 0; i < size; i++) {
			probs[i] = chances.get(values[i]);
			sum += probs[i];
		}

		if (sum <= 0) {
			return null;
		}

		float value = Float(sum);

		sum = probs[0];
		for (int i = 0; i < size; i++) {
			if (value < sum) {
				return (K) values[i];
			}
			sum += probs[i + 1];
		}

		return null;
	}

	public static int index(Collection<?> collection) {
		return (int) (Math.random() * collection.size());
	}

	@SafeVarargs
	public static <T> T oneOf(T... array) {
		return array[(int) (Math.random() * array.length)];
	}

	public static <T> T element(T[] array) {
		return element(array, array.length);
	}

	public static <T> T element(T[] array, int max) {
		return array[(int) (Math.random() * max)];
	}

	@SuppressWarnings("unchecked")
	public static <T> T element(Collection<? extends T> collection) {
		int size = collection.size();
		return size > 0 ?
				(T) collection.toArray()[Int(size)] :
				null;
	}

	public static<T> void shuffle( List<?extends T> list){
		Collections.shuffle(list, rand);
	}

	public static <T> void shuffle(T[] array) {
		for (int i = 0; i < array.length - 1; i++) {
			int j = Int(i, array.length);
			if (j != i) {
				T t = array[i];
				array[i] = array[j];
				array[j] = t;
			}
		}
	}

	public static <U, V> void shuffle(U[] u, V[] v) {
		for (int i = 0; i < u.length - 1; i++) {
			int j = Int(i, u.length);
			if (j != i) {
				U ut = u[i];
				u[i] = u[j];
				u[j] = ut;

				V vt = v[i];
				v[i] = v[j];
				v[j] = vt;
			}
		}
	}
}
