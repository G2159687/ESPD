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

import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class ChargrilledMeat extends Food {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.STEAK;
		energy = Hunger.STARVING/2;
		hornValue = 2;
		bones = false;
	}

	@Override
	public String info() {
		return Messages.get(this,"desc");
	}

	@Override
	public int price() {
		return 5 * quantity;
	}

	public static Food cook(MysteryMeat ingredient) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}
	
	public static Food cook(Meat ingredient) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}
}
