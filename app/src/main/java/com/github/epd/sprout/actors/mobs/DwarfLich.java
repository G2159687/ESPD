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

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.potions.PotionOfHealing;
import com.github.epd.sprout.items.weapon.enchantments.Leech;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.DwarfLichSprite;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.HashSet;

public class DwarfLich extends Mob {

	private static final float SPAWN_DELAY = 2f;

	{
		name = Messages.get(this, "name");
		spriteClass = DwarfLichSprite.class;

		HP = HT = 100 + (adj(0) * Random.NormalIntRange(7, 5));
		defenseSkill = 24 + adj(1);

		EXP = 14;

		loot = new PotionOfHealing();
		lootChance = 0.2f;

		lootOther = new Blackberry();
		lootChanceOther = 0.333f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20, 32);
	}

	@Override
	public int attackSkill(Char target) {
		return 36 + adj(1);
	}

	@Override
	public int dr() {
		return 16 + adj(0);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		Ballistica attack = new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT);
		return !Level.adjacent(pos, enemy.pos) && attack.collisionPos == enemy.pos;
	}

	@Override
	protected boolean getCloser(int target) {
		if (state == HUNTING) {
			return enemySeen && getFurther(target);
		} else {
			return super.getCloser(target);
		}
	}


	public static void spawnAround(int pos) {
		for (int n : PathFinder.NEIGHBOURS4) {
			int cell = pos + n;
			if (Level.passable[cell] && Actor.findChar(cell) == null) {
				spawnAt(cell);
			}
		}
	}

	public static DwarfLich spawnAt(int pos) {

		DwarfLich d = new DwarfLich();

		d.pos = pos;
		d.state = d.HUNTING;
		GameScene.add(d, SPAWN_DELAY);

		return d;

	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Leech.class);
		RESISTANCES.add(Poison.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
