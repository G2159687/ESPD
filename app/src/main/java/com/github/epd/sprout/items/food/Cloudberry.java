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
import com.github.epd.sprout.actors.buffs.Haste;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.Levitation;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.potions.PotionOfLevitation;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Cloudberry extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SEED_CLOUDBERRY;
		energy = (Hunger.STARVING - Hunger.HUNGRY) / 10;
		message = Messages.get(Blackberry.class, "eat");
		bones = false;
	}

	private int duration = 10;

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(10)) {
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
					Buff.affect(hero, Haste.class, Haste.DURATION);
					GLog.i(Messages.get(this, "haste"));
					break;
				case 6:
				case 7:
				case 8:
					Buff.affect(hero, Haste.class, Haste.DURATION);
					if (Dungeon.depth < 51) {
						Buff.affect(hero, Levitation.class, duration);
						GLog.i(Messages.get(PotionOfLevitation.class, "float"));
					}
					GLog.i(Messages.get(this, "haste"));

					break;
				case 9:
				case 10:
					Buff.affect(hero, Haste.class, Haste.DURATION);
					if (Dungeon.depth < 51) {
						Buff.affect(hero, Levitation.class, duration * 2);
						GLog.i(Messages.get(PotionOfLevitation.class, "float"));
					}
					GLog.i(Messages.get(this, "haste"));

					GLog.p(Messages.get(Blackberry.class, "eat3"));
					Buff.affect(hero, BerryRegeneration.class).level(hero.HT);
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

	public Cloudberry() {
		this(1);
	}

	public Cloudberry(int value) {
		this.quantity = value;
	}
}
