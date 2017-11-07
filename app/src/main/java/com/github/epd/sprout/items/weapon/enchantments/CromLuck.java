
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;

public class CromLuck extends Weapon.Enchantment {

	private static final String TXT_LUCKY = Messages.get(CromLuck.class, "lucky");

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing(0x800000);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		int level = Math.max(0, weapon.level);

		int dmg = damage;
		for (int i = 1; i <= level + 1; i++) {
			dmg = Math.max(dmg, attacker.damageRoll());
		}

		if (dmg > damage) {
			defender.damage(dmg - damage, weapon);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_LUCKY, weaponName);
	}

	@Override
	public Glowing glowing() {
		return RED;
	}
}
