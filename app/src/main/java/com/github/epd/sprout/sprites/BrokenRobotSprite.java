
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.effects.Beam;
import com.watabou.noosa.TextureFilm;

public class BrokenRobotSprite extends MobSprite {

	private int attackPos;

	public BrokenRobotSprite() {
		super();

		texture(Assets.BROKENROBOT);

		TextureFilm frames = new TextureFilm(texture, 16, 18);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 1, 0, 1);

		run = new Animation(12, true);
		run.frames(frames, 2, 3, 4, 5, 6, 7);

		attack = new Animation(8, false);
		attack.frames(frames, 8, 9);

		die = new Animation(8, false);
		die.frames(frames, 10, 11, 12, 13);

		play(idle);
	}

	@Override
	public void attack(int pos) {
		attackPos = pos;
		super.attack(pos);
	}


	@Override
	public void onComplete(Animation anim) {
		super.onComplete(anim);

		if (anim == attack) {
			if (Dungeon.visible[ch.pos] || Dungeon.visible[attackPos]) {
				parent.add(new Beam.DeathRay(center(), DungeonTilemap
						.tileCenterToWorld(attackPos)));
			}
		}
	}
}
