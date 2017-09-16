package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.BlueMilk;
import com.github.epd.sprout.items.food.DeathCap;
import com.github.epd.sprout.items.food.Earthstar;
import com.github.epd.sprout.items.food.GoldenJelly;
import com.github.epd.sprout.items.food.JackOLantern;
import com.github.epd.sprout.items.food.PixieParasol;
import com.github.epd.sprout.items.potions.Potion;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class PotionBandolier extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.BANDOLIER;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Potion
				|| item instanceof BlueMilk
				|| item instanceof DeathCap
				|| item instanceof Earthstar
				|| item instanceof GoldenJelly
				|| item instanceof JackOLantern
				|| item instanceof PixieParasol;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
