
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ImpSprite;

public class ImpShopkeeper extends Shopkeeper {

	{
		name = Messages.get(Imp.class, "name");
		spriteClass = ImpSprite.class;
	}

	@Override
	public String description() {
		return Messages.get(Imp.class, "desc");
	}
}
