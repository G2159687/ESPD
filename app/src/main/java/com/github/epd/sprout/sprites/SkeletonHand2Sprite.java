
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class SkeletonHand2Sprite extends MobSprite {

	public SkeletonHand2Sprite() {
		super();

		texture(Assets.SKELETONHAND1);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 1);

		run = new Animation(4, true);
		run.frames(frames, 0, 1);

		attack = new Animation(5, false);
		attack.frames(frames, 2, 3, 4, 5);

		die = new Animation(5, false);
		die.frames(frames, 6, 7, 8, 9);

		play(idle);
	}

}
