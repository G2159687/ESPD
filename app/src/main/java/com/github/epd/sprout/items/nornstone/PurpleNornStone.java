
package com.github.epd.sprout.items.nornstone;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class PurpleNornStone extends NornStone {


	{
		type = 4;
		name = Messages.get(NornStone.class, "name");
		image = ItemSpriteSheet.NORNPURPLE;
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
