
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class JournalPage extends Item {

	public int room;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PAGE_0;

		stackable = false;
		unique = true;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
