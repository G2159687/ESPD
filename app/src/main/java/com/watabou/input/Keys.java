
package com.watabou.input;

import android.view.KeyEvent;

import com.watabou.utils.Signal;

import java.util.ArrayList;

public class Keys {

	public static final int BACK = KeyEvent.KEYCODE_BACK;
	public static final int MENU = KeyEvent.KEYCODE_MENU;
	public static final int VOLUME_UP = KeyEvent.KEYCODE_VOLUME_UP;
	public static final int VOLUME_DOWN = KeyEvent.KEYCODE_VOLUME_DOWN;

	public static Signal<Key> event = new Signal<Key>(true);

	public static void processTouchEvents(ArrayList<KeyEvent> events) {

		int size = events.size();
		for (int i = 0; i < size; i++) {

			KeyEvent e = events.get(i);

			switch (e.getAction()) {
				case KeyEvent.ACTION_DOWN:
					event.dispatch(new Key(e.getKeyCode(), true));
					break;
				case KeyEvent.ACTION_UP:
					event.dispatch(new Key(e.getKeyCode(), false));
					break;
			}
		}
	}

	public static class Key {

		public int code;
		public boolean pressed;

		public Key(int code, boolean pressed) {
			this.code = code;
			this.pressed = pressed;
		}
	}
}
