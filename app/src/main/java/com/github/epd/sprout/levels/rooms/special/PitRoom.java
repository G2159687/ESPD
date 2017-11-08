
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.items.consumables.Ankh;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.ScrollOfTeleportation;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.plants.Fadeleaf;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class PitRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		Door entrance = entrance();
		entrance.set(Door.Type.ONEWAY);

		Point well = null;
		if (entrance.x == left) {
			well = new Point(right - 1, Random.Int(2) == 0 ? top + 1
					: bottom - 1);
		} else if (entrance.x == right) {
			well = new Point(left + 1, Random.Int(2) == 0 ? top + 1
					: bottom - 1);
		} else if (entrance.y == top) {
			well = new Point(Random.Int(2) == 0 ? left + 1
					: right - 1, bottom - 1);
		} else if (entrance.y == bottom) {
			well = new Point(Random.Int(2) == 0 ? left + 1
					: right - 1, top + 1);
		}
		Painter.set(level, well, Terrain.EMPTY_WELL);

		int remains = level.pointToCell(random());
		while (level.map[remains] == Terrain.EMPTY_WELL) {
			remains = level.pointToCell(random());
		}

		level.drop(new ScrollOfTeleportation(), remains).type = Heap.Type.SKELETON;
		int loot = Random.Int(3);
		if (loot == 0) {
			level.drop(Generator.random(Generator.Category.RING), remains);
		} else if (loot == 1) {
			level.drop(Generator.random(Generator.Category.ARTIFACT), remains);
		} else {
			level.drop(Generator.random(Random.oneOf(Generator.Category.WEAPON,
					Generator.Category.ARMOR)), remains);
		}
		level.drop(new Ankh(), remains);
		level.drop(new Fadeleaf.Seed(), remains);
		int n = Random.IntRange(1, 2);
		for (int i = 0; i < n; i++) {
			level.drop(prize(level), remains);
		}
	}

	private static Item prize(Level level) {

		if (Random.Int(2) != 0) {
			Item prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}

		return Generator.random(Random.oneOf(Generator.Category.POTION,
				Generator.Category.SCROLL, Generator.Category.FOOD,
				Generator.Category.GOLD));
	}

	@Override
	public boolean canPlaceTrap(Point p) {
		return false;
	}
}
