
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class BookOfTranscendence extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_19;
		room = 19;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}