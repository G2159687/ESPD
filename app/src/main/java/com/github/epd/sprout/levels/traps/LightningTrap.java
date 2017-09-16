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
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Lightning;
import com.github.epd.sprout.effects.particles.SparkParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.Camera;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class LightningTrap {

	private static final String name = Messages.get(LightningTrap.class, "name");

	// 00x66CCEE

	public static void trigger(int pos, Char ch) {

		if (ch != null) {
			ch.damage(Math.max(1, Random.Int(ch.HP / 3, 2 * ch.HP / 3)),
					LIGHTNING);
			if (ch == Dungeon.hero) {

				Camera.main.shake(2, 0.3f);

				if (!ch.isAlive()) {
					Dungeon.fail(Utils.format(ResultDescriptions.TRAP, name));
					GLog.n(Messages.get(LightningTrap.class, "ondeath"));
				} else {
					((Hero) ch).belongings.charge(false);
				}
			}

			ArrayList<Lightning.Arc> arcs = new ArrayList<>();
			arcs.add(new Lightning.Arc(pos - Level.WIDTH, pos + Level.WIDTH));
			arcs.add(new Lightning.Arc(pos - 1, pos + 1));

			ch.sprite.parent.add(new Lightning(arcs, null));
		}

		CellEmitter.center(pos).burst(SparkParticle.FACTORY,
				Random.IntRange(3, 4));

		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.lit();
		}

	}

	public static final Electricity LIGHTNING = new Electricity();

	public static class Electricity {
	}
}
