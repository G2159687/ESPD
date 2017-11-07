package com.github.epd.sprout.levels.painters;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class CityPainter extends RegularPainter {

	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {

		int[] map = level.map;
		int w = level.getWidth();
		int l = level.getLength();

		for (int i=0; i < l - w; i++) {

			if (map[i] == Terrain.EMPTY && Random.Int( 10 ) == 0) {
				map[i] = Terrain.EMPTY_DECO;

			} else if (map[i] == Terrain.WALL
					&& Random.Int( 5 ) == 0) {
				map[i] = Terrain.WALL_DECO;
			}
		}

	}
}
