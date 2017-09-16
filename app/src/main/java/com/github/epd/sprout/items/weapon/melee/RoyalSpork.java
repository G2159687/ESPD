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
package com.github.epd.sprout.items.weapon.melee;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Gullin;
import com.github.epd.sprout.actors.mobs.Kupua;
import com.github.epd.sprout.actors.mobs.MineSentinel;
import com.github.epd.sprout.actors.mobs.Otiluke;
import com.github.epd.sprout.actors.mobs.Zot;
import com.github.epd.sprout.actors.mobs.ZotPhase;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class RoyalSpork extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.ROYALSPORK;
		reinforced = true;
	}

	public RoyalSpork() {
		super(1, 1f, 0.15f);
	}


	@Override
	public void proc(Char attacker, Char defender, int damage) {

		if (defender instanceof Gullin
				|| defender instanceof Kupua
				|| defender instanceof MineSentinel
				|| defender instanceof Otiluke
				|| defender instanceof Zot
				|| defender instanceof ZotPhase) {

			//damage*=2;

			defender.damage(Random.Int(damage, damage * 8), this);
		}


		if (enchantment != null) {
			enchantment.proc(this, attacker, defender, damage);
		}
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
