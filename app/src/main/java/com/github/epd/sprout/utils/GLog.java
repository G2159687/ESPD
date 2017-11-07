
package com.github.epd.sprout.utils;

import android.util.Log;

import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.Signal;

public class GLog {

	public static final String TAG = "GAME";

	public static final String POSITIVE = "++ ";
	public static final String NEGATIVE = "-- ";
	public static final String WARNING = "** ";
	public static final String HIGHLIGHT = "@@ ";

	public static Signal<String> update = new Signal<String>();

	public static void i(String text, Object... args) {

		if (args.length > 0) {
			text = Messages.format(text, args);
		}

		Log.i(TAG, text);
		update.dispatch(text);
	}

	public static void p(String text, Object... args) {
		i(POSITIVE + text, args);
	}

	public static void n(String text, Object... args) {
		i(NEGATIVE + text, args);
	}

	public static void w(String text, Object... args) {
		i(WARNING + text, args);
	}

	public static void h(String text, Object... args) {
		i(HIGHLIGHT + text, args);
	}
}
