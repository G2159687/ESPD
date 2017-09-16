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
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.Rice;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.food.Blueberry;
import com.github.epd.sprout.items.food.Cloudberry;
import com.github.epd.sprout.items.food.FullMoonberry;
import com.github.epd.sprout.items.food.GoldenNut;
import com.github.epd.sprout.items.food.Moonberry;
import com.github.epd.sprout.items.food.Nut;
import com.github.epd.sprout.items.food.ToastedNut;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class SeedPouch extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.POUCH;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof GoldenNut || item instanceof ToastedNut || item instanceof Nut
				|| item instanceof Blackberry
				|| item instanceof Blueberry
				|| item instanceof Cloudberry
				|| item instanceof Moonberry
				|| item instanceof FullMoonberry
				|| item instanceof Plant.Seed
				|| item instanceof Rice;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
