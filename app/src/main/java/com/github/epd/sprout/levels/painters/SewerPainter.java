package com.github.epd.sprout.levels.painters;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SewerPainter extends RegularPainter {

	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {

		int[] map = level.map;
		int w = level.getWidth();
		int l = level.getLength();

		for (int i=0; i < w; i++) {
			if (map[i] == Terrain.WALL &&
					map[i + w] == Terrain.WATER &&
					Random.Int( 4 ) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		for (int i=w; i < l - w; i++) {
			if (map[i] == Terrain.WALL &&
					map[i - w] == Terrain.WALL &&
					map[i + w] == Terrain.WATER &&
					Random.Int( 2 ) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		for (int i=w + 1; i < l - w - 1; i++) {
			if (map[i] == Terrain.EMPTY) {

				int count =
						(map[i + 1] == Terrain.WALL ? 1 : 0) +
								(map[i - 1] == Terrain.WALL ? 1 : 0) +
								(map[i + w] == Terrain.WALL ? 1 : 0) +
								(map[i - w] == Terrain.WALL ? 1 : 0);

				if (Random.Int( 16 ) < count * count) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}
	}
}
