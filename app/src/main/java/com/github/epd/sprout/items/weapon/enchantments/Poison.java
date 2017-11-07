
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

public class Poison extends Weapon.Enchantment {

	private static final String TXT_VENOMOUS = Messages.get(Poison.class, "name");

	private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x4400AA);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 3) >= 2) {

			Buff.affect(
					defender,
					com.github.epd.sprout.actors.buffs.Poison.class)
					.set(com.github.epd.sprout.actors.buffs.Poison
							.durationFactor(defender) * (level + 1));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 33%
		// lvl 1 - 50%
		// lvl 2 - 60%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 3) >= 2) {

			Buff.affect(
					defender,
					com.github.epd.sprout.actors.buffs.Poison.class)
					.set(com.github.epd.sprout.actors.buffs.Poison
							.durationFactor(defender) * (level + 1));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Glowing glowing() {
		return PURPLE;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_VENOMOUS, weaponName);
	}

}
