
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class NornStone extends Item {

	public int type = 0;

	{
		stackable = true;
		name = Messages.get(NornStone.class, "name");
		image = ItemSpriteSheet.NORNGREEN;
		bones = false;
	}


	@Override
	public String info() {
		return Messages.get(NornStone.class, "desc");
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
