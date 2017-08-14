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

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ShieldedSprite;

public class Shielded extends Brute {

	{
		name = Messages.get(this,"name");
		spriteClass = ShieldedSprite.class;

		defenseSkill = 20+adj(0);
	}

	@Override
	public int dr() {
		return 20+adj(0);
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this,"def");
	}

	@Override
	public void die(Object cause) {
		super.die(cause);

	}
}
