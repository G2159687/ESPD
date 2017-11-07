
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.DM300;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class DM300Sprite extends MobSprite {

	public DM300Sprite() {
		super();

		texture(Assets.DM300);

		TextureFilm frames = new TextureFilm(texture, 22, 20);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 1);

		run = new Animation(10, true);
		run.frames(frames, 2, 3);

		attack = new Animation(15, false);
		attack.frames(frames, 4, 5, 6);

		die = new Animation(20, false);
		die.frames(frames, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 0, 7, 8);

		play(idle);
	}

	@Override
	public void zap(int pos) {

		parent.add(new Lightning(ch.pos, pos, (DM300) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}

	@Override
	public void onComplete(Animation anim) {

		super.onComplete(anim);

		if (anim == die) {
			emitter().burst(Speck.factory(Speck.WOOL), 15);
		}
	}

	@Override
	public int blood() {
		return 0xFFFFFF88;
	}
}
