
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class GreenNornStone extends NornStone {


	{
		type = 1;
		name = Messages.get(NornStone.class, "name");
		image = ItemSpriteSheet.NORNGREEN;
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
