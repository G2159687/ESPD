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
package com.github.epd.sprout.plants;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Drowsy;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Slow;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.potions.PotionOfPurity;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;

public class Dreamfoil extends Plant {

	private static final String TXT_DESC = Messages.get(Dreamfoil.class,"desc");

	{
		image = 10;
		plantName = Messages.get(this,"name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		if (ch != null) {
			if (ch instanceof Mob)
				Buff.affect(ch, MagicalSleep.class);
			else if (ch instanceof Hero) {
				GLog.i(Messages.get(this,"refreshed"));
				Buff.detach(ch, Poison.class);
				Buff.detach(ch, Cripple.class);
				Buff.detach(ch, Weakness.class);
				Buff.detach(ch, Bleeding.class);
				Buff.detach(ch, Drowsy.class);
				Buff.detach(ch, Slow.class);
				Buff.detach(ch, Vertigo.class);
			}
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Dreamfoil.class,"name");

			name = Messages.get(this,"name");
			image = ItemSpriteSheet.SEED_DREAMFOIL;

			plantClass = Dreamfoil.class;
			alchemyClass = PotionOfPurity.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class,"seeddesc", plantName);
		}
	}
}