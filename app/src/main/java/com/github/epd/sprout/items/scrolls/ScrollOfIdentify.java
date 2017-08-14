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

import com.github.epd.sprout.effects.Identification;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;

public class ScrollOfIdentify extends InventoryScroll {

	{
		name = Messages.get(this,"name");
		inventoryTitle = Messages.get(this,"inv_title");
		mode = WndBag.Mode.UNIDENTIFED;
		consumedValue = 10;
		initials=0;

		bones = true;
	}

	@Override
	protected void onItemSelected(Item item) {

		curUser.sprite.parent.add(new Identification(curUser.sprite.center()
				.offset(0, -16)));

		item.identify();
		GLog.i(Messages.get(this,"it_is", item));
	}

	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}

	@Override
	public int price() {
		return isKnown() ? 30 * quantity : super.price();
	}
}
