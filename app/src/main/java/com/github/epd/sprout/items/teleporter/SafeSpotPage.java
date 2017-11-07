
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class SafeSpotPage extends JournalPage {

	public int room = 0;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_0;

		stackable = false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
