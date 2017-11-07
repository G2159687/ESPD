
package com.github.epd.sprout.items.bags;

import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class WandHolster extends Bag {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.HOLSTER;

		size = 24;
	}

	@Override
	public boolean grab(Item item) {
		return item instanceof Wand;
	}

	@Override
	public boolean collect(Bag container) {
		if (super.collect(container)) {
			if (owner != null) {
				for (Item item : items) {
					((Wand) item).charge(owner);
				}
			}
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		for (Item item : items) {
			((Wand) item).stopCharging();
		}
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}
}
