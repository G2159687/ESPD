
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class BrownBatSprite extends MobSprite {

	public BrownBatSprite() {
		super();

		texture(Assets.BAT);

		TextureFilm frames = new TextureFilm(texture, 15, 15);

		idle = new Animation(8, true);
		idle.frames(frames, 16, 17);

		run = new Animation(12, true);
		run.frames(frames, 16, 17);

		attack = new Animation(12, false);
		attack.frames(frames, 18, 19, 16, 17);

		die = new Animation(12, false);
		die.frames(frames, 20, 21, 22);

		play(idle);
	}
}
