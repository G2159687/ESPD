
package com.github.epd.sprout.items.armor;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class ClothArmor extends Armor {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARMOR_CLOTH;

		bones = false; // Finding them in bones would be semi-frequent and
		// disappointing.
	}

	public ClothArmor() {
		super(1);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
