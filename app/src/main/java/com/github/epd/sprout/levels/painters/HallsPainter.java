package com.github.epd.sprout.levels.painters;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.mobs.Sentinel;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HallsPainter extends RegularPainter {

	@Override
	protected void decorate(Level level, ArrayList<Room> rooms) {

		int[] map = level.map;
		int w = level.getWidth();
		int l = level.getLength();

		for (int i=w + 1; i < l - w - 1; i++) {
			if (map[i] == Terrain.EMPTY) {

				int count = 0;
				for (int j = 0; j < PathFinder.NEIGHBOURS8.length; j++) {
					if ((Terrain.flags[map[i + PathFinder.NEIGHBOURS8[j]]] & Terrain.PASSABLE) > 0) {
						count++;
					}
				}

				if (Random.Int( 80 ) < count) {
					map[i] = Terrain.EMPTY_DECO;
				}

			} else
			if (map[i] == Terrain.WALL &&
					map[i-1] != Terrain.WALL_DECO && map[i-w] != Terrain.WALL_DECO &&
					Random.Int( 20 ) == 0) {

				map[i] = Terrain.WALL_DECO;

			}
		}

		for (int i = 0; i < level.getLength(); i++) {

			if (Dungeon.depth == 24 && map[i] == Terrain.EXIT) {
				level.sealedlevel = true;
				map[i] = Terrain.EMBERS;
				Sentinel sentinel = new Sentinel();
				sentinel.pos = i;
				level.mobs.add(sentinel);

			}

		}
	}
}
