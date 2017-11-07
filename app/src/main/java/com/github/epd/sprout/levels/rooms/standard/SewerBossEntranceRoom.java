package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Point;

public class SewerBossEntranceRoom extends EntranceRoom {

	@Override
	public int minWidth() {
		return 9;
	}

	@Override
	public int maxWidth() {
		return 9;
	}

	@Override
	public int minHeight() {
		return 6;
	}

	@Override
	public int maxHeight() {
		return 10;
	}

	@Override
	public boolean canConnect(Point p) {
		//refuses connections on the center 3 tiles on the top side, and the top tile along left/right
		return super.canConnect(p)
				&& !(p.y == top && p.x >= (left + (width()/2 - 1)) && p.x <= (left + (width()/2 + 1)))
				&& p.y != top+1;
	}

	public void paint(Level level ) {

		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );

		Painter.fill( level, left+1, top+1, width()-2, 1, Terrain.WALL_DECO);
		Painter.fill( level, left+1, top+2, width()-2, 1, Terrain.WATER);

		Painter.set( level, left+width()/2, top+1, Terrain.LOCKED_EXIT);
		level.exit = level.pointToCell(new Point(left+width()/2, top+1));

		do {
			level.entrance = level.pointToCell(random(3));
		} while (level.findMob(level.entrance) != null);
		Painter.set( level, level.entrance, Terrain.ENTRANCE );

		for (Room.Door door : connected.values()) {
			door.set( Room.Door.Type.REGULAR );

			if (door.y == top){
				Painter.set( level, door.x, door.y+1, Terrain.WATER);
			}
		}

	}

}
