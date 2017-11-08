
package com.github.epd.sprout.items.quest;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class OrangeNornStone extends NornStone {


	{
		type = 3;
		name = Messages.get(NornStone.class, "name");
		image = ItemSpriteSheet.NORNORANGE;
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
