
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.EarthParticle;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Earthroot;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Entanglement extends Glyph {

	private static final String TXT_ENTANGLEMENT = Messages.get(Entanglement.class, "name");

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x448822);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);
		int levelRoots = Math.min(4, armor.level);

		if (Random.Int(4) == 0) {

			Buff.prolong(defender, Roots.class, 5 - levelRoots);
			Buff.affect(defender, Earthroot.Armor.class).level(5 * (level + 1));
			CellEmitter.bottom(defender.pos).start(EarthParticle.FACTORY,
					0.05f, 8);
			Camera.main.shake(1, 0.4f);

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_ENTANGLEMENT, weaponName);
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}

}
