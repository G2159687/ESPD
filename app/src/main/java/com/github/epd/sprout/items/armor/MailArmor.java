
package com.github.epd.sprout.items.armor;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class MailArmor extends Armor {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ARMOR_MAIL;
	}

	public MailArmor() {
		super(3);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
