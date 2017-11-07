
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.Otiluke;
import com.github.epd.sprout.effects.MagicMissile;
import com.watabou.noosa.TextureFilm;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;

public class OtilukeSprite extends MobSprite {

	public OtilukeSprite() {
		super();

		texture(Assets.OTILUKESTONE);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 3, 0, 0, 3, 3);

		run = new Animation(15, true);
		run.frames(frames, 0, 1, 2, 0);

		attack = new Animation(12, false);
		attack.frames(frames, 0, 1, 4, 4, 4);

		zap = attack.clone();

		die = new Animation(15, false);
		die.frames(frames, 0, 5, 6, 7, 8, 7);

		play(idle);
	}

	@Override
	public void zap(int cell) {

		turnTo(ch.pos, cell);
		play(zap);

		MagicMissile.shadow(parent, ch.pos, cell, new Callback() {
			@Override
			public void call() {
				((Otiluke) ch).onZapComplete();
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
