
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.items.Item;
import com.watabou.noosa.tweeners.PosTweener;
import com.watabou.noosa.tweeners.Tweener;
import com.watabou.utils.Callback;
import com.watabou.utils.PointF;

public class MissileSprite extends ItemSprite implements Tweener.Listener {

	private static final float SPEED = 240f;

	private Callback callback;

	public MissileSprite() {
		super();
		originToCenter();
	}

	public void reset(int from, int to, Item item, Callback listener) {
		if (item == null) {
			reset(from, to, 0, null, listener);
		} else {
			reset(from, to, item.image(), item.glowing(), listener);
		}
	}

	public void reset(int from, int to, int image, Glowing glowing,
	                  Callback listener) {
		revive();

		view(image, glowing);

		this.callback = listener;

		point(DungeonTilemap.tileToWorld(from));
		PointF dest = DungeonTilemap.tileToWorld(to);

		PointF d = PointF.diff(dest, point());
		speed.set(d).normalize().scale(SPEED);

		if (image == ItemSpriteSheet.DART
				|| image == ItemSpriteSheet.INCENDIARY_DART
				|| image == ItemSpriteSheet.CURARE_DART
				|| image == ItemSpriteSheet.JAVELIN) {

			angularSpeed = 0;
			angle = 135 - (float) (Math.atan2(d.x, d.y) / 3.1415926 * 180);

		} else if (image == ItemSpriteSheet.WAVE
				|| image == ItemSpriteSheet.SKULLWEP) {

			angularSpeed = 0;
			angle = 90 - (float) (Math.atan2(d.x, d.y) / 3.1415926 * 180);

		} else {

			angularSpeed = image == 15 || image == 106 ? 1440 : 720;

		}

		PosTweener tweener = new PosTweener(this, dest, d.length() / SPEED);
		tweener.listener = this;
		parent.add(tweener);
	}

	@Override
	public void onComplete(Tweener tweener) {
		kill();
		if (callback != null) {
			callback.call();
		}
	}
}
