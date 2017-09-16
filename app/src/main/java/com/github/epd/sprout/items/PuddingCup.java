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
package com.github.epd.sprout.items;

import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;

import java.util.ArrayList;

public class PuddingCup extends Item {

	private static final String AC_END = Messages.get(PuddingCup.class, "ac_end");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.AMULET;

		unique = true;
	}

	@Override
	public boolean doPickUp(Hero hero) {

		if (!Statistics.amuletObtained) {
			Statistics.amuletObtained = true;
		}

		return super.doPickUp(hero);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_END);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action == AC_END) {

			detach(hero.belongings.backpack);
			GLog.n(Messages.get(this, "broken"));

			hero.sprite.operate(hero.pos);
			hero.busy();

			hero.spend(TIME_TO_PICK_UP);

		} else {

			super.execute(hero, action);

		}
	}

	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
