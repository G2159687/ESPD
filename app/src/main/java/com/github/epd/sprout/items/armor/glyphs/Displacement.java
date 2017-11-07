
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.Random;

public class Displacement extends Glyph {

	private static final String TXT_DISPLACEMENT = Messages.get(Displacement.class, "name");

	private static ItemSprite.Glowing BLUE = new ItemSprite.Glowing(0x66AAFF);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		if (Dungeon.bossLevel()) {
			return damage;
		}

		float LEVEL = 0.333f;
		if (defender.HP > defender.HT * LEVEL) {
			return damage;
		}

		int nTries = (armor.level < 0 ? 1 : armor.level + 1) * 5;
		for (int i = 0; i < nTries; i++) {
			int pos = Random.Int(Dungeon.level.getLength());
			if (Dungeon.visible[pos] && Level.passable[pos]
					&& Actor.findChar(pos) == null) {

				WandOfBlink.appear(defender, pos);
				Dungeon.level.press(pos, defender);
				Buff.affect(defender, Invisibility.class, Invisibility.DURATION);
				Dungeon.observe();
				GameScene.updateFog();

				break;
			}
		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_DISPLACEMENT, weaponName);
	}

	@Override
	public Glowing glowing() {
		return BLUE;
	}
}
