
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class GullinSprite extends MobSprite {

	public GullinSprite() {
		super();

		texture(Assets.GULLIN);

		TextureFilm frames = new TextureFilm(texture, 12, 16);

		idle = new Animation(12, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 1, 1);

		run = new Animation(15, true);
		run.frames(frames, 0, 2, 3, 4, 5);

		attack = new Animation(15, false);
		attack.frames(frames, 0, 5, 6);

		die = new Animation(12, false);
		die.frames(frames, 7, 8, 9, 10, 10, 11, 12, 13, 14, 15, 16, 17);

		play(idle);
	}

}
