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
package com.github.epd.sprout.items.misc;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.KindofMisc;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MiscEquippable extends KindofMisc {
	
	public String cursedDesc(){
		return Messages.get(this,"cursed", this);
	}

	protected Buff buff;

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(isEquipped(hero) ? AC_UNEQUIP : AC_EQUIP);
		return actions;
	}

	@Override
	public void activate(Char ch) {
		buff = buff();
		buff.attachTo(ch);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {
		if (super.doUnequip(hero, collect, single)) {

			hero.remove(buff);
			buff = null;

			return true;

		} else {

			return false;

		}
	}

	@Override
	public Item upgrade() {

		super.upgrade();

		if (buff != null) {

			Char owner = buff.target;
			buff.detach();
			if ((buff = buff()) != null) {
				buff.attachTo(owner);
			}
		}

		return this;
	}



	@Override
	public String name() {
		return name ;
	}

	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}

	@Override
	public String info() {
		if (isEquipped(Dungeon.hero)) {

			return desc();
				

		} else if (cursed && cursedKnown) {

			return desc();
					
		} else {

			return desc() ;

		}
	}

	

	@Override
	public Item random() {
		if (Random.Float() < 0.3f) {
			level = -Random.Int(1, 3);
			cursed = true;
		} else
			level = Random.Int(1, 2);
		return this;
	}

	
	@Override
	public int price() {
		int price = 75;
		if (cursed && cursedKnown) {
			price /= 2;
		}
		if (levelKnown) {
			if (level > 0) {
				price *= (level + 1);
			} else if (level < 0) {
				price /= (1 - level);
			}
		}
		if (price < 1) {
			price = 1;
		}
		return price;
	}

	protected MiscBuff buff() {
		return null;
	}

	

	public class MiscBuff extends Buff {

		public int level;

		public MiscBuff() {
			level = MiscEquippable.this.level;
		}

		@Override
		public boolean attachTo(Char target) {
		
			return super.attachTo(target);
		}

		@Override
		public boolean act() {		

			spend(TICK);

			return true;
		}
	}
}
