
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class GreyRatSprite extends MobSprite {

	public GreyRatSprite() {
		super();

		texture(Assets.RAT);

		TextureFilm frames = new TextureFilm(texture, 16, 15);

		idle = new Animation(2, true);
		idle.frames(frames, 48, 48, 48, 49);

		run = new Animation(10, true);
		run.frames(frames, 54, 55, 56, 57, 58);

		attack = new Animation(15, false);
		attack.frames(frames, 50, 51, 52, 53);

		die = new Animation(10, false);
		die.frames(frames, 59, 60, 61, 62);

		play(idle);
	}
}
