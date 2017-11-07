
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

public class Potential extends Glyph {

	private static final String TXT_POTENTIAL = Messages.get(Potential.class, "name");

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x66CCEE);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);

		if (Dungeon.level.adjacent(attacker.pos, defender.pos)
				&& Random.Int(level + 7) >= 6) {

			int dmg = Random.IntRange(1, damage);
			attacker.damage(dmg, LightningTrap.LIGHTNING);
			//dmg = Random.IntRange(1, dmg);
			//defender.damage(dmg, LightningTrap.LIGHTNING);

			checkOwner(defender);
			if (defender == Dungeon.hero) {
				Camera.main.shake(2, 0.3f);
			}

			attacker.sprite.parent.add(new Lightning(attacker.pos, defender.pos, null));

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_POTENTIAL, weaponName);
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}
}
