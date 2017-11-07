
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.SanChikarahDeath;
import com.github.epd.sprout.items.SanChikarahLife;
import com.github.epd.sprout.items.SanChikarahTranscend;
import com.github.epd.sprout.items.keys.Key;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class KeyRing extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.KEYRING;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Key
				|| item instanceof SanChikarahDeath
				|| item instanceof SanChikarahLife
				|| item instanceof SanChikarahTranscend;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}

