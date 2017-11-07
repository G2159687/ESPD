
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.Speck;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.particles.Emitter;

public class FetidRatSprite extends MobSprite {

	private Emitter cloud;

	public FetidRatSprite() {
		super();

		texture(Assets.RAT);

		TextureFilm frames = new TextureFilm(texture, 16, 15);

		idle = new Animation(2, true);
		idle.frames(frames, 32, 32, 32, 33);

		run = new Animation(10, true);
		run.frames(frames, 38, 39, 40, 41, 42);

		attack = new Animation(15, false);
		attack.frames(frames, 34, 35, 36, 37, 32);

		die = new Animation(10, false);
		die.frames(frames, 43, 44, 45, 46);

		play(idle);
	}

	@Override
	public void link(Char ch) {
		super.link(ch);

		if (cloud == null) {
			cloud = emitter();
			cloud.pour(Speck.factory(Speck.STENCH), 0.7f);
		}
	}

	@Override
	public void update() {

		super.update();

		if (cloud != null) {
			cloud.visible = visible;
		}
	}

	@Override
	public void die() {
		super.die();

		if (cloud != null) {
			cloud.on = false;
		}
	}
}
