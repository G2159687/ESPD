
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.bombs.Bomb;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class ArmoryRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		Door entrance = entrance();
		Point statue = null;
		if (entrance.x == left) {
			statue = new Point(right - 1,
					Random.Int(2) == 0 ? top + 1 : bottom - 1);
		} else if (entrance.x == right) {
			statue = new Point(left + 1, Random.Int(2) == 0 ? top + 1
					: bottom - 1);
		} else if (entrance.y == top) {
			statue = new Point(Random.Int(2) == 0 ? left + 1
					: right - 1, bottom - 1);
		} else if (entrance.y == bottom) {
			statue = new Point(Random.Int(2) == 0 ? left + 1
					: right - 1, top + 1);
		}
		if (statue != null) {
			Painter.set(level, statue, Terrain.STATUE);
		}

		int n = Random.IntRange(2, 3);
		for (int i = 0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != Terrain.EMPTY
					|| level.heaps.get(pos) != null);
			level.drop(prize(level), pos);
		}

		entrance.set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));
	}

	private static Item prize(Level level) {
		return Random.Int(6) == 0 ? new Bomb().random() : Generator
				.random(Random.oneOf(Generator.Category.ARMOR,
						Generator.Category.WEAPON));
	}
}
