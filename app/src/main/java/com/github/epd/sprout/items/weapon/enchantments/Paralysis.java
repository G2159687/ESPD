
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Paralysis extends Weapon.Enchantment {

	private static final String TXT_STUNNING = Messages.get(Paralysis.class, "name");

	private static ItemSprite.Glowing YELLOW = new ItemSprite.Glowing(0xCCAA44);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 13%
		// lvl 1 - 22%
		// lvl 2 - 30%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 8) >= 7) {

			Buff.prolong(
					defender,
					com.github.epd.sprout.actors.buffs.Paralysis.class,
					Random.Float(1, 1.5f + level));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 13%
		// lvl 1 - 22%
		// lvl 2 - 30%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 8) >= 7) {

			Buff.prolong(
					defender,
					com.github.epd.sprout.actors.buffs.Paralysis.class,
					Random.Float(1, 1.5f + level));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Glowing glowing() {
		return YELLOW;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_STUNNING, weaponName);
	}

}
