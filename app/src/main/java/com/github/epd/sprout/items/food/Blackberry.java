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
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.BerryRegeneration;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.MindVision;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Blackberry extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SEED_BLACKBERRY;
		energy = (Hunger.STARVING - Hunger.HUNGRY) / 10;
		message = Messages.get(this, "eat");
		bones = false;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(10)) {
				case 1:
					Buff.affect(hero, MindVision.class, MindVision.DURATION);
					Dungeon.observe();

					if (Dungeon.level.mobs.size() > 0) {
						GLog.i(Messages.get(this, "mv1"));
					} else {
						GLog.i(Messages.get(this, "mv2"));
					}
					Buff.affect(hero, BerryRegeneration.class).level(hero.HT + hero.HT);
					GLog.w(Messages.get(this, "eat3"));
					break;
				case 0:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
					GLog.p(Messages.get(this, "eat3"));
					Buff.affect(hero, BerryRegeneration.class).level(hero.HT / 2);
					break;
			}
		}
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

	public Blackberry() {
		this(1);
	}

	public Blackberry(int value) {
		this.quantity = value;
	}
}
