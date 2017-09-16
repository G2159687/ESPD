package com.github.epd.sprout.items.help.mechanics;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

//This is for help only, not a real item!!!

public class GameProgress extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.OTILUKES_JOURNAL;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
