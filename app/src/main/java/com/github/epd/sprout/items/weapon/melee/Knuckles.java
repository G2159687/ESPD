
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Knuckles extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.KNUCKLEDUSTER;
	}

	public Knuckles() {
		super(1, 1f, 0.5f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
