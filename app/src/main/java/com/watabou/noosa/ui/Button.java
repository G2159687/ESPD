
package com.watabou.noosa.ui;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Game;
import com.watabou.noosa.TouchArea;

public class Button extends Component {

	public static float longClick = 1f;

	protected TouchArea hotArea;

	protected boolean pressed;
	protected float pressTime;

	protected boolean processed;

	@Override
	protected void createChildren() {
		hotArea = new TouchArea(0, 0, 0, 0) {
			@Override
			protected void onTouchDown(Touch touch) {
				pressed = true;
				pressTime = 0;
				processed = false;
				Button.this.onTouchDown();
			}

			@Override
			protected void onTouchUp(Touch touch) {
				pressed = false;
				Button.this.onTouchUp();
			}

			@Override
			protected void onClick(Touch touch) {
				if (!processed) {
					Button.this.onClick();
				}
			}
		};
		add(hotArea);
	}

	@Override
	public void update() {
		super.update();

		hotArea.active = visible;

		if (pressed) {
			if ((pressTime += Game.elapsed) >= longClick) {
				pressed = false;
				if (onLongClick()) {

					hotArea.reset();
					processed = true;
					onTouchUp();
				}
			}
		}
	}

	protected void onTouchDown() {
	}

	protected void onTouchUp() {
	}

	protected void onClick() {
	}

	protected boolean onLongClick() {
		return false;
	}

	@Override
	protected void layout() {
		hotArea.x = x;
		hotArea.y = y;
		hotArea.width = width;
		hotArea.height = height;
	}
}
