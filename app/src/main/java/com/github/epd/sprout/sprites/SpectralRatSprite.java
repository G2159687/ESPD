
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.SpectralRat;
import com.github.epd.sprout.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class SpectralRatSprite extends MobSprite {

	public SpectralRatSprite() {
		super();

		texture(Assets.SPECTRALRAT);

		TextureFilm frames = new TextureFilm(texture, 16, 15);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 1);

		run = new Animation(10, true);
		run.frames(frames, 6, 7, 8, 9, 10);

		attack = new Animation(15, false);
		attack.frames(frames, 2, 3, 4, 5, 0);

		zap = attack.clone();

		die = new Animation(10, false);
		die.frames(frames, 11, 12, 13, 14);

		play(idle);
	}

	@Override
	public void zap(int cell) {

		turnTo(ch.pos, cell);
		play(zap);

		MagicMissile.shadow(parent, ch.pos, cell, new Callback() {
			@Override
			public void call() {
				((SpectralRat) ch).onZapComplete();
			}
		});
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public void onComplete(Animation anim) {
		if (anim == zap) {
			idle();
		}
		super.onComplete(anim);
	}
}
