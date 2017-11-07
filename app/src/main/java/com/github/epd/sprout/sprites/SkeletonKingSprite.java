
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class SkeletonKingSprite extends MobSprite {

	public SkeletonKingSprite() {
		super();

		texture(Assets.SKELETONKING);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(5, true);
		idle.frames(frames, 0, 1, 2);

		run = new Animation(6, true);
		run.frames(frames, 3, 4);

		attack = new Animation(5, false);
		attack.frames(frames, 5, 6, 7);

		die = new Animation(5, false);
		die.frames(frames, 8, 9, 10, 11, 12, 13);

		play(idle);
	}

}
