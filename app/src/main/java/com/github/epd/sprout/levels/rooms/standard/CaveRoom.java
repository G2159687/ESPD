package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;

public class CaveRoom extends PatchRoom {

	@Override
	public float[] sizeCatProbs() {
		return new float[]{9, 3, 1};
	}

	@Override
	public void paint(Level level) {
		Painter.fill( level, this, Terrain.WALL );
		Painter.fill( level, this, 1 , Terrain.EMPTY );
		for (Door door : connected.values()) {
			door.set( Door.Type.REGULAR );
		}

		//fill scales from ~25% at 4x4, to ~55% at 18x18
		// normal   ~25% to ~35%
		// large    ~35% to ~45%
		// giant    ~45% to ~55%
		float fill = 0.25f + (width()*height())/1024f;

		setupPatch(level, fill, 4, true);
		cleanDiagonalEdges();

		for (int i = top + 1; i < bottom; i++) {
			for (int j = left + 1; j < right; j++) {
				if (patch[xyToPatchCoords(j, i)]) {
					int cell = i * level.getWidth() + j;
					level.map[cell] = Terrain.WALL;
				}
			}
		}
	}

}
