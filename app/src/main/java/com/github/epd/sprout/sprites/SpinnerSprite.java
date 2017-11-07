
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class SpinnerSprite extends MobSprite {

	public SpinnerSprite() {
		super();

		texture(Assets.SPINNER);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 1, 0, 1);

		run = new Animation(15, true);
		run.frames(frames, 0, 2, 0, 3);

		attack = new Animation(12, false);
		attack.frames(frames, 0, 4, 5, 0);

		die = new Animation(12, false);
		die.frames(frames, 6, 7, 8, 9);

		play(idle);
	}

	@Override
	public int blood() {
		return 0xFFBFE5B8;
	}
}
