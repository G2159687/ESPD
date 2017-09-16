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
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.items.potions.PotionOfLiquidFlame;
import com.github.epd.sprout.items.wands.WandOfFirebolt;
import com.github.epd.sprout.items.weapon.enchantments.Fire;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SkeletonHand1Sprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class SkeletonHand1 extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = SkeletonHand1Sprite.class;

		HP = HT = 200;
		defenseSkill = 30;

		EXP = 10;
		maxLvl = 20;

		flying = true;

		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;

	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(10, 30);
	}

	@Override
	public int attackSkill(Char target) {
		return 25;
	}

	@Override
	public int dr() {
		return 15;
	}

	@Override
	protected boolean act() {
		boolean result = super.act();

		if (state == FLEEING && buff(Terror.class) == null && enemy != null
				&& enemySeen && enemy.buff(Burning.class) == null) {
			state = HUNTING;
		}
		return result;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(2) == 0) {
			if (enemy == Dungeon.hero) {
				Buff.affect(enemy, Burning.class).reignite(enemy);
				state = FLEEING;
			}
		}

		return damage;
	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(Fire.class);
		IMMUNITIES.add(WandOfFirebolt.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
