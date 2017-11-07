
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Chill;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Slow extends Weapon.Enchantment {

	private static final String TXT_CHILLING = Messages.get(Slow.class, "name");

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x0044FF);

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 4) >= 3) {

			Buff.affect(defender, Chill.class,
					Random.Float(1, 3f));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		// lvl 0 - 25%
		// lvl 1 - 40%
		// lvl 2 - 50%
		int level = Math.max(0, weapon.level);

		if (Random.Int(level + 4) >= 3) {

			Buff.affect(defender, Chill.class,
					Random.Float(1, 3f));

			return true;
		} else {
			return false;
		}
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_CHILLING, weaponName);
	}

}
