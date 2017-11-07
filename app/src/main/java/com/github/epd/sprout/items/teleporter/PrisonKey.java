
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class PrisonKey extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_9;
		room = 9;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
