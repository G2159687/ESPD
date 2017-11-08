
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Bone extends JournalPage {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_14;
		room = 14;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}