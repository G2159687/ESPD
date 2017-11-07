
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;

public class Luck extends Weapon.Enchantment {

	private static final String TXT_LUCKY = Messages.get(Luck.class, "name");

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x00FF00);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		int level = Math.max(0, weapon.level);

		int dmg = damage;
		for (int i = 1; i <= level + 1; i++) {
			dmg = Math.max(dmg, attacker.damageRoll() - i);
		}

		if (dmg > damage) {
			defender.damage(dmg - damage, this);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		int level = Math.max(0, weapon.level);

		int dmg = damage;
		for (int i = 1; i <= level + 1; i++) {
			dmg = Math.max(dmg, attacker.damageRoll() - i);
		}

		if (dmg > damage) {
			defender.damage(dmg - damage, this);
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
		return GREEN;
	}
}
