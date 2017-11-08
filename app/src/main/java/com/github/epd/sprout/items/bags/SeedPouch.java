
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.misc.Rice;
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
