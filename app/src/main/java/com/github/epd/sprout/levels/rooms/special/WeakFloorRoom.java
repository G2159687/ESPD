
package com.github.epd.sprout.levels.rooms.special;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.watabou.utils.Point;
import com.watabou.utils.Random;

public class WeakFloorRoom extends SpecialRoom {

	public void paint(Level level) {

		Painter.fill(level, this, Terrain.WALL);
		Painter.fill(level, this, 1, Terrain.CHASM);

		Door door = entrance();
		door.set(Door.Type.REGULAR);

		if (door.x == left) {
			for (int i = top + 1; i < bottom; i++) {
				Painter.drawInside(level, this, new Point(left, i),
						Random.IntRange(1, width() - 3), Terrain.EMPTY_SP);
			}
		} else if (door.x == right) {
			for (int i = top + 1; i < bottom; i++) {
				Painter.drawInside(level, this, new Point(right, i),
						Random.IntRange(1, width() - 3), Terrain.EMPTY_SP);
			}
		} else if (door.y == top) {
			for (int i = left + 1; i < right; i++) {
				Painter.drawInside(level, this, new Point(i, top),
						Random.IntRange(1, height() - 3), Terrain.EMPTY_SP);
			}
		} else if (door.y == bottom) {
			for (int i = left + 1; i < right; i++) {
				Painter.drawInside(level, this, new Point(i, bottom),
						Random.IntRange(1, height() - 3), Terrain.EMPTY_SP);
			}
		}
	}
}
