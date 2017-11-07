
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.PotionOfLevitation;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class TrapsRoom extends SpecialRoom {

	public void paint(Level level) {

		Integer traps[] = {
				Terrain.TOXIC_TRAP,
				Terrain.TOXIC_TRAP,
				Terrain.TOXIC_TRAP,
				Terrain.PARALYTIC_TRAP,
				Terrain.PARALYTIC_TRAP,
				!Dungeon.bossLevel(Dungeon.depth + 1) && (Dungeon.depth < 22 || Dungeon.depth > 26) && !Dungeon.townCheck(Dungeon.depth) ? Terrain.CHASM
						: Terrain.SUMMONING_TRAP};
		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Random.element(traps));

		Door door = entrance();
		door.set(Door.Type.REGULAR);

		int lastRow = level.map[left + 1 + (top + 1) * level.getWidth()] == Terrain.CHASM ? Terrain.CHASM
				: Terrain.EMPTY;

		int x = -1;
		int y = -1;
		if (door.x == left) {
			x = right - 1;
			y = top + height() / 2;
			Painter.fill(level, x, top + 1, 1, height() - 2, lastRow);
		} else if (door.x == right) {
			x = left + 1;
			y = top + height() / 2;
			Painter.fill(level, x, top + 1, 1, height() - 2, lastRow);
		} else if (door.y == top) {
			x = left + width() / 2;
			y = bottom - 1;
			Painter.fill(level, left + 1, y, width() - 2, 1, lastRow);
		} else if (door.y == bottom) {
			x = left + width() / 2;
			y = top + 1;
			Painter.fill(level, left + 1, y, width() - 2, 1, lastRow);
		}

		int pos = x + y * level.getWidth();
		if (Random.Int(3) == 0) {
			if (lastRow == Terrain.CHASM) {
				Painter.set(level, pos, Terrain.EMPTY);
			}
			level.drop(prize(level), pos).type = Heap.Type.CHEST;
		} else {
			Painter.set(level, pos, Terrain.PEDESTAL);
			level.drop(prize(level), pos);
		}

		level.addItemToSpawn(new PotionOfLevitation());
	}

	private static Item prize(Level level) {

		Item prize;

		if (Random.Int(4) != 0) {
			prize = level.findPrizeItem();
			if (prize != null)
				return prize;
		}

		prize = Generator.random(Random.oneOf(Generator.Category.WEAPON,
				Generator.Category.ARMOR));

		for (int i = 0; i < 3; i++) {
			Item another = Generator.random(Random.oneOf(
					Generator.Category.WEAPON, Generator.Category.ARMOR));
			if (another.level > prize.level) {
				prize = another;
			}
		}

		return prize;
	}
}
