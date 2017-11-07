
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.blobs.Foliage;
import com.github.epd.sprout.items.Ankh;
import com.github.epd.sprout.items.Honeypot;
import com.github.epd.sprout.items.SteelHoneypot;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.plants.BlandfruitBush;
import com.github.epd.sprout.plants.Sungrass;
import com.watabou.utils.Random;

public class FoliageRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.HIGH_GRASS);
		Painter.fill(level, this, 2, Terrain.GRASS);

		entrance().set(Door.Type.REGULAR);

		int bushes = Random.Int(3);
		if (bushes == 0) {
			level.plant(new Sungrass.Seed(), level.pointToCell(random()));
		} else if (bushes == 1) {
			level.plant(new BlandfruitBush.Seed(), level.pointToCell(random()));
		} else if (Random.Int(5) == 0) {
			level.plant(new Sungrass.Seed(), level.pointToCell(random()));
			level.plant(new BlandfruitBush.Seed(), level.pointToCell(random()));
		}

		if (Random.Int(100) == 0) {
			int pos;
			do {
				pos = level.pointToCell(random());
			}
			while (level.heaps.get(pos) != null);
			level.drop(new SteelHoneypot(), pos);
		}

		if (Dungeon.depth == 32 && Random.Float() < 0.75f) {
			int pos;
			do {
				pos = level.pointToCell(random());
			}
			while (level.heaps.get(pos) != null);
			level.drop(new Honeypot(), pos);

			do {
				pos = level.pointToCell(random());
			}
			while (level.heaps.get(pos) != null);
			level.drop(new Honeypot(), pos);

			do {
				pos = level.pointToCell(random());
			}
			while (level.heaps.get(pos) != null);
			level.drop(new Honeypot(), pos);
		}

		if (Dungeon.depth == 32 && Random.Float() < 0.75f) {
			int pos;
			do {
				pos = level.pointToCell(random());
			}
			while (level.heaps.get(pos) != null);
			level.drop(new Ankh(), pos);
		}

		Foliage light = (Foliage) level.blobs.get(Foliage.class);
		if (light == null) {
			light = new Foliage();
		}
		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				light.seed(level, j + level.getWidth() * i, 1);
			}
		}
		level.blobs.put(Foliage.class, light);
	}
}
