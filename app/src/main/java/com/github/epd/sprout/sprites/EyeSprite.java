
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.effects.Beam;
import com.watabou.noosa.TextureFilm;

public class EyeSprite extends MobSprite {

	private int attackPos;

	public EyeSprite() {
		super();

		texture(Assets.EYE);

		TextureFilm frames = new TextureFilm(texture, 16, 18);

		idle = new Animation(8, true);
		idle.frames(frames, 0, 1, 2);

		run = new Animation(12, true);
		run.frames(frames, 5, 6);

		attack = new Animation(8, false);
		attack.frames(frames, 4, 3);

		die = new Animation(8, false);
		die.frames(frames, 7, 8, 9);

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
