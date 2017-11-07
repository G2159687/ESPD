
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class RatKingSprite extends MobSprite {

	public boolean festive;

	public RatKingSprite() {
		super();

		// Sometimes rat king just want to be a bit festive!
		festive = (Random.Int(5) == 0);

		final int c = festive ? 8 : 0;

		texture(Assets.RATKING);

		TextureFilm frames = new TextureFilm(texture, 16, 17);

		idle = new Animation(2, true);
		idle.frames(frames, c + 0, c + 0, c + 0, c + 1);

		run = new Animation(10, true);
		run.frames(frames, c + 2, c + 3, c + 4, c + 5, c + 6);

		attack = new Animation(15, false);
		attack.frames(frames, c + 0);

		die = new Animation(10, false);
		die.frames(frames, c + 0);

		play(idle);
	}
}
