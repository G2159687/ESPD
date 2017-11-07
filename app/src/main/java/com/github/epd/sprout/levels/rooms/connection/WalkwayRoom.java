package com.github.epd.sprout.levels.rooms.connection;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Rect;

public class WalkwayRoom extends PerimeterRoom {

	@Override
	public void paint(Level level) {

		if (Math.min(width(), height()) > 3) {
			Painter.fill(level, this, 1, Terrain.CHASM);
		}

		super.paint(level);

		for (Room r : neigbours){
			if (r instanceof BridgeRoom || r instanceof WalkwayRoom){
				Rect i = intersect(r);
				if (i.width() != 0){
					i.left++;
					i.right--;
				} else {
					i.top++;
					i.bottom--;
				}
				Painter.fill(level, i.left, i.top, i.width()+1, i.height()+1, Terrain.CHASM);
			}
		}
	}
}
