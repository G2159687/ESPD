
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Spork extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SPORK;
		reinforced = true;
	}

	public Spork() {
		super(1, 1.2f, 0.25f);
	}

	@Override
	public int min() {
		return 3;
	}

	@Override
	public int max() {
		return 4;
	}

	@Override
	public Item upgrade(boolean enchant) {
		STR--;
		MIN++;
		MAX += 3;

		return super.upgrade(enchant);
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {

		if (defender.properties().contains(Char.Property.EVIL))
			defender.damage(Random.Int(damage, damage * 4), this);


		if (enchantment != null)
			enchantment.proc(this, attacker, defender, damage);
		
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
