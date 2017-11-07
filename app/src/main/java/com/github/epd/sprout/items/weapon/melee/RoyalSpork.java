
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class RoyalSpork extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ROYALSPORK;
		reinforced = true;
	}

	public RoyalSpork() {
		super(1, 1f, 0.15f);
	}


	@Override
	public void proc(Char attacker, Char defender, int damage) {

		if (defender.properties().contains(Char.Property.EVIL))
			defender.damage(Random.Int(damage, damage * 8), this);

		if (enchantment != null)
			enchantment.proc(this, attacker, defender, damage);

	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
