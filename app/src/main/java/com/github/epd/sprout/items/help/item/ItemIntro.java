package com.github.epd.sprout.items.help.item;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

//This is for help only, not a real item!!!

public class ItemIntro extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PALANTIR;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
