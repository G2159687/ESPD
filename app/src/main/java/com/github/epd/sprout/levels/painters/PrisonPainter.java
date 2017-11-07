package com.github.epd.sprout.levels.painters;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class PrisonPainter extends RegularPainter {

	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {

		int w = level.getWidth();
		int l = level.getLength();
		int[] map = level.map;

		for (int i=w + 1; i < l - w - 1; i++) {
			if (map[i] == Terrain.EMPTY) {

				float c = 0.05f;
				if (map[i + 1] == Terrain.WALL && map[i + w] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i + w] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i + 1] == Terrain.WALL && map[i - w] == Terrain.WALL) {
					c += 0.2f;
				}
				if (map[i - 1] == Terrain.WALL && map[i - w] == Terrain.WALL) {
					c += 0.2f;
				}

				if (Random.Float() < c) {
					map[i] = Terrain.EMPTY_DECO;
				}
			}
		}

		for (int i=0; i < w; i++) {
			if (map[i] == Terrain.WALL &&
					(map[i + w] == Terrain.EMPTY || map[i + w] == Terrain.EMPTY_SP) &&
					Random.Int( 6 ) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}

		for (int i=w; i < l - w; i++) {
			if (map[i] == Terrain.WALL &&
					map[i - w] == Terrain.WALL &&
					(map[i + w] == Terrain.EMPTY || map[i + w] == Terrain.EMPTY_SP) &&
					Random.Int( 3 ) == 0) {

				map[i] = Terrain.WALL_DECO;
			}
		}
	}
}
