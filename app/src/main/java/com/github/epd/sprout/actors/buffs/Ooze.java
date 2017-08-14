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
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Ooze extends Buff {

	@Override
	public int icon() {
		return BuffIndicator.OOZE;
	}

	@Override
	public String toString() {
		return Messages.get(this,"name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			if (Dungeon.depth > 4)
				target.damage(Dungeon.depth / 5, this);
			else if (Random.Int(2) == 0)
				target.damage(1, this);
			if (!target.isAlive() && target == Dungeon.hero) {
				Dungeon.fail(ResultDescriptions.OOZE);
				GLog.n(Messages.get(this,"die"), toString());
			}
			spend(TICK);
		}
		if (Level.water[target.pos]) {
			detach();
		}
		return true;
	}
}
