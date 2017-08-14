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
package com.github.epd.sprout.items.spells;

import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;

public class Spell extends Item {

	private static final String TXT_VALUE = "Spell Page";
	public int spellnum;

	{
		name = "spell page";
		image = ItemSpriteSheet.JOURNAL_PAGE;

		stackable = false;
		unique = true;
	}
		
	@Override
	public boolean doPickUp(Hero hero) {
         
		GLog.p("You found a spell page! It reads, %s.", TXT_VALUE);
		return super.doPickUp(hero);
	
	}


	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public int price() {
		return 10 * quantity;
	}
	
	@Override
	public String info() {
		return "A Spell Page.";
	}
}
