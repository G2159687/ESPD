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
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Thief;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.FrozenCarpaccio;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.items.rings.RingOfElements.Resistance;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;

public class Frost extends FlavourBuff {

	private static final float DURATION = 5f;

	@Override
	public boolean attachTo(Char target) {
		if (super.attachTo(target)) {

			target.paralysed++;
			Buff.detach(target, Burning.class);

			if (target instanceof Hero) {

				Hero hero = (Hero) target;
				Item item = hero.belongings.randomUnequipped();
				if (item instanceof Potion) {

					item = item.detach(hero.belongings.backpack);
					GLog.w(Messages.get(this,"item"), item.toString());
					((Potion) item).shatter(hero.pos);

				} else if (item instanceof MysteryMeat) {

					item = item.detach(hero.belongings.backpack);
					FrozenCarpaccio carpaccio = new FrozenCarpaccio();
					if (!carpaccio.collect(hero.belongings.backpack)) {
						Dungeon.level.drop(carpaccio, target.pos).sprite.drop();
					}
					GLog.w(Messages.get(this,"item"), item.toString());

				}
			} else if (target instanceof Thief
					&& ((Thief) target).item instanceof Potion) {

				((Potion) ((Thief) target).item).shatter(target.pos);
				((Thief) target).item = null;

			}

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void detach() {
		super.detach();
		if (target.paralysed > 0)
			target.paralysed--;
	}

	@Override
	public int icon() {
		return BuffIndicator.FROST;
	}

	@Override
	public String toString() {
		return Messages.get(this,"name");
	}

	public static float duration(Char ch) {
		Resistance r = ch.buff(Resistance.class);
		return r != null ? r.durationFactor() * DURATION : DURATION;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
