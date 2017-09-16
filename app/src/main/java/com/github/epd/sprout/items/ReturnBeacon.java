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
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.watabou.noosa.Game;

import java.util.ArrayList;

public class ReturnBeacon extends Item {

	{
		defaultAction = AC_RETURN;
	}


	private static final String TXT_INFO = Messages.get(ReturnBeacon.class, "desc");

	public static final float TIME_TO_USE = 1;

	//public static final String AC_SET = "SET";
	public static final String AC_RETURN = Messages.get(ReturnBeacon.class, "ac_return");

	//private int returnDepth = -1;
	//private int returnPos;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.BEACON;

		unique = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_RETURN);
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_RETURN) {


			Buff buff = Dungeon.hero
					.buff(TimekeepersHourglass.timeFreeze.class);
			if (buff != null)
				buff.detach();

			for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
				if (mob instanceof DriedRose.GhostHero)
					mob.destroy();

			InterlevelScene.mode = InterlevelScene.Mode.RETURNSAVE;
			InterlevelScene.returnDepth = 1;
			InterlevelScene.returnPos = 1;
			Game.switchScene(InterlevelScene.class);


		} else {

			super.execute(hero, action);

		}
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
		return TXT_INFO;
	}
}
