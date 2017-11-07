
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Nomnom extends Weapon.Enchantment {

	private static final String TXT_NOM = Messages.get(Nomnom.class, "name");

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing(0x660022);

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
		// lvl 0 - 8%
		// lvl 1 ~ 9%
		// lvl 2 ~ 10%
		int level = Math.max(0, weapon.level * 3);

		if (Random.Int(level + 100) >= 75) {

			defender.damage(defender.HP, this);


			int maxValue = damage * (level + 2) / (level + 3);
			int effValue = Math.min(Random.IntRange(0, maxValue), attacker.HT - attacker.HP);

			if (effValue > 0) {

				attacker.HP += effValue;
				attacker.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f, 1);
				attacker.sprite.showStatus(CharSprite.POSITIVE, Integer.toString(effValue));
			}

			attacker.buff(Hunger.class).satisfy(maxValue);
			SpellSprite.show(attacker, SpellSprite.FOOD);


			return true;

		} else {

			return false;

		}
	}

	@Override
	public Glowing glowing() {
		return RED;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_NOM, weaponName);
	}

}
