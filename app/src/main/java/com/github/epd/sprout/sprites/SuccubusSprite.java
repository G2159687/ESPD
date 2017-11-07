
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;

public class SuccubusSprite extends MobSprite {

	public SuccubusSprite() {
		super();

		texture(Assets.SUCCUBUS);

		TextureFilm frames = new TextureFilm(texture, 12, 15);

		idle = new Animation(8, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 2,
				2, 2, 1);

		run = new Animation(15, true);
		run.frames(frames, 3, 4, 5, 6, 7, 8);

		attack = new Animation(12, false);
		attack.frames(frames, 9, 10, 11);

		die = new Animation(10, false);
		die.frames(frames, 12);

		play(idle);
	}

	@Override
	public void die() {
		super.die();
		emitter().burst(Speck.factory(Speck.HEART), 6);
		emitter().burst(ShadowParticle.UP, 8);
	}
}
