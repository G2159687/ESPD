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
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.items.ClusterBomb;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.SeekingClusterBombSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class SeekingClusterBomb extends Mob {


	{
		name = Messages.get(this, "name");
		spriteClass = SeekingClusterBombSprite.class;
		hostile = false;
		state = HUNTING;
		HP = HT = 10;
		defenseSkill = 3;
	}


	private static final float SPAWN_DELAY = 0.1f;

	@Override
	public int dr() {
		return 1;
	}


	@Override
	public int attackProc(Char enemy, int damage) {
		int dmg = super.attackProc(enemy, damage);

		ClusterBomb cbomb = new ClusterBomb();
		for (int n : PathFinder.NEIGHBOURS8DIST2) {
			int c = pos + n;
			if (Random.Int(3) == 0) {
				cbomb.explode(c);
				//spend(2f);
			}
		}

		yell(Messages.get(this, "atk"));

		destroy();
		sprite.die();

		return dmg;
	}

	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile && Level.fieldOfView[mob.pos]) {
					enemies.add(mob);
				}
			}

			enemy = enemies.size() > 0 ? Random.element(enemies) : null;
		}

		return enemy;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}


	public void interact() {

		int curPos = pos;

		moveSprite(pos, Dungeon.hero.pos);
		move(Dungeon.hero.pos);

		Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
		Dungeon.hero.move(curPos);

		Dungeon.hero.spend(1 / Dungeon.hero.speed());
		Dungeon.hero.busy();
	}


	public static SeekingClusterBomb spawnAt(int pos) {

		SeekingClusterBomb b = new SeekingClusterBomb();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}


	@Override
	public void die(Object cause) {
		ClusterBomb cbomb = new ClusterBomb();
		cbomb.explode(pos);

		super.die(cause);
	}


	@Override
	public void beckon(int cell) {
	}


	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Death.class);
		RESISTANCES.add(Leech.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Terror.class);
		IMMUNITIES.add(ToxicGas.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
