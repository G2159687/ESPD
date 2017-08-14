/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.sprites;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Slow;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.items.weapon.missiles.Shuriken;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class TenguSprite extends MobSprite {

	private static final float DURATION = 2f;
	private Animation cast;

	public TenguSprite() {
		super();

		texture(Assets.TENGU);

		TextureFilm frames = new TextureFilm(texture, 14, 16);

		idle = new Animation(2, true);
		idle.frames(frames, 0, 0, 0, 1);

		run = new Animation(15, false);
		run.frames(frames, 2, 3, 4, 5, 0);

		attack = new Animation(15, false);
		attack.frames(frames, 6, 7, 7, 0);

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
		if (!Level.adjacent(cell, ch.pos)) {
			Char enemy = Actor.findChar(cell);
				  ((MissileSprite) parent.recycle(MissileSprite.class)).reset(ch.pos,
					cell, new Shuriken(), new Callback() {
						@Override
						public void call() {
							ch.onAttackComplete();
						}
				});
		 	  
		  		if(Random.Int(5)==0){
		  				Buff.affect(enemy, Burning.class).reignite(enemy);
		  				enemy.sprite.emitter().burst(FlameParticle.FACTORY, 5);
		  			}
		  		if(Random.Int(10)==0){
		  			Buff.affect(enemy, Slow.class, Slow.duration(enemy) / 2);
		  		}
		  				  		
		  		if(Random.Int(20)==0){
		  		       Buff.prolong(enemy, Paralysis.class, DURATION);
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
			synchronized (this){
				isMoving = false;
				idle();
				notifyAll();
			}
		} else {
			super.onComplete(anim);
		}
	}
}
