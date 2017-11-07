
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.ui.BuffIndicator;
import com.watabou.utils.Random;

public class Metabolism extends Glyph {

	private static final String TXT_METABOLISM = Messages.get(Metabolism.class, "name");

	private static ItemSprite.Glowing RED = new ItemSprite.Glowing(0xCC0000);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);
		if (Random.Int(level / 2 + 5) >= 4) {

			int healing = Math.min(defender.HT - defender.HP,
					Random.Int(1, defender.HT / 5));

			if (healing > 0) {

				Hunger hunger = defender.buff(Hunger.class);

				if (hunger != null && !hunger.isStarving()) {

					//hunger.satisfy(-Hunger.STARVING / 10);
					BuffIndicator.refreshHero();

					defender.HP += healing;
					defender.sprite.emitter().burst(
							Speck.factory(Speck.HEALING), 1);
					defender.sprite.showStatus(CharSprite.POSITIVE,
							Integer.toString(healing));
				}
			}

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_METABOLISM, weaponName);
	}

	@Override
	public Glowing glowing() {
		return RED;
	}
}
