
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;

public class CryptRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		Point c = center();
		int cx = c.x;
		int cy = c.y;

		Door entrance = entrance();

		entrance.set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));

		if (entrance.x == left) {
			Painter.set(level, new Point(right - 1, top + 1), Terrain.STATUE);
			Painter.set(level, new Point(right - 1, bottom - 1),
					Terrain.STATUE);
			cx = right - 2;
		} else if (entrance.x == right) {
			Painter.set(level, new Point(left + 1, top + 1), Terrain.STATUE);
			Painter.set(level, new Point(left + 1, bottom - 1),
					Terrain.STATUE);
			cx = left + 2;
		} else if (entrance.y == top) {
			Painter.set(level, new Point(left + 1, bottom - 1),
					Terrain.STATUE);
			Painter.set(level, new Point(right - 1, bottom - 1),
					Terrain.STATUE);
			cy = bottom - 2;
		} else if (entrance.y == bottom) {
			Painter.set(level, new Point(left + 1, top + 1), Terrain.STATUE);
			Painter.set(level, new Point(right - 1, top + 1), Terrain.STATUE);
			cy = top + 2;
		}

		level.drop(prize(level), cx + cy * level.getWidth()).type = Heap.Type.TOMB;
	}

	private static Item prize(Level level) {

		Item prize = Generator.random(Generator.Category.ARMOR);

		for (int i = 0; i < 3; i++) {
			Item another = Generator.random(Generator.Category.ARMOR);
			if (another.level > prize.level) {
				prize = another;
			}
		}

		return prize;
	}
}
