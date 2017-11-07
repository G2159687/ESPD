
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class TreasuryRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		Painter.set(level, center(), Terrain.STATUE);

		Heap.Type heapType = Random.Int(2) == 0 ? Heap.Type.CHEST
				: Heap.Type.HEAP;

		int n = Random.IntRange(3, 4);
		for (int i = 0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY
					|| level.heaps.get(pos) != null);
			level.drop(Dungeon.isChallenged(Challenges.NO_ARMOR) ?
					Generator.random(Random.oneOf(Generator.Category.POTION, Generator.Category.SCROLL, Generator.Category.FOOD, Generator.Category.BERRY, Generator.Category.MUSHROOM, Generator.Category.SEED))
					: new Gold().random(), pos).type = (i == 0 && heapType == Heap.Type.CHEST ? Heap.Type.MIMIC : heapType);
		}

		if (heapType == Heap.Type.HEAP) {
			for (int i = 0; i < 6; i++) {
				int pos;
				do {
					pos = level.pointToCell(random());
				} while (level.map[pos] != Terrain.EMPTY);
				level.drop(new Gold(Random.IntRange(5, 12)), pos);
			}
		}

		entrance().set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));
	}
}
