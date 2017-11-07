
package com.github.epd.sprout.items.armor;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class ScaleArmor extends Armor {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARMOR_SCALE;
	}

	public ScaleArmor() {
		super(4);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
