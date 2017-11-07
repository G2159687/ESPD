
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.PoisonParticle;
import com.github.epd.sprout.items.Heap;

public class PoisonTrap {

	// 0xBB66EE

	public static void trigger(int pos, Char ch) {

		if (ch != null) {
			Buff.affect(ch, Poison.class).set(
					Poison.durationFactor(ch) * (4 + Dungeon.depth / 2));
		}

		CellEmitter.center(pos).burst(PoisonParticle.SPLASH, 3);
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.poison();
		}

	}
}
