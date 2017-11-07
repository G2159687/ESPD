
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class SokobanSheepSwitchSprite extends MobSprite {

	public SokobanSheepSwitchSprite() {
		super();

		texture(Assets.SOKOBANSHEEP);

		TextureFilm frames = new TextureFilm(texture, 16, 15);

		idle = new Animation(8, true);
		idle.frames(frames, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 8, 9, 10, 11, 8);

		run = idle.clone();
		attack = idle.clone();

		die = new Animation(20, false);
		die.frames(frames, 8);

		play(idle);
		curFrame = Random.Int(curAnim.frames.length);
	}
}
