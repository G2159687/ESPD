
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Slow;
import com.github.epd.sprout.items.weapon.missiles.Dart;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class ThiefKingSprite extends MobSprite {

	private static final float DURATION = 2f;
	private Animation cast;


	public ThiefKingSprite() {
		super();

		texture(Assets.THIEFKING);

		TextureFilm frames = new TextureFilm(texture, 16, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 0);

		run = new Animation(15, false);
		run.frames(frames, 1, 2, 3, 4, 5);

		attack = new Animation(15, false);
		attack.frames(frames, 6, 7, 8);

		cast = new Animation(15, false);
		cast.frames(frames, 9, 10);

		die = new Animation(8, false);
		die.frames(frames, 11, 12, 13, 14);

		play(run.clone());
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
			Char enemy = Actor.findChar(cell);
			((MissileSprite) parent.recycle(MissileSprite.class)).reset(ch.pos,
					cell, new Dart(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
					});


			if (Random.Int(10) == 0) {
				Buff.affect(enemy, Slow.class, Slow.duration(enemy) / 2);
			}

			if (Random.Int(10) == 0) {
				Buff.affect(enemy, Poison.class).set(Random.Int(7, 9) * Poison.durationFactor(enemy));
			}

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
