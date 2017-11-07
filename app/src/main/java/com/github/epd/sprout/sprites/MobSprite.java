
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.actors.mobs.Mob;
import com.watabou.noosa.tweeners.AlphaTweener;
import com.watabou.noosa.tweeners.ScaleTweener;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class MobSprite extends CharSprite {

	private static final float FADE_TIME = 3f;
	private static final float FALL_TIME = 1f;

	@Override
	public void update() {
		sleeping = ch != null && ((Mob) ch).state == ((Mob) ch).SLEEPING;
		super.update();
	}

	@Override
	public void onComplete(Animation anim) {

		super.onComplete(anim);

		if (anim == die) {
			parent.add(new AlphaTweener(this, 0, FADE_TIME) {
				@Override
				protected void onComplete() {
					MobSprite.this.killAndErase();
					parent.erase(this);
				}
			});
		}
	}

	public void fall() {

		origin.set(width / 2, height - DungeonTilemap.SIZE / 2);
		angularSpeed = Random.Int(2) == 0 ? -720 : 720;

		parent.add(new ScaleTweener(this, new PointF(0, 0), FALL_TIME) {
			@Override
			protected void onComplete() {
				MobSprite.this.killAndErase();
				parent.erase(this);
			}

			@Override
			protected void updateValues(float progress) {
				super.updateValues(progress);
				am = 1 - progress;
			}
		});
	}
}
