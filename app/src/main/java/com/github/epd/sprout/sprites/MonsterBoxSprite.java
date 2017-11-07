
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class MonsterBoxSprite extends MobSprite {

	public MonsterBoxSprite() {
		super();

		texture(Assets.MONSTERBOX);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(5, true);
		idle.frames(frames, 0, 0, 0, 1, 1);

		run = new Animation(10, true);
		run.frames(frames, 0, 1, 2, 3, 3, 2, 1);

		attack = new Animation(10, false);
		attack.frames(frames, 0, 4, 5, 6);

		die = new Animation(5, false);
		die.frames(frames, 7, 8, 9);

		play(idle);
	}

	@Override
	public int blood() {
		return 0xFFcb9700;
	}
}
