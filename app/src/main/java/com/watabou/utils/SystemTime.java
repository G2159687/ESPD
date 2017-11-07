
package com.watabou.utils;

public class SystemTime {

	public static long now;

	public static void tick() {
		now = System.currentTimeMillis();
	}
}

