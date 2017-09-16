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

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;

public class MagicalSleep extends Buff {

	private static final float STEP = 1f;
	public static final float SWS = 1.5f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)
				&& !target.immunities().contains(Sleep.class)) {

			if (target instanceof Hero)
				if (target.HP >= target.HT) {
					GLog.i(Messages.get(this, "nosleep"));
					detach();
					return true;
				} else {
					GLog.i(Messages.get(this, "sleep"));
				}
			else if (target instanceof Mob)
				((Mob) target).state = ((Mob) target).SLEEPING;

			target.paralysed++;

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean act() {
		if (target instanceof Hero) {
			target.HP = Math.min(target.HP + 1, target.HT);
			((Hero) target).resting = true;
			if (target.HP == target.HT) {
				GLog.p(Messages.get(this, "wakeup"));
				detach();
			}
		}
		spend(STEP);
		return true;
	}

	@Override
	public void detach() {
		if (target.paralysed > 0)
			target.paralysed--;
		if (target instanceof Hero)
			((Hero) target).resting = false;
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.MAGIC_SLEEP;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}