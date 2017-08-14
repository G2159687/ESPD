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
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {

	{
		name = Messages.get(this,"name");
		spriteClass = GnollSprite.class;

		HP = HT = 12+(Dungeon.depth*Random.NormalIntRange(1, 3));
		defenseSkill = 4+(Math.round((Dungeon.depth)/2));

		EXP = 2;
		maxLvl = 8;

		if (Dungeon.isChallenged(Challenges.NO_HERBALISM))
		{
			loot = Generator.Category.SCROLL;
			lootChance = 0.75f;

			lootOther = Generator.Category.POTION;
			lootChanceOther = 0.8f;

			lootThird = Gold.class;
			lootChanceThird = 1f;
		} else {

			loot = Gold.class;
			lootChance = 0.5f;

			lootOther = new Meat();
			lootChanceOther = 0.5f; // by default, see die()
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2, 5+(Dungeon.depth));
	}

	@Override
	public int attackSkill(Char target) {
		return 11+(Dungeon.depth);
	}

	@Override
	public int dr() {
		return 2;
	}

	@Override
	public String description() {
		return Messages.get(this,"desc");
	}
}
