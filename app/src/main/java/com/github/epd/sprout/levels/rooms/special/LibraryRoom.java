
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.scrolls.Scroll;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class LibraryRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		Door entrance = entrance();
		Point a = null;
		Point b = null;

		if (entrance.x == left) {
			a = new Point(left + 1, entrance.y - 1);
			b = new Point(left + 1, entrance.y + 1);
			Painter.fill(level, right - 1, top + 1, 1, height() - 2,
					Terrain.BOOKSHELF);
		} else if (entrance.x == right) {
			a = new Point(right - 1, entrance.y - 1);
			b = new Point(right - 1, entrance.y + 1);
			Painter.fill(level, left + 1, top + 1, 1, height() - 2,
					Terrain.BOOKSHELF);
		} else if (entrance.y == top) {
			a = new Point(entrance.x + 1, top + 1);
			b = new Point(entrance.x - 1, top + 1);
			Painter.fill(level, left + 1, bottom - 1, width() - 2, 1,
					Terrain.BOOKSHELF);
		} else if (entrance.y == bottom) {
			a = new Point(entrance.x + 1, bottom - 1);
			b = new Point(entrance.x - 1, bottom - 1);
			Painter.fill(level, left + 1, top + 1, width() - 2, 1,
					Terrain.BOOKSHELF);
		}
		if (a != null && level.map[a.x + a.y * level.getWidth()] == Terrain.EMPTY) {
			Painter.set(level, a, Terrain.STATUE);
		}
		if (b != null && level.map[b.x + b.y * level.getWidth()] == Terrain.EMPTY) {
			Painter.set(level, b, Terrain.STATUE);
		}

		int n = Random.IntRange(4, 5);
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

		Item prize = level.findPrizeItem(Scroll.class);
		if (prize == null)
			prize = Generator.random(Generator.Category.SCROLL);

		return prize;
	}
}
