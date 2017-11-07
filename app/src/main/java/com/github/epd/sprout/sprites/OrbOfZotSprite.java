
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.DungeonTilemap;
import com.github.epd.sprout.effects.Beam;
import com.watabou.noosa.TextureFilm;

public class OrbOfZotSprite extends MobSprite {

	private int attackPos;

	public OrbOfZotSprite() {
		super();

		texture(Assets.ORBOFZOT);

		TextureFilm frames = new TextureFilm(texture, 16, 18);

		idle = new Animation(2, true);
		idle.frames(frames, 1, 2, 3, 4);

		run = new Animation(12, true);
		run.frames(frames, 2, 3, 4);

		attack = new Animation(8, false);
		attack.frames(frames, 1, 5);

		die = new Animation(8, false);
		die.frames(frames, 1, 0, 6);

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
