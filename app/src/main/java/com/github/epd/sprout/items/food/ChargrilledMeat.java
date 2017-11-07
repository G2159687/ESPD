
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class ChargrilledMeat extends Food {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.STEAK;
		energy = Hunger.STARVING / 2;
		bones = false;
	}

	@Override
	public String info() {
		return Messages.get(this, "desc");
	}

	@Override
	public int price() {
		return 5 * quantity;
	}

	public static Food cook(MysteryMeat ingredient) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}

	public static Food cook(Meat ingredient) {
		ChargrilledMeat result = new ChargrilledMeat();
		result.quantity = ingredient.quantity();
		return result;
	}
}
