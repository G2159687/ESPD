
package com.github.epd.sprout.effects;

import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.ui.Icons;
import com.watabou.noosa.Game;
import com.watabou.noosa.Image;
import com.watabou.utils.Random;

public class EmoIcon extends Image {

	protected float maxSize = 2;
	protected float timeScale = 1;

	protected boolean growing = true;

	protected CharSprite owner;

	public EmoIcon(CharSprite owner) {
		super();

		this.owner = owner;
		GameScene.add(this);
	}

	@Override
	public void update() {
		super.update();

		if (visible) {
			if (growing) {
				scale.set(Math.min(scale.x + Game.elapsed * timeScale, maxSize));
				if (scale.x >= maxSize) {
					growing = false;
				}
			} else {
				scale.set(Math.max(scale.x - Game.elapsed * timeScale, 1f));
				if (scale.x <= 1) {
					growing = true;
				}
			}

			x = owner.x + owner.width - width / 2;
			y = owner.y - height;
		}
	}

	public static class Sleep extends EmoIcon {

		public Sleep(CharSprite owner) {

			super(owner);

			copy(Icons.get(Icons.SLEEP));

			maxSize = 1.2f;
			timeScale = 0.5f;

			origin.set(width / 2, height / 2);
			scale.set(Random.Float(1, maxSize));
		}
	}

	public static class Alert extends EmoIcon {

		public Alert(CharSprite owner) {

			super(owner);

			copy(Icons.get(Icons.ALERT));

			maxSize = 1.3f;
			timeScale = 2;

			origin.set(2.5f, height - 2.5f);
			scale.set(Random.Float(1, maxSize));
		}
	}

}
