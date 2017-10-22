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
package com.github.epd.sprout.items.weapon.enchantments;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.items.weapon.melee.relic.RelicMeleeWeapon;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSprite;
import com.github.epd.sprout.sprites.ItemSprite.Glowing;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class AresLeech extends Weapon.Enchantment {

	private static final String TXT_DRAWING = Messages.get(AresLeech.class, "drawing");

	private static ItemSprite.Glowing PURPLE = new ItemSprite.Glowing(0x660066);

	@Override
	public boolean proc(Weapon weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(Wand weapon, Char attacker, Char defender, int damage) {
		return false;
	}

	@Override
	public boolean proc(RelicMeleeWeapon weapon, Char attacker, Char defender, int damage) {

		int level = Math.max(0, weapon.level);

		int drains = 0;

		boolean procced = false;
		int distance = 1 + level * 2;

		int maxValue = damage * (level + 2) / (level + 6);
		int effValue = Math.min(Random.IntRange(0, maxValue), attacker.HT - attacker.HP);

		for (Mob mob : Dungeon.level.mobs) {

			boolean visible = Level.fieldOfView[mob.pos];

			if (Level.distance(attacker.pos, mob.pos) < distance && mob.isAlive() && !mob.isPassive()) {
				if (effValue < mob.HP) {
					mob.damage(effValue, weapon);
					weapon.charge++;
					drains++;
				}
			}
		}

		if (drains > 0) {
			GLog.i(Messages.get(AresLeech.class, "effect", drains));
		}
		// lvl 0 - 33%
		// lvl 1 - 43%
		// lvl 2 - 50%


		if (effValue > 0) {

			attacker.HP += effValue;
			attacker.sprite.emitter().start(Speck.factory(Speck.HEALING), 0.4f,
					1);
			attacker.sprite.showStatus(CharSprite.POSITIVE,
					Integer.toString(effValue));

			return true;

		} else {
			return false;
		}


	}

	@Override
	public Glowing glowing() {
		return PURPLE;
	}

	@Override
	public String name(String weaponName) {
		return String.format(TXT_DRAWING, weaponName);
	}

}
