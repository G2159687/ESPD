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
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.SpectralRatSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class SpectralRat extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 1f;

	private static final String TXT_SHADOWBOLT_KILLED = Messages.get(SpectralRat.class, "kill");

	private static final float SPAWN_DELAY = 2f;

	{
		name = Messages.get(this, "name");
		spriteClass = SpectralRatSprite.class;
		baseSpeed = 4f;

		HP = HT = 80 + (Dungeon.depth * Random.NormalIntRange(2, 5));
		defenseSkill = 2;

		loot = new Meat();
		lootChance = 0.5f;

		properties.add(Property.UNDEAD);
		properties.add(Property.EVIL);

	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(Dungeon.depth / 2, Dungeon.depth);
	}

	@Override
	public int attackSkill(Char target) {
		return 50;
	}

	@Override
	public int dr() {
		return 10;
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Level.adjacent(pos, enemy.pos)) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Level.fieldOfView[pos]
					|| Level.fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			} else {
				zap();
			}

			return !visible;
		}
	}

	private void zap() {
		spend(TIME_TO_ZAP);

		if (hit(this, enemy, true)) {
			if (enemy == Dungeon.hero && Random.Int(5) == 0) {
				Buff.prolong(enemy, Weakness.class, Weakness.duration(enemy));
			}

			int dmg = Random.Int(20, 45);
			enemy.damage(dmg, this);

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail(Utils.format(ResultDescriptions.MOB,
						Utils.indefinite(name)));
				GLog.n(TXT_SHADOWBOLT_KILLED, name);
			}
		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}
	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void call() {
		next();
	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}

	public static void spawnAroundChance(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null && Random.Float() < 0.75f) {
				spawnAt(cell);
			}
		}
	}

	public static SpectralRat spawnAt(int pos) {

		SpectralRat b = new SpectralRat();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}

}
