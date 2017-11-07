
package com.github.epd.sprout.items.consumables;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class AdamantWand extends Item {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_ADAMANT;

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
