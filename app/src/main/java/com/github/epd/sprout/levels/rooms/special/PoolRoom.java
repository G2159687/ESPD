
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.mobs.Piranha;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.PotionOfInvisibility;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class PoolRoom extends SpecialRoom {

	private static final int NPIRANHAS = 3;

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.WATER);

		Door door = entrance();
		door.set(Door.Type.REGULAR);

		int x = -1;
		int y = -1;
		if (door.x == left) {

			x = right - 1;
			y = top + height() / 2;

		} else if (door.x == right) {

			x = left + 1;
			y = top + height() / 2;

		} else if (door.y == top) {

			x = left + width() / 2;
			y = bottom - 1;

		} else if (door.y == bottom) {

			x = left + width() / 2;
			y = top + 1;

		}

		int pos = x + y * level.getWidth();
		level.drop(prize(level), pos).type = Random.Int(3) == 0 ? Heap.Type.CHEST
				: Heap.Type.HEAP;
		Painter.set(level, pos, Terrain.PEDESTAL);

		level.addItemToSpawn(new PotionOfInvisibility());

		for (int i = 0; i < NPIRANHAS; i++) {
			Piranha piranha = new Piranha();
			do {
				piranha.pos = level.pointToCell(random());
			} while (level.map[piranha.pos] != Terrain.WATER
					|| Actor.findChar(piranha.pos) != null);
			level.mobs.add(piranha);
		}
	}


	//TODO:Going to change this
	private static Item prize(Level level) {

		Item prize;

		if (Random.Int(3) != 0) {
			prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}

		prize = Generator.random(Random.oneOf(Generator.Category.WEAPON,
				Generator.Category.ARMOR));

		for (int i = 0; i < 4; i++) {
			Item another = Generator.random(Random.oneOf(
					Generator.Category.WEAPON, Generator.Category.ARMOR));
			if (another.level > prize.level) {
				prize = another;
			}
		}

		return prize;
	}
}
