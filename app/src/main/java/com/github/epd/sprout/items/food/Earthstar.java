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
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Earthstar extends Food {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.MUSHROOM_EARTHSTAR;
		energy = (Hunger.STARVING - Hunger.HUNGRY)/10;
		message = Messages.get(BlueMilk.class,"eat");
		hornValue = 2;
		bones = false;
	}
	
	private static final String TXT_PREVENTING = Messages.get(BlueMilk.class,"prevent");
	private static final String TXT_EFFECT = Messages.get(Earthstar.class,"effect");

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
					if (mob.hostile) {
						int damage = Math.max(0, (Dungeon.depth + 3) - Random.IntRange(0, mob.dr() / 2));
						Buff.affect(mob, Bleeding.class).set(damage);
						Buff.prolong(mob, Cripple.class, Cripple.DURATION * 2);
					}
				}
				int damage = Math.max(0,(Dungeon.depth) - Random.IntRange(0, hero.dr()));
				hero.damage(Math.max(1,Math.round(hero.HP/2)), this);
				Buff.affect(hero, Bleeding.class).set(damage);
				Buff.prolong(hero, Cripple.class, Cripple.DURATION);
				break;
			case 0: case 2: case 3: case 4: case 5: 
			case 6: case 7: case 8: case 9: case 10:
				for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0])) {
					if (mob.hostile) {
						int mobdamage = Math.max(0, (Dungeon.depth + 3) - Random.IntRange(0, mob.dr() / 2));
						Buff.affect(mob, Bleeding.class).set(mobdamage);
						Buff.prolong(mob, Cripple.class, Cripple.DURATION * 2);
					}
				}
				int herodamage = Math.max(0,(Dungeon.depth) - Random.IntRange(0, hero.dr()/2));
				hero.damage(Math.max(1,Math.round(hero.HP/2)), this);
				Buff.affect(hero, Bleeding.class).set(herodamage);
				Buff.prolong(hero, Cripple.class,  Cripple.DURATION);
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
	
	public Earthstar() {
		this(1);
	}

	public Earthstar(int value) {
		this.quantity = value;
	}
}
