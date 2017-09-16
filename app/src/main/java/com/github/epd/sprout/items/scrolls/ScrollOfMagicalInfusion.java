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
package com.github.epd.sprout.items.scrolls;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;

public class ScrollOfMagicalInfusion extends InventoryScroll {

	private static final String TXT_INFUSE = Messages.get(ScrollOfMagicalInfusion.class, "infuse");

	{
		initials = 2;
		name = Messages.get(this, "name");
		inventoryTitle = Messages.get(this, "inv_title");
		mode = WndBag.Mode.ENCHANTABLE;
		consumedValue = 15;

		bones = true;
	}

	@Override
	protected void onItemSelected(Item item) {

		ScrollOfRemoveCurse.uncurse(Dungeon.hero, item);
		if (item instanceof Weapon)
			((Weapon) item).upgrade(true);
		else
			((Armor) item).upgrade(true);

		GLog.p(TXT_INFUSE, item.name());

		curUser.sprite.emitter().start(Speck.factory(Speck.UP), 0.2f, 3);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
