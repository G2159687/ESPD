
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.consumables.AdamantRing;
import com.github.epd.sprout.items.Ankh;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.RingOfDisintegration;
import com.github.epd.sprout.items.artifacts.RingOfFrost;
import com.github.epd.sprout.items.rings.Ring;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class AnkhChain extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.CHAIN;

		size = 24;
	}


	@Override
	public boolean grab(Item item) {
		return item instanceof Ankh || item instanceof Ring || item instanceof RingOfDisintegration
				|| item instanceof AdamantRing || item instanceof RingOfFrost;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}

