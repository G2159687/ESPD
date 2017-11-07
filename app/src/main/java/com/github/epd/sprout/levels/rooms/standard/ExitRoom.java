
package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;

public class ExitRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 5);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 5);
	}

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.EMPTY);

		for (Room.Door door : connected.values()) {
			door.set(Room.Door.Type.REGULAR);
		}

		level.exit = level.pointToCell(random(2));
		Painter.set(level, level.exit, Terrain.EXIT);

	}

}
