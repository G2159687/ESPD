
package com.watabou.noosa;

import com.watabou.input.Touchscreen;
import com.watabou.input.Touchscreen.Touch;
import com.watabou.utils.Signal;

public class TouchArea extends Visual implements Signal.Listener<Touchscreen.Touch> {

	// Its target can be toucharea itself
	public Visual target;

	protected Touchscreen.Touch touch = null;

	//if true, this TouchArea will always block input, even when it is inactive
	public boolean blockWhenInactive = false;

	public TouchArea(Visual target) {
		super(0, 0, 0, 0);
		this.target = target;

		Touchscreen.event.add(this);
	}

	public TouchArea(float x, float y, float width, float height) {
		super(x, y, width, height);
		this.target = this;

		visible = false;

		Touchscreen.event.add(this);
	}

	@Override
	public void onSignal(Touch touch) {

		boolean hit = touch != null && target.overlapsScreenPoint((int) touch.current.x, (int) touch.current.y);

		if (!isActive()) {
			if (hit && blockWhenInactive) Touchscreen.event.cancel();
			return;
		}

		if (hit) {

			if (touch.down || touch == this.touch) Touchscreen.event.cancel();

			if (touch.down) {

				if (this.touch == null) {
					this.touch = touch;
				}
				onTouchDown(touch);

			} else {

				onTouchUp(touch);

				if (this.touch == touch) {
					this.touch = null;
					onClick(touch);
				}

			}

		} else {

			if (touch == null && this.touch != null) {
				onDrag(this.touch);
			} else if (this.touch != null && !touch.down) {
				onTouchUp(touch);
				this.touch = null;
			}

		}
	}

	protected void onTouchDown(Touch touch) {
	}

	protected void onTouchUp(Touch touch) {
	}

	protected void onClick(Touch touch) {
	}

	protected void onDrag(Touch touch) {
	}

	public void reset() {
		touch = null;
	}

	@Override
	public void destroy() {
		Touchscreen.event.remove(this);
		super.destroy();
	}
}
