
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Charm;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.GameMath;
import com.watabou.utils.Random;

public class Affection extends Glyph {

	private static final String TXT_AFFECTION = Messages.get(Affection.class, "name");

	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xFF4488);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = (int) GameMath.gate(0, armor.level, 6);

		if (Dungeon.level.adjacent(attacker.pos, defender.pos)
				&& Random.Int(level / 2 + 5) >= 4) {

			int duration = Random.IntRange(3, 7);

			Buff.affect(attacker, Charm.class, Charm.durationFactor(attacker)
					* duration).object = defender.id();
			attacker.sprite.centerEmitter().start(Speck.factory(Speck.HEART),
					0.2f, 5);

			//duration *= Random.Float(0.5f, 1);

			//Buff.affect(defender, Charm.class, Charm.durationFactor(defender)
			//		* duration).object = attacker.id();
			//defender.sprite.centerEmitter().start(Speck.factory(Speck.HEART),
			//		0.2f, 5);
		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_AFFECTION, weaponName);
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
}
