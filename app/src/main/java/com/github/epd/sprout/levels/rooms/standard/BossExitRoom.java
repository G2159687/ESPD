
package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;

public class BossExitRoom extends StandardRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		for (Room.Door door : connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}

		level.exit = top * level.getWidth() + (left + right) / 2;
		Painter.set(level, level.exit, Terrain.LOCKED_EXIT);
	}

}
