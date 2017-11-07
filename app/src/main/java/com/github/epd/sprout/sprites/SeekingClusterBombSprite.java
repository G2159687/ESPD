
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class SeekingClusterBombSprite extends MobSprite {

	public SeekingClusterBombSprite() {
		super();

		texture(Assets.SEEKINGBOMB);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(5, true);
		idle.frames(frames, 16, 16, 16, 16);

		run = new Animation(15, true);
		run.frames(frames, 17, 18, 19, 20);

		attack = new Animation(12, false);
		attack.frames(frames, 17, 18, 19);

		die = new Animation(12, false);
		die.frames(frames, 20, 20, 20, 20);

		play(idle);
	}

	@Override
	public int blood() {
		return 0xFFFFEA80;
	}
}
