
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class StoneOre extends Item {


	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.STONE;
		bones = false;
		stackable = true;
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
	public int price() {
		return 1000 * quantity;
	}
}
