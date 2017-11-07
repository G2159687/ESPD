
package com.github.epd.sprout;

import com.watabou.noosa.NinePatch;

public class Chrome {

	public enum Type {
		TOAST, TOAST_TR, WINDOW, BUTTON, TAG, GEM, SCROLL, TAB_SET, TAB_SELECTED, TAB_UNSELECTED
	}

	public static NinePatch get(Type type) {
		String Asset = Assets.CHROME;
		switch (type) {
			case WINDOW:
				return new NinePatch(Asset, 0, 0, 20, 20, 6);
			case TOAST:
				return new NinePatch(Asset, 22, 0, 18, 18, 5);
			case TOAST_TR:
				return new NinePatch(Asset, 40, 0, 18, 18, 5);
			case BUTTON:
				return new NinePatch(Asset, 58, 0, 4, 4, 1);
			case TAG:
				return new NinePatch(Asset, 22, 18, 16, 14, 3);
			case GEM:
				return new NinePatch(Asset, 0, 32, 32, 32, 13);
			case SCROLL:
				return new NinePatch(Asset, 32, 32, 32, 32, 5, 11, 5, 11);
			case TAB_SET:
				return new NinePatch(Asset, 64, 0, 20, 20, 6);
			case TAB_SELECTED:
				return new NinePatch(Asset, 65, 22, 8, 13, 3, 7, 3, 5);
			case TAB_UNSELECTED:
				return new NinePatch(Asset, 75, 22, 8, 13, 3, 7, 3, 5);
			default:
				return null;
		}
	}
}
