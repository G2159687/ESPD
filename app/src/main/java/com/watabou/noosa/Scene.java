
package com.watabou.noosa;

import com.watabou.input.Keys;
import com.watabou.utils.Signal;

public class Scene extends Group {

	private Signal.Listener<Keys.Key> keyListener;

	public void create() {
		Keys.event.add(keyListener = new Signal.Listener<Keys.Key>() {
			@Override
			public void onSignal(Keys.Key key) {
				if (Game.instance != null && key.pressed) {
					switch (key.code) {
						case Keys.BACK:
							onBackPressed();
							break;
						case Keys.MENU:
							onMenuPressed();
							break;
					}
				}
			}
		});
	}

	@Override
	public void destroy() {
		Keys.event.remove(keyListener);
		super.destroy();
	}

	public void pause() {

	}

	public void resume() {

	}

	@Override
	public void update() {
		super.update();
	}

	@Override
	public Camera camera() {
		return Camera.main;
	}

	protected void onBackPressed() {
		Game.instance.finish();
	}

	protected void onMenuPressed() {

	}

}
