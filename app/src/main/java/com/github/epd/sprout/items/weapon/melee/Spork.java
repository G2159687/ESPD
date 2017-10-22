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
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.utils.Random;

public class Spork extends MeleeWeapon {

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.SPORK;
		reinforced = true;
	}

	public Spork() {
		super(1, 1.2f, 0.25f);
	}

	@Override
	public int min() {
		return 3;
	}

	@Override
	public int max() {
		return 4;
	}

	@Override
	public Item upgrade(boolean enchant) {
		STR--;
		MIN++;
		MAX += 3;

		return super.upgrade(enchant);
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {

		if (defender.properties().contains(Char.Property.EVIL))
			defender.damage(Random.Int(damage, damage * 4), this);


		if (enchantment != null)
			enchantment.proc(this, attacker, defender, damage);
		
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}
}
