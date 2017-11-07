
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.SnowParticle;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class AntiEntropy extends Glyph {

	private static final String TXT_ANTI_ENTROPY = Messages.get(AntiEntropy.class, "name");

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x0000FF);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);

		if (Dungeon.level.adjacent(attacker.pos, defender.pos)
				&& Random.Int(level + 6) >= 5) {

			Buff.prolong(attacker, Frost.class, Frost.duration(attacker)
					* Random.Float(1f, 1.5f));
			CellEmitter.get(attacker.pos).start(SnowParticle.FACTORY, 0.2f, 6);

			//Buff.affect(defender, Burning.class).reignite(defender);
			//defender.sprite.emitter().burst(FlameParticle.FACTORY, 5);

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_ANTI_ENTROPY, weaponName);
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}
}
