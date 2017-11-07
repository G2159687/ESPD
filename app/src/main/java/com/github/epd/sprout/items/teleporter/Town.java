
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Town extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_5;
		room = 5;

		stackable = false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
