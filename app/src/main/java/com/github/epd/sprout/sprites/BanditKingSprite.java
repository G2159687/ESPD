
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.Char;
import com.watabou.noosa.TextureFilm;

public class BanditKingSprite extends MobSprite {

	public BanditKingSprite() {
		super();

		texture(Assets.BANDITKING);
		TextureFilm film = new TextureFilm(texture, 12, 13);

		idle = new Animation(1, true);
		idle.frames(film, 0, 0, 0, 1, 0, 0, 0, 0, 1);

		run = new Animation(15, true);
		run.frames(film, 0, 0, 2, 3, 3, 4);

		die = new Animation(10, false);
		die.frames(film, 1, 1, 5, 5, 5);

		attack = new Animation(12, false);
		attack.frames(film, 10, 11, 12, 0);

		idle();
	}

	@Override
	public void link(Char ch) {
		super.link(ch);
		add(State.LEVITATING);
	}

	@Override
	public void die() {
		super.die();
		remove(State.LEVITATING);
	}
}
