
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Pasty extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.PASTY;
		energy = Hunger.STARVING;

		bones = true;
	}

	@Override
	public String info() {
		return Messages.get(this, "pasty_desc");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}
}
