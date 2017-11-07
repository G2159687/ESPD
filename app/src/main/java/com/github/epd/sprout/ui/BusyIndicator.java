
package com.github.epd.sprout.ui;

import com.github.epd.sprout.Dungeon;
import com.watabou.noosa.Image;

public class BusyIndicator extends Image {

	public BusyIndicator() {
		super();
		copy(Icons.BUSY.get());

		origin.set(width / 2, height / 2);
		angularSpeed = 720;
	}

	@Override
	public void update() {
		super.update();
		visible = Dungeon.hero.isAlive() && !Dungeon.hero.ready;
	}
}
