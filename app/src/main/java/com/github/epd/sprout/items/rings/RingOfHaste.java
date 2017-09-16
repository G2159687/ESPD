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
package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndPetHaste;

public class RingOfHaste extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Haste();
	}


	private PET checkpet() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc")
				+ Messages.get(this, "desc2")
				: super.desc();
	}

	public class Haste extends RingBuff {
	}


	@Override
	public boolean doEquip(Hero hero) {

		PET heropet = checkpet();


		if (Dungeon.hero.haspet && heropet != null && isKnown()) {
			GameScene.show(new WndPetHaste(heropet, this));
		}

		return super.doEquip(hero);
	}

	@Override
	public boolean doUnequip(Hero hero, boolean collect, boolean single) {

		PET heropet = checkpet();

		if (Dungeon.hero.haspet && heropet != null && Dungeon.petHasteLevel > 0) {
			Dungeon.petHasteLevel = 0;
			GLog.w(Messages.get(this, "pet", heropet.name));
		}

		return super.doUnequip(hero, collect, single);

	}


	@Override
	public Item upgrade() {

		if (level > 7) {
			GLog.w(Messages.get(this, "warning"));
		}

		if (level > 8) {
			level = 9;
		}

		return super.upgrade();

	}


}
