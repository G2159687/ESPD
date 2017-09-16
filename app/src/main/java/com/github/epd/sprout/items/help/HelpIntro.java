package com.github.epd.sprout.items.help;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

//This is for help only, not a real item!!!

public class HelpIntro extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.LOCKED_CHEST;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
