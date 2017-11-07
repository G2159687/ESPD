
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.Shaman;
import com.github.epd.sprout.effects.Lightning;
import com.watabou.noosa.TextureFilm;

public class ShamanSprite extends MobSprite {

	public ShamanSprite() {
		super();

		texture(Assets.SHAMAN);

		TextureFilm frames = new TextureFilm(texture, 12, 15);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 1, 0, 0, 1, 1);

		run = new Animation(12, true);
		run.frames(frames, 4, 5, 6, 7);

		attack = new Animation(12, false);
		attack.frames(frames, 2, 3, 0);

		zap = attack.clone();

		die = new Animation(12, false);
		die.frames(frames, 8, 9, 10);

		play(idle);
	}

	@Override
	public void zap(int pos) {

		parent.add(new Lightning(ch.pos, pos, (Shaman) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}
}
