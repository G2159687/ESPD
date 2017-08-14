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
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class Levitation extends FlavourBuff {

	public static final float DURATION = 20f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {
			target.flying = true;
			Buff.detach(target, Roots.class);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		target.flying = false;
		Dungeon.level.press(target.pos, target);
		super.detach();
	}

	@Override
	public int icon() {
		return BuffIndicator.LEVITATION;
	}

	@Override
	public String toString() {
		return Messages.get(this,"name");
	}

	@Override
	public String desc() {
		return  Messages.get(this,"desc", dispTurns());
	}
}
