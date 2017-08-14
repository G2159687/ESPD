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
import com.github.epd.sprout.actors.buffs.Sleep;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.actors.mobs.npcs.Imp;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.GolemSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Golem extends Mob {

	{
		name = Messages.get(this,"name");
		spriteClass = GolemSprite.class;

		HP = HT = 85+(adj(0)*Random.NormalIntRange(4, 7));
		defenseSkill = 18+adj(1);

		EXP = 12;
		maxLvl = 22;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(20+adj(1), 60+adj(0));
	}

	@Override
	public int attackSkill(Char target) {
		return 28+adj(1);
	}

	@Override
	protected float attackDelay() {
		return 1.5f;
	}

	@Override
	public int dr() {
		return 12+adj(0);
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this,"def");
	}

	@Override
	public void die(Object cause) {
		Imp.Quest.process(this);

		super.die(cause);
	}

	@Override
	public String description() {
		return Messages.get(this,"desc");
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
		IMMUNITIES.add(Sleep.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
