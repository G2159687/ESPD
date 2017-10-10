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
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.artifacts.ChaliceOfBlood;

public class Regeneration extends Buff {

	private static final float REGENERATION_DELAY = 10;

	@Override
	public boolean act() {
		if (target.isAlive()) {

			ChaliceOfBlood.chaliceRegen regenBuff = Dungeon.hero.buff(ChaliceOfBlood.chaliceRegen.class);
			float EXTRA_REGEN;

			if (target.HP < target.HT && !((Hero) target).isStarving()) {

				target.HP += 1;

				if (regenBuff != null) {
					if ((!regenBuff.isCursed()) && regenBuff.level() > 10) {
						EXTRA_REGEN = (regenBuff.level() - 10) * target.HT * 0.0005f;
						target.HP += Math.min(Math.round(EXTRA_REGEN), target.HT - target.HP);
					}
				}

				if (target.HP == target.HT)
					((Hero) target).resting = false;
			}

			if (regenBuff != null) {
				if (regenBuff.isCursed())
					spend(REGENERATION_DELAY * 1.5f);
				else
					spend(Math.max(REGENERATION_DELAY - regenBuff.level(), 0.5f));
			} else

				spend(REGENERATION_DELAY);

		} else {

			diactivate();

		}

		return true;
	}
}
