/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.items.armor.glyphs;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.npcs.MirrorImage;
import com.github.epd.sprout.items.armor.Armor;
import com.github.epd.sprout.items.armor.Armor.Glyph;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Multiplicity extends Glyph {

	private static final String TXT_MULTIPLICITY = Messages.get(Multiplicity.class,"name");

	private static ItemSprite.Glowing PINK = new ItemSprite.Glowing(0xCCAA88);

	@Override
	public int proc(Armor armor, Char attacker, Char defender, int damage) {

		int level = Math.max(0, armor.level);

		if (Random.Int(level / 2 + 6) >= 5) {

			ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

			for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
				int p = defender.pos + PathFinder.NEIGHBOURS8[i];
				if (Actor.findChar(p) == null
						&& (Level.passable[p] || Level.avoid[p])) {
					respawnPoints.add(p);
				}
			}

			if (respawnPoints.size() > 0) {
				MirrorImage mob = new MirrorImage();
				mob.duplicate((Hero) defender);
				GameScene.add(mob);
				WandOfBlink.appear(mob, Random.element(respawnPoints));

				defender.damage(Random.IntRange(1, defender.HT / 6), this);
				checkOwner(defender);
			}

		}

		return damage;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_MULTIPLICITY, weaponName);
	}

	@Override
	public Glowing glowing() {
		return PINK;
	}
}
