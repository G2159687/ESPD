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
package com.github.epd.sprout.items.food;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class GoldenJelly extends Food {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.MUSHROOM_GOLDENJELLY;
		energy = (Hunger.STARVING - Hunger.HUNGRY)/10;
		message = Messages.get(BlueMilk.class,"eat");
		hornValue = 2;
		bones = false;
	}

	private static final String TXT_PREVENTING = Messages.get(BlueMilk.class,"prevent");
	private static final String TXT_EFFECT = Messages.get(GoldenJelly.class,"effect");

	@Override
	public void execute(Hero hero, String action) {
		
		if (action.equals(AC_EAT)) {
			
			if (Dungeon.bossLevel()){
				GLog.w(TXT_PREVENTING);
				return;
			}

		}
		
	   if (action.equals(AC_EAT)) {
		   
		  	   
		   GLog.w(TXT_EFFECT);
			
		   switch (Random.Int(10)) {
			case 1:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.hostile)
					Buff.prolong(mob, Roots.class, 20);
				}
				Buff.affect(hero, Vertigo.class, 1f);
				break;
			case 0: case 2: case 3: case 4: case 5: 
			case 6: case 7: case 8: case 9: case 10:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.hostile)
					Buff.prolong(mob, Roots.class, 10);
				}
				Buff.affect(hero, Vertigo.class, 3f);
				break;
			}
		}
	   super.execute(hero, action);
	}	
	
	@Override
	public String info() {
		return Messages.get(this,"desc");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}
	
	public GoldenJelly() {
		this(1);
	}

	public GoldenJelly(int value) {
		this.quantity = value;
	}
}
