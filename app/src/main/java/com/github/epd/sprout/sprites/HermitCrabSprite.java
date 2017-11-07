
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.HermitCrab;
import com.github.epd.sprout.effects.Lightning;
import com.watabou.noosa.TextureFilm;

public class HermitCrabSprite extends MobSprite {

	public HermitCrabSprite() {
		super();

		texture(Assets.HERMITCRAB);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

		run = new Animation(12, true);
		run.frames(frames, 2, 3, 4, 5, 6);

		attack = new Animation(12, false);
		attack.frames(frames, 6, 7, 8, 9);

		zap = attack.clone();

		die = new Animation(12, false);
		die.frames(frames, 10, 11, 12, 13);

		play(idle);
	}

	@Override
	public void zap(int pos) {

		parent.add(new Lightning(ch.pos, pos, (HermitCrab) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}
}
