
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.consumables.Honeypot;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.PotionOfLiquidFlame;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Random;

public class StorageRoom extends SpecialRoom {

	public void paint(Level level) {

		final int floor = Terrain.EMPTY_SP;

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, floor);

		boolean honeyPot = Random.Int(2) == 0;

		int n = Random.IntRange(3, 4);
		for (int i = 0; i < n; i++) {
			int pos;
			do {
				pos = level.pointToCell(random());
			} while (level.map[pos] != floor);
			if (honeyPot) {
				level.drop(new Honeypot(), pos);
				honeyPot = false;
			} else
				level.drop(prize(level), pos);
		}

		entrance().set(Door.Type.BARRICADE);
		level.addItemToSpawn(new PotionOfLiquidFlame());
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
}
