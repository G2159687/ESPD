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

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.GreyOniSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class GreyOni extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = GreyOniSprite.class;
		state = SLEEPING;

		HP = HT = 500;
		defenseSkill = 35;

		EXP = 22;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(55, 115);
	}

	@Override
	public int attackSkill(Char target) {
		return 55;
	}

	@Override
	protected float attackDelay() {
		return 1.5f;
	}

	@Override
	public int dr() {
		return 32;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Amok.class);
		IMMUNITIES.add(Terror.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
