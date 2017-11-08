
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class BookOfDead extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_17;
		room = 17;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}