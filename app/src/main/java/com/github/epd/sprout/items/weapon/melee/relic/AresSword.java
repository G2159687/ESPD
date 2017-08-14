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
package com.github.epd.sprout.items.weapon.melee.relic;

import com.github.epd.sprout.actors.buffs.BerryRegeneration;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;

import java.util.ArrayList;

public class AresSword extends RelicMeleeWeapon {

	public AresSword() {
		super(6, 1f, 1f);

	}

	
	{
		name = Messages.get(this,"name");
		image = ItemSpriteSheet.ARESSWORD;

		level = 0;
		exp = 0;
		levelCap = 15;

		charge = 0;
		chargeCap = 1000;

		cooldown = 0;
		bones = false;

		defaultAction = AC_REGEN;
  }
		
	public static final String AC_REGEN = Messages.get(AresSword.class,"ac_regen");

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (isEquipped(hero) && charge >= chargeCap)
			actions.add(AC_REGEN);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {
		if (action.equals(AC_REGEN)) {
			GLog.p(Messages.get(this,"ready"));
			charge = 0;
			Buff.affect(hero, BerryRegeneration.class).level(level*2);
		} else
			super.execute(hero, action);
	}

	
	public class RegenCounter extends WeaponBuff {

		@Override
		public boolean act() {
			if (charge < chargeCap) {
				charge+=level;
				if (charge >= chargeCap) {
					charge = chargeCap;
					GLog.p(Messages.get(AresSword.class,"buffdesc"));
				}
				updateQuickslot();
			}
			spend(TICK);
			return true;
		}
		
		@Override
		public String toString() {
			return Messages.get(AresSword.class,"buffname");
		}

		@Override
		public int icon() {
			if (cooldown == 0)
				return BuffIndicator.NONE;
			else
				return BuffIndicator.NONE;
		}

		@Override
		public void detach() {
			cooldown = 0;
			charge = 0;
			super.detach();
		}

	}
	
	
	
	
	@Override
	protected WeaponBuff passiveBuff() {
		return new RegenCounter();
	}
	
}


