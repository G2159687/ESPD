
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.mobs.Shell;
import com.github.epd.sprout.effects.Lightning;
import com.watabou.noosa.TextureFilm;

public class ShellSprite extends MobSprite {

	public ShellSprite() {
		super();

		texture(Assets.SHELL);
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

		parent.add(new Lightning(ch.pos, pos, (Shell) ch));

		turnTo(ch.pos, pos);
		play(zap);
	}

}
