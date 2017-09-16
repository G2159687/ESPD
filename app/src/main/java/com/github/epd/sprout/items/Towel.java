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
package com.github.epd.sprout.items;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Ooze;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class Towel extends Item {

	{
		defaultAction = AC_TOWEL;
	}

	private static final String TXT_PREVENTING = Messages.get(Towel.class, "prevent");
	private static final String TXT_APPLY = Messages.get(Towel.class, "apply");
	private static final String TXT_END = Messages.get(Towel.class, "end");

	public static final float TIME_TO_USE = 1;

	public static final String AC_TOWEL = Messages.get(Towel.class, "ac_apply");
	public static final String AC_TOWEL_PET = Messages.get(Towel.class, "ac_pet");

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.TOWEL;
		level = 10;

	}


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);

		actions.add(AC_TOWEL);

		if (checkpet() != null && checkpetNear()) {
			actions.add(AC_TOWEL_PET);
		}

		return actions;
	}

	private PET checkpet() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	private boolean checkpetNear() {
		Hero hero = Dungeon.hero;
		for (int n : PathFinder.NEIGHBOURS8) {
			int c = hero.pos + n;
			if (Actor.findChar(c) instanceof PET) {
				return true;
			}
		}
		return false;
	}


	@Override
	public void execute(Hero hero, String action) {


		if (action == AC_TOWEL) {

			Buff.detach(hero, Bleeding.class);
			Buff.detach(hero, Ooze.class);
			Dungeon.observe();

			GLog.i(TXT_APPLY);

			//	  level -= 1;
			if (level == 0) {
				detach(Dungeon.hero.belongings.backpack);
				GLog.n(TXT_END);
			}


		} else if (action == AC_TOWEL_PET) {

			PET pet = checkpet();

			Buff.detach(pet, Bleeding.class);
			Buff.detach(pet, Ooze.class);
			Dungeon.observe();

			GLog.i(TXT_APPLY);

			level -= 1;
			if (level == 0) {
				detach(Dungeon.hero.belongings.backpack);
				GLog.n(TXT_END);
			}


		} else {

			super.execute(hero, action);
		}
	}

	@Override
	public int price() {
		return 500 * quantity;
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
	public String info() {
		return Messages.get(this, "desc");
	}

}
