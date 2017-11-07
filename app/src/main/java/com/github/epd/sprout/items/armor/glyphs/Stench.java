
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.GasesImmunity;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Stench extends Glyph {

	private static final String TXT_STENCH = Messages.get(Stench.class, "name");

	private static ItemSprite.Glowing GREEN = new ItemSprite.Glowing(0x22CC44);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);

		if (Dungeon.level.adjacent(attacker.pos, defender.pos)
				&& Random.Int(level + 5) >= 4) {

			Buff.prolong(defender, GasesImmunity.class, GasesImmunity.DURATION);
			GameScene.add(Blob.seed(attacker.pos, 20, ToxicGas.class));

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_STENCH, weaponName);
	}

	@Override
	public Glowing glowing() {
		return GREEN;
	}

}
