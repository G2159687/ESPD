
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Longsword extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.LONG_SWORD;
	}

	public Longsword() {
		super(4, 1f, 1f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
