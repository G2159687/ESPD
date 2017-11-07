
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.keys.GoldenKey;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class VaultRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		int cx = (left + right) / 2;
		int cy = (top + bottom) / 2;
		int c = cx + cy * level.getWidth();

		switch (Random.Int(3)) {

			case 0:
				//if (Random.Int(1)==2){
				level.drop(prizeUncursed(level), c).type = Heap.Type.LOCKED_CHEST;
				//} else {
				//level.drop(prize(level), c).type = Type.MONSTERBOX;
				//}
				level.addItemToSpawn(new GoldenKey(Dungeon.depth));
				break;

			case 1:
				Item i1,
						i2;
				do {
					i1 = prizeUncursed(level);
					i2 = prizeUncursed(level);
				} while (i1.getClass() == i2.getClass());
				level.drop(i1, c).type = Heap.Type.CRYSTAL_CHEST;
				level.drop(i2, c + PathFinder.NEIGHBOURS8[Random.Int(8)]).type = Heap.Type.CRYSTAL_CHEST;
				level.addItemToSpawn(new GoldenKey(Dungeon.depth));
				break;

			case 2:
				level.drop(prizeUncursed(level), c);
				Painter.set(level, c, Terrain.PEDESTAL);
				break;
		}

		entrance().set(Door.Type.LOCKED);
		level.addItemToSpawn(new IronKey(Dungeon.depth));
	}

	private static Item prize(Level level) {
		return Generator.random(Random.oneOf(Generator.Category.WAND,
				Generator.Category.RING, Generator.Category.ARTIFACT));
	}


	private static Item prizeUncursed(Level level) {

		Item item = Generator.random(Random.oneOf(Generator.Category.WAND, Generator.Category.RING, Generator.Category.ARTIFACT));

		if (item != null && item.cursed && item.isUpgradable()) {
			item.cursed = false;
			if (item.level < 0) {
				item.upgrade(-item.level);
			} //upgrade to even
		}

		return item;
	}

}
