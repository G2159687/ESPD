
package com.github.epd.sprout.ui;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.sprites.CharSprite;

public class HealthIndicator extends HealthBar {

	public static HealthIndicator instance;

	private Char target;

	public HealthIndicator() {
		super();

		instance = this;
	}

	@Override
	public void update() {
		super.update();

		if (target != null && target.isAlive() && target.sprite.visible) {
			CharSprite sprite = target.sprite;
			width = sprite.width;
			x = sprite.x;
			y = sprite.y - 3;
			level(target);
			visible = true;
		} else {
			visible = false;
		}
	}

	public void target(Char ch) {
		if (ch != null && ch.isAlive()) {
			target = ch;
		} else {
			target = null;
		}
	}

	public Char target() {
		return target;
	}
}