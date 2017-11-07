
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;

public class Instability extends Weapon.Enchantment {

	private static final String TXT_UNSTABLE = Messages.get(Instability.class, "name");

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		return random().proc(weapon, attacker, defender, damage);
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		return random().proc(weapon, attacker, defender, damage);
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_UNSTABLE, weaponName);
	}

}
