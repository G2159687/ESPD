package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.actors.mobs.Piranha;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;

public class AquariumRoom extends StandardRoom {

	@Override
	public int minWidth() {
		return Math.max(super.minWidth(), 7);
	}

	@Override
	public int minHeight() {
		return Math.max(super.minHeight(), 7);
	}

	@Override
	public float[] sizeCatProbs() {
		return new float[]{3, 1, 0};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1, Terrain.EMPTY );
		Painter.fill( level, this, 2, Terrain.EMPTY_SP );
		Painter.fill( level, this, 3, Terrain.WATER );

		int minDim = Math.min(width(), height());
		int numFish = (minDim - 4)/3; //1-3 fish, depending on room size

		for (int i=0; i < numFish; i++) {
			Piranha piranha = new Piranha();
			do {
				piranha.pos = level.pointToCell(random(3));
			} while (level.map[piranha.pos] != Terrain.WATER|| level.findMob( piranha.pos ) != null);
			level.mobs.add( piranha );
		}

		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		super.paint(level);
	}

}
