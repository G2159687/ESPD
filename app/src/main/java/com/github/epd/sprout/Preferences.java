
package com.github.epd.sprout;

import android.content.Context;
import android.content.SharedPreferences;

import com.watabou.noosa.Game;
import com.watabou.utils.GameMath;

enum Preferences {

	INSTANCE;

	public static final String KEY_LANDSCAPE = "landscape";
	public static final String KEY_IMMERSIVE = "immersive";
	public static final String KEY_SCALE = "scale";
	public static final String KEY_MUSIC = "music";
	public static final String KEY_SOUND_FX = "soundfx";
	public static final String KEY_FLIPTOOLBAR = "flipped_ui";
	public static final String KEY_FLIPTAGS = "flip_tags";
	public static final String KEY_BARMODE = "toolbar_mode";
	public static final String KEY_ZOOM = "zoom";
	public static final String KEY_LAST_CLASS = "last_class";
	public static final String KEY_CHALLENGES = "challenges";
	public static final String KEY_QUICKSLOTS = "quickslots";
	public static final String KEY_INTRO = "intro";
	public static final String KEY_BRIGHTNESS = "brightness";
	public static final String KEY_VERSION = "version";
	public static final String KEY_LANG = "language";
	public static final String KEY_CLASSICFONT = "classic_font";
	public static final String KEY_AUTOCOLLECT = "auto_collect";
	public static final String KEY_DEWMODE = "dew_mode";
	public static final String KEY_MAPSIZE = "map_size";

	private SharedPreferences prefs;

	private SharedPreferences get() {
		if (prefs == null) {
			prefs = Game.instance.getPreferences(Context.MODE_PRIVATE);
		}
		return prefs;
	}

	int getInt(String key, int defValue) {
		return getInt(key, defValue, Integer.MIN_VALUE, Integer.MAX_VALUE);
	}

	int getInt(String key, int defValue, int min, int max) {
		try {
			int i = get().getInt(key, defValue);
			if (i < min || i > max) {
				int val = (int) GameMath.gate(min, i, max);
				put(key, val);
				return val;
			} else {
				return i;
			}
		} catch (ClassCastException e) {
			ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}

	boolean getBoolean(String key, boolean defValue) {
		try {
			return get().getBoolean(key, defValue);
		} catch (ClassCastException e) {
			ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}

	String getString(String key, String defValue) {
		return getString(key, defValue, Integer.MAX_VALUE);
	}

	String getString(String key, String defValue, int maxLength) {
		try {
			String s = get().getString(key, defValue);
			if (s != null && s.length() > maxLength) {
				put(key, defValue);
				return defValue;
			} else {
				return s;
			}
		} catch (ClassCastException e) {
			ShatteredPixelDungeon.reportException(e);
			put(key, defValue);
			return defValue;
		}
	}

	void put(String key, int value) {
		get().edit().putInt(key, value).apply();
	}

	void put(String key, boolean value) {
		get().edit().putBoolean(key, value).apply();
	}

	void put(String key, String value) {
		get().edit().putString(key, value).apply();
	}
}
