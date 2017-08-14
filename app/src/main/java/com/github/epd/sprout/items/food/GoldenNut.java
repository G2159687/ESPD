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

import com.github.epd.sprout.actors.buffs.Hunger;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class GoldenNut extends Nut {

	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.SEED_GOLDENDUNGEONNUT;
		energy = Hunger.STARVING;
		message = Messages.get(this,"eat");
		hornValue = 10;
	}

	@Override
	public void execute(Hero hero, String action) {

		super.execute(hero, action);

		if (action.equals(AC_EAT)) {

			switch (Random.Int(2)) {
			case 0:
				GLog.p(Messages.get(this,"effect1"));
				
				hero.HT+=20;
				hero.STR+=2;
				hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this,"effect2"));
				GLog.p(Messages.get(this,"effect3"));
				break;
			case 1:
				GLog.p(Messages.get(this,"effect4"));
				
				hero.HT+=50;
				hero.STR+=5;
				hero.sprite.showStatus(CharSprite.POSITIVE, Messages.get(this,"effect5"));
				GLog.p(Messages.get(this,"effect3"));
				break;
			}
		}
	}	
	
	@Override
	public String info() {
		return Messages.get(this,"desc");
	}

	@Override
	public int price() {
		return 20 * quantity;
	}

}


