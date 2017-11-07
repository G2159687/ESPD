
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.Camera;
import com.watabou.noosa.TextureFilm;

public class RottingFistSprite extends MobSprite {

	private static final float FALL_SPEED = 64;

	public RottingFistSprite() {
		super();

		texture(Assets.ROTTING);

		TextureFilm frames = new TextureFilm(texture, 24, 17);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 1);

		run = new Animation(3, true);
		run.frames(frames, 0, 1);

		attack = new Animation(2, false);
		attack.frames(frames, 0);

		die = new Animation(10, false);
		die.frames(frames, 0, 2, 3, 4);

		play(idle);
	}

	@Override
	public void attack(int cell) {
		super.attack(cell);

		speed.set(0, -FALL_SPEED);
		acc.set(0, FALL_SPEED * 4);
	}

	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);
		if (anim == attack) {
			speed.set(0);
			acc.set(0);
			place(ch.pos);

			Camera.main.shake(4, 0.2f);
		}
	}
}
