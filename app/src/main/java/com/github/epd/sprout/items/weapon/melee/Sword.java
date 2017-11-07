
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Sword extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SWORD;
	}

	public Sword() {
		super(3, 1f, 1f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
