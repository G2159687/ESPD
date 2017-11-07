package com.github.epd.sprout.levels.rooms.standard;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.levels.painters.Painter;

public class RuinsRoom extends PatchRoom {

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

		//fill scales from ~10% at 4x4, to ~25% at 18x18
		// normal   ~20% to ~25%
		// large    ~25% to ~30%
		// giant    ~30% to ~35%
		float fill = .2f + (width()*height())/2048f;

		setupPatch(level, fill, 0, true);
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
