
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;

public class OtilukeNPCSprite extends MobSprite {

	public OtilukeNPCSprite() {
		super();

		texture(Assets.OTILUKE);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(10, true);
		idle.frames(frames, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2, 3,
				3, 3, 3, 3, 3, 2, 1);

		run = new Animation(20, true);
		run.frames(frames, 0);

		die = new Animation(20, false);
		die.frames(frames, 0);

		play(idle);
	}


}
