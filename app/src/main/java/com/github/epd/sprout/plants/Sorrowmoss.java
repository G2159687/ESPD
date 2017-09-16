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

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PoisonParticle;
import com.github.epd.sprout.items.potions.PotionOfToxicGas;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;

public class Sorrowmoss extends Plant {

	private static final String TXT_DESC = Messages.get(Sorrowmoss.class, "desc");

	{
		image = 2;
		plantName = Messages.get(this, "name");
	}

	@Override
	public void activate(Char ch) {
		super.activate(ch);

		if (ch != null) {
			Buff.affect(ch, Poison.class).set(
					Poison.durationFactor(ch) * (4 + Dungeon.depth / 2));
		}

		if (Dungeon.visible[pos]) {
			CellEmitter.center(pos).burst(PoisonParticle.SPLASH, 3);
		}
	}

	@Override
	public String desc() {
		return TXT_DESC;
	}

	public static class Seed extends Plant.Seed {
		{
			plantName = Messages.get(Sorrowmoss.class, "name");

			name = Messages.get(this, "name");
			image = ItemSpriteSheet.SEED_SORROWMOSS;

			plantClass = Sorrowmoss.class;
			alchemyClass = PotionOfToxicGas.class;
		}

		@Override
		public String desc() {
			return Messages.get(Plant.class, "seeddesc", plantName);
		}
	}
}
