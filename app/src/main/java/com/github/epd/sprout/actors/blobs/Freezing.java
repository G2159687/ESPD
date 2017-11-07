
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.SnowParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.watabou.utils.Random;

public class Freezing {

	// Returns true, if this cell is visible
	public static boolean affect(int cell, Fire fire) {

		Char ch = Actor.findChar(cell);
		if (ch != null) {
			if (Level.water[ch.pos]) {
				Buff.prolong(ch, Frost.class,
						Frost.duration(ch) * Random.Float(5f, 7.5f));
			} else {
				Buff.prolong(ch, Frost.class,
						Frost.duration(ch) * Random.Float(1.0f, 1.5f));
			}
		}

		if (fire != null) {
			fire.clear(cell);
		}

		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap != null) {
			heap.freeze();
		}

		if (Dungeon.visible[cell]) {
			CellEmitter.get(cell).start(SnowParticle.FACTORY, 0.2f, 6);
			return true;
		} else {
			return false;
		}
	}
}
