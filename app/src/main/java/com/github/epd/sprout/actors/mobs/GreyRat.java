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

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.CorruptGas;
import com.github.epd.sprout.actors.blobs.StenchGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Chill;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Sleep;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.GreyRatSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class GreyRat extends Mob {


	private static final float SPAWN_DELAY = 2f;

	{
		name = Messages.get(this, "name");
		spriteClass = GreyRatSprite.class;

		HP = HT = 12 + (Dungeon.depth * Random.NormalIntRange(1, 3));
		defenseSkill = 3 + (Math.round((Dungeon.depth) / 2));

		if (Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
			loot = Generator.Category.SEED;
			lootChance = 0.75f;

			lootOther = new Meat();
			lootChanceOther = 1f;
		} else {
			loot = new Meat();
			lootChance = 0.5f;


			lootOther = Generator.Category.MUSHROOM;
			lootChanceOther = 0.25f;
		}

	}


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2, 5 + Dungeon.depth);
	}

	@Override
	public int attackSkill(Char target) {
		return 5 + Dungeon.depth;
	}

	@Override
	public int dr() {
		return 2;
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

	public static GreyRat spawnAt(int pos) {

		GreyRat b = new GreyRat();

		b.pos = pos;
		b.state = b.HUNTING;
		GameScene.add(b, SPAWN_DELAY);

		return b;

	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Burning.class);
		RESISTANCES.add(Vertigo.class);
		RESISTANCES.add(Poison.class);
		RESISTANCES.add(StenchGas.class);
		RESISTANCES.add(CorruptGas.class);
		RESISTANCES.add(Chill.class);
		RESISTANCES.add(Paralysis.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

}
