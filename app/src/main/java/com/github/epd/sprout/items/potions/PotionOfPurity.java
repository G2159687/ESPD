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
package com.github.epd.sprout.items.potions;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.blobs.ParalyticGas;
import com.github.epd.sprout.actors.blobs.StenchGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.GasesImmunity;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.BArray;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;

public class PotionOfPurity extends Potion {

	private static final String TXT_FRESHNESS = Messages.get(PotionOfPurity.class,"freshness");
	private static final String TXT_NO_SMELL = Messages.get(PotionOfPurity.class,"no_smell");

	private static final int DISTANCE = 5;

	{
		initials=9;
		name = Messages.get(this,"name");
	}

	@Override
	public void shatter(int cell) {

		PathFinder.buildDistanceMap(cell, BArray.not(Level.losBlocking, null),
				DISTANCE);

		boolean procd = false;

		Blob[] blobs = { Dungeon.level.blobs.get(ToxicGas.class),
				Dungeon.level.blobs.get(ParalyticGas.class),
				Dungeon.level.blobs.get(ConfusionGas.class),
				Dungeon.level.blobs.get(StenchGas.class) };

		for (int j = 0; j < blobs.length; j++) {

			Blob blob = blobs[j];
			if (blob == null) {
				continue;
			}

			for (int i = 0; i < Level.getLength(); i++) {
				if (PathFinder.distance[i] < Integer.MAX_VALUE) {

					int value = blob.cur[i];
					if (value > 0) {

						blob.cur[i] = 0;
						blob.volume -= value;
						procd = true;

						if (Dungeon.visible[i]) {
							CellEmitter.get(i).burst(
									Speck.factory(Speck.DISCOVER), 1);
						}
					}

				}
			}
		}

		boolean heroAffected = PathFinder.distance[Dungeon.hero.pos] < Integer.MAX_VALUE;

		if (procd) {

			if (Dungeon.visible[cell]) {
				splash(cell);
				Sample.INSTANCE.play(Assets.SND_SHATTER);
			}

			setKnown();

			if (heroAffected) {
				GLog.p(TXT_FRESHNESS);
			}

		} else {

			super.shatter(cell);

			if (heroAffected) {
				GLog.i(TXT_FRESHNESS);
				setKnown();
			}

		}
	}

	@Override
	public void apply(Hero hero) {
		GLog.w(TXT_NO_SMELL);
		Buff.prolong(hero, GasesImmunity.class, GasesImmunity.DURATION);
		setKnown();
	}

	@Override
	public String desc() {
		return Messages.get(this,"desc");
	}

	@Override
	public int price() {
		return isKnown() ? 50 * quantity : super.price();
	}
}
