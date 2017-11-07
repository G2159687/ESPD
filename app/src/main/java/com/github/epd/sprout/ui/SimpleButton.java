
package com.github.epd.sprout.ui;

import com.watabou.input.Touchscreen.Touch;
import com.watabou.noosa.Image;
import com.watabou.noosa.TouchArea;
import com.watabou.noosa.ui.Component;

public class SimpleButton extends Component {

	private Image image;

	public SimpleButton(Image image) {
		super();

		this.image.copy(image);
		width = image.width;
		height = image.height;
	}

	@Override
	protected void createChildren() {
		image = new Image();
		add(image);

		add(new TouchArea(image) {
			@Override
			protected void onTouchDown(Touch touch) {
				image.brightness(1.2f);
			}

			@Override
			protected void onTouchUp(Touch touch) {
				image.brightness(1.0f);
			}

			@Override
			protected void onClick(Touch touch) {
				SimpleButton.this.onClick();
			}
		});
	}

	@Override
	protected void layout() {
		image.x = x;
		image.y = y;
	}

	protected void onClick() {
	}
}
