
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.weapon.missiles.Wave;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;

public class CrabKingSprite extends MobSprite {

	private static final float DURATION = 2f;
	private Animation cast;

	public CrabKingSprite() {
		super();

		texture(Assets.CRABKING);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 1, 2, 3, 10, 11, 12);

		run = new Animation(15, false);
		run.frames(frames, 4, 5, 6, 10, 11, 12);

		attack = new Animation(15, false);
		attack.frames(frames, 7, 8, 9);

		cast = attack.clone();

		die = new Animation(8, false);
		die.frames(frames, 8, 9, 10, 10, 10, 10, 10, 10);

		play(run.clone());
	}

	@Override
	public void move(int from, int to) {

		place(to);

		play(run);
		turnTo(from, to);

		isMoving = true;

		if (Level.water[to]) {
			GameScene.ripple(to);
		}
	}

	@Override
	public void attack(int cell) {
		if (!Dungeon.level.adjacent(cell, ch.pos)) {
			//Char enemy = Actor.findChar(cell);
			((MissileSprite) parent.recycle(MissileSprite.class)).reset(ch.pos,
					cell, new Wave(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					});


			play(cast);
			turnTo(ch.pos, cell);

		} else {

			super.attack(cell);

		}
	}

	@Override
	public void onComplete(Animation anim) {
		if (anim == run) {
			synchronized (this) {
				isMoving = false;
				idle();
				notifyAll();
			}
		} else {
			super.onComplete(anim);
		}
	}
}
