
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Fire extends Weapon.Enchantment {

	private static final String TXT_BLAZING = Messages.get(Fire.class, "name");

	private static ItemSprite.Glowing ORANGE = new ItemSprite.Glowing(0xFF4400);

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

			if (Random.Int(2) == 0) {
				Buff.affect(defender, Burning.class).reignite(defender);
			}
			defender.damage(Random.Int(1, level + 2), this);

			defender.sprite.emitter().burst(FlameParticle.FACTORY, level < 10 ? level + 1 : 10);

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

			if (Random.Int(2) == 0) {
				Buff.affect(defender, Burning.class).reignite(defender);
			}
			defender.damage(Random.Int(1, level + 2), this);

			defender.sprite.emitter().burst(FlameParticle.FACTORY, level < 10 ? level + 1 : 10);

			return true;

		} else {

			return false;

		}
	}

	@Override
	public Glowing glowing() {
		return ORANGE;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_BLAZING, weaponName);
	}

}
