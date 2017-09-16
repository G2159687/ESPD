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
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MindVision;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.misc.Spectacles.MagicSight;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;

public class PotionOfMindVision extends Potion {

	{
		initials = 7;
		name = Messages.get(this, "name");
	}

	private static final String TXT_PREVENTING = Messages.get(PotionOfMindVision.class, "prevent");

	@Override
	public void apply(Hero hero) {
		setKnown();

		if (Dungeon.level.locked && Dungeon.depth > 50 && Dungeon.hero.buff(MagicSight.class) == null) {
			GLog.w(TXT_PREVENTING);
			return;
		}

		Buff.affect(hero, MindVision.class, Dungeon.hero.buff(MagicSight.class) != null ? MindVision.DURATION * 4 : MindVision.DURATION);
		Dungeon.observe();

		if (Dungeon.level.mobs.size() > 0) {
			GLog.i(Messages.get(this, "see_mobs"));
		} else {
			GLog.i(Messages.get(this, "see_none"));
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return isKnown() ? 35 * quantity : super.price();
	}
}
