
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Mace extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.MACE;
	}

	public Mace() {
		super(3, 1f, 0.8f);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
