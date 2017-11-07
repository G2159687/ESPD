
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.LitTower;
import com.github.epd.sprout.effects.Lightning;
import com.watabou.noosa.TextureFilm;

public class LitTowerSprite extends MobSprite {

	public LitTowerSprite() {
		super();

		texture(Assets.LITTOWER);
		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0);

		run = idle.clone();
		die = idle.clone();
		attack = idle.clone();

		zap = attack.clone();

		idle();
	}

	@Override
	public void zap(int pos) {

		parent.add(new Lightning(ch.pos, pos, (LitTower) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}

}
