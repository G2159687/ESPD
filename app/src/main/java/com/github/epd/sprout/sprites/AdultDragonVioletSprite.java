
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.AdultDragonViolet;
import com.github.epd.sprout.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class AdultDragonVioletSprite extends MobSprite {

	private static final float DURATION = 2f;
	private Animation cast;

	public AdultDragonVioletSprite() {
		super();

		texture(Assets.DRAGON);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 1, 0, 1, 0, 1, 0);

		run = new Animation(6, false);
		run.frames(frames, 0, 2, 1);

		attack = new Animation(6, false);
		attack.frames(frames, 0, 2, 3, 4, 5);

		cast = attack.clone();

		die = new Animation(8, false);
		die.frames(frames, 0, 5, 6, 7, 8, 9, 9, 9);

		play(run.clone());
	}


	@Override
	public void zap(int cell) {

		turnTo(ch.pos, cell);
		play(zap);

		MagicMissile.poison(parent, ch.pos, cell, new Callback() {
			@Override
			public void call() {
				((AdultDragonViolet) ch).onZapComplete();
			}
		});
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}
}
