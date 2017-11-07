
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Glaive extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.GLAIVE;
	}

	public Glaive() {
		super(5, 1f, 1f);
	}

	@Override
	public int reachFactor(Hero hero) {
		return 2;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
