
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Sokoban4 extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_4;
		room = 4;

		stackable = false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
