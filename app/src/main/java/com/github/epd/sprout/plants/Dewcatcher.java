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
package com.github.epd.sprout.plants;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.VioletDewdrop;
import com.github.epd.sprout.items.YellowDewdrop;
import com.github.epd.sprout.items.potions.PotionOfOverHealing;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.features.HighGrass;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class Dewcatcher extends Plant {

	private static final String TXT_DESC = Messages.get(Dewcatcher.class, "desc");

	{
		image = 12;
		plantName = Messages.get(this, "name");
	}

	@Override
	public void activate(Char ch) {

		explodeDew(pos);
		if (Random.Int(2) == 0) {
			super.activate(ch);
		}


	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Dewcatcher.class, "name");

			name = Messages.get(this, "name");
			image = ItemSpriteSheet.SEED_DEWCATCHER;

			plantClass = Dewcatcher.class;
			alchemyClass = PotionOfOverHealing.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class, "seeddesc", plantName);
		}

	}

	public void explodeDew(int cell) {

		DewVial vial = Dungeon.hero.belongings.getItem(DewVial.class);

		for (int n : PathFinder.NEIGHBOURS8) {
			int c = cell + n;
			if (c >= 0 && c < Level.getLength() && Level.passable[c]) {

				if (Random.Int(10) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (vial.volume <= (DewVial.MAX_VOLUME() - 45)) {
							vial.volume = vial.volume + (Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE) ? 200 : 50);
							GLog.i(Messages.get(HighGrass.class, "violet"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new VioletDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new VioletDewdrop(), c).sprite.drop();
				} else if (Random.Int(5) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (!vial.isFull()) {
							vial.volume = vial.volume + (Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE) ? 50 : 5);
							GLog.i(Messages.get(HighGrass.class, "red"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new RedDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new RedDewdrop(), c).sprite.drop();
				} else if (Random.Int(3) == 1) {
					if (ShatteredPixelDungeon.autocollect() && vial != null) {
						if (!vial.isFull()) {
							vial.volume = vial.volume + (Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE) ? 20 : 2);
							GLog.i(Messages.get(HighGrass.class, "yellow"));
							if (vial.isFull()) {
								vial.volume = DewVial.MAX_VOLUME();
								Messages.get(DewVial.class, "full");
							}
						} else Dungeon.level.drop(new YellowDewdrop(), c).sprite.drop();
					} else Dungeon.level.drop(new YellowDewdrop(), c).sprite.drop();
				}
				QuickSlotButton.refresh();
			}
		}

	}


}
